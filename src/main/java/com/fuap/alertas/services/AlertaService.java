package com.fuap.alertas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuap.alertas.configuration.PubHandler;
import com.fuap.alertas.data.DTO.AlertaDTO;
import com.fuap.alertas.data.DTO.DispositivoDTO;
import com.fuap.alertas.data.DTO.NotificacionDTO;
import com.fuap.alertas.data.models.Alerta;
import com.fuap.alertas.data.repository.AlertasRepository;
import com.fuap.alertas.handlers.Handler;

import reactor.core.publisher.Mono;

@Service
public class AlertaService {
    private final WebClient webClient;
    private final WebClient userWebClient;
    private final AlertasRepository alertaRepository;
    private final Handler userHandler;
    @Autowired
    private ObjectMapper objectMapper;

    private final PubHandler.Gateway mqttAlertGateway;
    @Autowired
    public AlertaService(WebClient.Builder webClientBuilder, WebClient.Builder userWebClientBuilder, AlertasRepository alertaRepository,
            @Qualifier("userChain") Handler handler, PubHandler.Gateway mqttAlertGateway,
            @Value("${service.dispositivos.api.key}") String serviceApiKey,
            @Value("${service.usuarios.api.key}") String usuariosApiKey) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080")
                .defaultHeader("X-Service-API-Key", serviceApiKey).build();

        this.userWebClient = userWebClientBuilder.baseUrl("http://localhost:8086")
                .defaultHeader("X-Service-API-Key", serviceApiKey).build();
        this.alertaRepository = alertaRepository;
        this.userHandler = handler;
        this.mqttAlertGateway = mqttAlertGateway;
    }

    private Alerta crearAlerta(int deviceId, String message, String nivel, Boolean exists, String timestamp) {
        Alerta alertaGuardar = new Alerta();
        alertaGuardar.setDeviceId(deviceId);
        alertaGuardar.setMessage(message);
        alertaGuardar.setDeviceExists(exists);
        alertaGuardar.setTimestamp(timestamp);
        alertaGuardar.setNivel(nivel);
        return alertaGuardar;
    }

    private Alerta crearAlerta(int deviceId, String message, String nivel, Boolean exists, String timestamp,
            int[] handlerUsers) {
        Alerta alertaGuardar = new Alerta();
        alertaGuardar.setDeviceId(deviceId);
        alertaGuardar.setMessage(message);
        alertaGuardar.setDeviceExists(exists);
        alertaGuardar.setTimestamp(timestamp);
        alertaGuardar.setNivel(nivel);
        alertaGuardar.setHandlerUsers(handlerUsers);
        return alertaGuardar;
    }

    public DispositivoDTO getDevice(AlertaDTO alerta) {
        return this.webClient.get()
                .uri("/api/dispositivos/" + alerta.deviceId())
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        _ -> Mono.empty())
                .bodyToMono(DispositivoDTO.class)
                .block();
    }

    public void updateDevice(DispositivoDTO dispositivo, String state) {
        DispositivoDTO dispositivoActualizar = new DispositivoDTO(dispositivo.id(), dispositivo.name(),
                dispositivo.descripcion(), state, dispositivo.type(), dispositivo.schemaJson());
        webClient.put().uri("/api/dispositivos/{id}", dispositivoActualizar.id()).bodyValue(dispositivoActualizar)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public void insertNotification(AlertaDTO alertaDTO, int userId) {
        NotificacionDTO notificacion = new NotificacionDTO(0, alertaDTO.message(), alertaDTO.deviceId());
        userWebClient.post().uri("/api/notificaciones/{id}", userId).bodyValue(notificacion)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public int saveAlert(AlertaDTO alerta, DispositivoDTO dispositivo, int[] userIds) {
        Alerta alertaGuardar = this.crearAlerta(dispositivo.id(), alerta.message(), alerta.nivel(), true,
                alerta.timestamp(), userIds);
        alertaRepository.save(alertaGuardar);
        return alertaGuardar.getId();
    }

    public void mandarAlerta(DispositivoDTO dispositivo, AlertaDTO alerta, int[] userIds) {
        try {
            AlertaDTO payload = new AlertaDTO(dispositivo.id(), alerta.message(), alerta.timestamp(), alerta.nivel(),
                    userIds, alerta.alertaId());

            String json = this.objectMapper.writeValueAsString(payload);
            mqttAlertGateway.sendToMqtt(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void handleAlert(AlertaDTO alerta) {
        DispositivoDTO dispositivo = this.getDevice(alerta);
        if (dispositivo == null) {
            // guardar alerta como no existente
            Alerta alertaGuardar = this.crearAlerta(0, alerta.message(), alerta.nivel(), false, alerta.timestamp());
            alertaRepository.save(alertaGuardar);
            return;
        }

        updateDevice(dispositivo, "alert");

        int[] userIds = userHandler.handle(alerta.nivel());
        // poner notificacion a todos los usuarios
        for (int i : userIds) {
            this.insertNotification(alerta, i);
        }

        
        int alertaId = saveAlert(alerta, dispositivo, userIds);
        alerta = new AlertaDTO(dispositivo.id(), alerta.message(), alerta.timestamp(), alerta.nivel(), userIds,
                alertaId);

        mandarAlerta(dispositivo, alerta, userIds);
    }

    public AlertaDTO buscarAlerta(int alertaId) {
        Alerta alerta = alertaRepository.findById(alertaId).orElseThrow();
        return new AlertaDTO(alerta.getId(), alerta.getMessage(), alerta.getTimestamp(), alerta.geNivel());
    }
}
