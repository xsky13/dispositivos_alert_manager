package com.fuap.alertas.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fuap.alertas.data.DTO.DispositivoDTO;
import com.fuap.alertas.data.DTO.ResolutionDTO;
import com.fuap.alertas.services.ResolutionService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/resolucion")
public class ResolutionController {
    private final ResolutionService resolutionService;
    private final WebClient webClient;

    public ResolutionController(ResolutionService resService, WebClient.Builder webClientBuilder) {
        this.resolutionService = resService;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").defaultHeader("X-Service-API-Key", "clave-muy-secreta").build();
    }

    @PostMapping
    public void Resolver(@RequestBody ResolutionDTO resolutionDTO) {
        this.resolutionService.handleResolution(resolutionDTO);
    }

    @Value("${service.api.key}")
    private String serviceApiKey;

    @GetMapping
    public List<Map<String, Object>> testUsers() {
        try {
            return this.webClient.get()
                    .uri("/api/dispositivos")
                    .retrieve()
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch users: " + e.getMessage(), e);
        }
    }

}
