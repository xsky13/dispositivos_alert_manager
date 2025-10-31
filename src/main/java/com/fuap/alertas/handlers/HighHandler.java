package com.fuap.alertas.handlers;

import org.springframework.stereotype.Component;

@Component
public class HighHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        // retornar usuarios de alto nivel
        if ("high".equals(userType)) {
            return new int[] { 7, 8, 9 };
        }
        return callNext(userType);
    }
}
