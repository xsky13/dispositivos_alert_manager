package com.fuap.alertas.data.DTO;

public record AlertaDTO(
        int deviceId,
        String message,
        String timestamp,
        String nivel,
        int[] usuarios) {
    public AlertaDTO(int deviceId, String message, String timestamp, String nivel) {
        this(deviceId, message, timestamp, nivel, null);
    }
}
