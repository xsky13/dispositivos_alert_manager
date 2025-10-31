package com.fuap.alertas.handlers;

import org.springframework.stereotype.Component;

@Component
public class LowHandler extends Handler {
    @Override
    public int[] handle(String userType) {
        // retornar usuarios de bajo nivel
        if ("low".equals(userType)) {
            return new int[] { 1, 2, 3 };
        }
        return callNext(userType);
    }
}
