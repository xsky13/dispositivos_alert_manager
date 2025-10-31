package com.fuap.alertas.handlers;

import org.springframework.stereotype.Component;

@Component
public class MediumHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        // retornar usuarios de nivel medio
        if ("medium".equals(userType)) {
            return new int[] { 4, 5, 6 };
        }
        return callNext(userType);
    }
}
