package com.fuap.alertas.handlers;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.fuap.alertas.data.DTO.UserDTO;

@Component
public class MediumHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        // retornar usuarios de nivel medio
        if ("medium".equals(userType)) {
            UserDTO[] users = this.getWebClient().get()
                .uri("/api/usuarios?rol=ESPECIALIZADO")
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
