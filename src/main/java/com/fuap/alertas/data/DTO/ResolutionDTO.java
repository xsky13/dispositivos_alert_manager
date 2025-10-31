package com.fuap.alertas.data.DTO;

public record ResolutionDTO(
    int userId,
    int deviceId,
    int alertaId,
    String message
) {
    public ResolutionDTO(int userId, int deviceId, int alertaId) {
        this(userId, deviceId, alertaId, null);
    }
}