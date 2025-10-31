package com.fuap.alertas.data.DTO;

import java.util.Map;

public record DispositivoDTO(Integer id, String name, String description, String state, String type, Map<String, Object> schemaJson) {}
