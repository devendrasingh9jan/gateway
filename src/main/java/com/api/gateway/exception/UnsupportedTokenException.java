package com.api.gateway.exception;

public class UnsupportedTokenException extends RuntimeException {
    public UnsupportedTokenException(String message) {
        super(message);
    }
}
