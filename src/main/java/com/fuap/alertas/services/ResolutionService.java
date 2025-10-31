package com.fuap.alertas.services;

import org.springframework.stereotype.Service;

import com.fuap.alertas.data.DTO.ResolutionDTO;
import com.fuap.alertas.data.models.Resolution;
import com.fuap.alertas.data.repository.ResolutionRepository;

@Service
public class ResolutionService {
    private final AlertaService alertaService;
    private final ResolutionRepository resolutionRepository;

    public ResolutionService(AlertaService aService, ResolutionRepository resolutionRepository) {
        this.alertaService = aService;
        this.resolutionRepository = resolutionRepository;
    }

    public void handleResolution(ResolutionDTO resolutionDTO) {
        var alerta = this.alertaService.buscarAlerta(resolutionDTO.alertaId());
        var dispositivo = this.alertaService.getDevice(alerta);
        
        this.saveResolution(resolutionDTO);
        this.alertaService.updateDevice(dispositivo, "normal");
    }

    private void saveResolution(ResolutionDTO resolutionDTO) {
        Resolution resolution = new Resolution();
        resolution.setUserId(resolutionDTO.userId());
        resolution.setDispositivoId(resolutionDTO.deviceId());
        resolution.setAlertaId(resolutionDTO.alertaId());
        resolution.setMessage(resolutionDTO.message());
        this.resolutionRepository.save(resolution);
    }
}
