package com.fuap.alertas.handlers;

public abstract class Handler {
    private Handler next;

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
