package me.springboot_todo.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.springboot_todo.constants.ValidateTokenResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.access-expiration:3000}")
    private Long JWT_ACCESS_EXPIRATION;

    @Value("${jwt.refresh-expiration:90000}")
    private Long JWT_REFRESH_EXPIRATION;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(String username, long expiration) {

        return Jwts.builder()
                .signWith(getSigningKey())
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public String generateAccessToken(String username) {

        return this.generateToken(username, JWT_ACCESS_EXPIRATION);
    }

    public String generateRefreshToken(String username) {

        return this.generateToken(username, JWT_REFRESH_EXPIRATION);
    }

    public String extractSubjectFromToken(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public ValidateTokenResult validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return ValidateTokenResult.VALID;
        } catch (ExpiredJwtException e) {

            return ValidateTokenResult.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {

            return ValidateTokenResult.INVALID;
        }
    }
}
