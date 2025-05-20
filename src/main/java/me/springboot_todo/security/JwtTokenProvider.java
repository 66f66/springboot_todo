package me.springboot_todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import me.springboot_todo.enums.ValidateTokenResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private static final Long JWT_ACCESS_EXPIRATION = 1000L * 60 * 30; // 30분
    private static final Long JWT_REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 30; // 30일

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(String username, long expiration) {

        return Jwts.builder()
                .signWith(secretKey)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsTFunction.apply(claims);
    }

    public String generateAccessToken(String username) {

        return this.generateToken(username, JWT_ACCESS_EXPIRATION);
    }

    public String generateRefreshToken(String username) {

        return this.generateToken(username, JWT_REFRESH_EXPIRATION);
    }

    public ValidateTokenResult validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return ValidateTokenResult.VALID;
        } catch (ExpiredJwtException e) {

            return ValidateTokenResult.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {

            return ValidateTokenResult.INVALID;
        }
    }

    public String extractSubjectFromToken(String token) {
        return extractClaims(token, Claims::getSubject);
    }
}
