package com.fuap.alertas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuap.alertas.data.DTO.AlertaDTO;
import com.fuap.alertas.services.AlertaService;

@Configuration
public class SubHandler {
    private final AlertaService alertaService;

    public SubHandler(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883",
                "alerts", "alerts-raw");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(AlertaService alertaService) {
        return message -> {
            try {
                String payload = (String) message.getPayload();

                ObjectMapper mapper = new ObjectMapper();
                AlertaDTO alerta = mapper.readValue(payload, AlertaDTO.class);

                this.alertaService.handleAlert(alerta);

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
