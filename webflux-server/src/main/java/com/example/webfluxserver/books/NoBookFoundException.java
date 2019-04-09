package com.example.webfluxserver.books;

public class NoBookFoundException extends RuntimeException {

    public NoBookFoundException(String message) {
        super(message);
    }
}
