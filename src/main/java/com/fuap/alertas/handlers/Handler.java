package com.fuap.alertas.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class Handler {
    private Handler next;
    
    @Autowired
    protected WebClient.Builder webClientBuilder;

    @Value("${service.api.key}")
    private String serviceApiKey;

    protected WebClient getWebClient() {
        return webClientBuilder.baseUrl("http://localhost:8086").defaultHeader("X-Service-API-Key", serviceApiKey).build();
    }

    public Handler setNext(Handler nextHandler) {
        this.next = nextHandler;
        return nextHandler;
    }

    public int[] callNext(String userType) {
        if (this.next != null) {
            return this.next.handle(userType);
        }
        return null;
    }

    public abstract int[] handle(String userType);
}
