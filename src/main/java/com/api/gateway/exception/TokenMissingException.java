package com.api.gateway.exception;

public class TokenMissingException extends RuntimeException{
    public TokenMissingException(String message) {
        super(message);
    }
}
