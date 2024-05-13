package com.api.gateway.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
