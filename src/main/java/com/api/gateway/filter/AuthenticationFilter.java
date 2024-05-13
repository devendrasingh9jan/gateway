package com.api.gateway.filter;

import com.api.gateway.exception.*;
import com.api.gateway.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new TokenMissingException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (ExpiredJwtException ex) {
                    System.out.println("Token expired...!");
                    throw new TokenExpiredException("Token has expired");
                } catch (MalformedJwtException ex) {
                    System.out.println("Malformed token...!");
                    throw new MalformedTokenException("Malformed token");
                } catch (SignatureException ex) {
                    System.out.println("Invalid signature...!");
                    throw new InvalidSignatureException("Invalid token signature");
                } catch (UnsupportedJwtException ex) {
                    System.out.println("Unsupported token...!");
                    throw new UnsupportedTokenException("Unsupported token");
                } catch (IllegalArgumentException ex) {
                    System.out.println("Illegal argument...!");
                    throw new IllegalArgumentException("Invalid token");
                } catch (Exception ex) {
                    System.out.println("Unexpected error...!");
                    throw new RuntimeException("Unexpected error occurred", ex);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}