package com.fuap.alertas.handlers;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.fuap.alertas.data.DTO.UserDTO;

@Component
public class LowHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        if ("low".equals(userType)) {
            // buscar usuarios tecnicos
            UserDTO[] users = this.getWebClient().get()
                .uri("/api/usuarios?rol=TECNICO")
                .retrieve()
                .bodyToMono(UserDTO[].class)
                .block();

            if (users == null || users.length == 0) return new int[0];
            
            int[] ids = Arrays.stream(users)
                       .mapToInt(m -> m.getId())
                       .toArray();
            return ids;
        }
        return callNext(userType);
    }
}
