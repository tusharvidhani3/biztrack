package com.tushar.biztrack.features.auth;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    @Value("${auth.jwt.secret-key}")
    private String secretKey;

    @Value("${auth.jwt.expiration}")
    private String expiration;

    public String generateToken(Long userId, Set<String> roles) {
        return Jwts.builder()
                   .subject(userId.toString())
                   .claim("roles", roles)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis()))
                   .signWith(getSignInKey())
                   .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getSignInKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public boolean isTokenValid(String token, String userId) {
        String tokenUserId = extractUserId(token);
        return userId.equals(tokenUserId); //expiration is checked automatically during parsing signed claims
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractJwtFromCookies(HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("access_token"))
                    return cookie.getValue();
            }
        }
        return null;
    }
}
