package com.codesoom.assignment.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtDecoder {

    private final Key key;

    public JwtDecoder(@Value("#{environment['jwt.secret']}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 토큰을 해석하여 클레임을 리턴.
     *
     * @param token 해석할 토큰
     * @return 클레임
     * @throws SignatureException 토큰이 유효하지 않은 경우
     */
    public Claims decode(String token) throws SignatureException {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
