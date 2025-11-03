package com.fuap.alertas.handlers;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.fuap.alertas.data.DTO.UserDTO;

@Component
public class HighHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        // retornar usuarios de alto nivel
        if ("high".equals(userType)) {
            UserDTO[] users = this.getWebClient().get()
                .uri("/api/usuarios?rol=ADMIN")
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
