package com.api.gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final String SECRET_KET = "UTXC1yKlHH1OKvEnD+FobwzjXllZqn/zpXwxWrBig5GsDyYZx+NQYdkHRdVorRkl";

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}