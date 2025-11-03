package com.fuap.alertas.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fuap.alertas.data.DTO.ResolutionDTO;
import com.fuap.alertas.services.ResolutionService;


@RestController
@RequestMapping("/api/resolucion")
public class ResolutionController {
    private final ResolutionService resolutionService;

    public ResolutionController(ResolutionService resService, WebClient.Builder webClientBuilder) {
        this.resolutionService = resService;
    }

    @PostMapping
    public void Resolver(@RequestBody ResolutionDTO resolutionDTO) {
        this.resolutionService.handleResolution(resolutionDTO);
    }

    @Value("${service.api.key}")
    private String serviceApiKey;

}
