package com.example.webfluxserver.exceptions;

public class LiveEventNotFoundException extends RuntimeException {
    public LiveEventNotFoundException(String message) {
        super(message);
    }
}
