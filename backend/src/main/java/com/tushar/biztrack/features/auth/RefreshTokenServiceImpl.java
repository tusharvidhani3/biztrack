package com.tushar.biztrack.features.auth;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.tushar.biztrack.features.AppUser.AppUser;

import jakarta.transaction.Transactional;

public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepo;

    @Autowired
    private JwtService jwtService;

    @Value("${auth.refresh-token.expiration}")
    private long expiration;

    @Override
    public String generateRefreshToken(AppUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(Duration.ofSeconds(expiration)));
        refreshToken.setToken(generateTokenString());
        refreshToken = refreshTokenRepo.save(refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public String generateAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByTokenWithUserAndRole(token);
        Set<String> roles = refreshToken.getUser().getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
        String accessToken = jwtService.generateToken(refreshToken.getUser().getId(), roles);
        return accessToken;
    }

    @Override
    @Transactional
    public void invalidateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);
        if(!refreshToken.isRevoked())
            refreshToken.setRevoked(true);
    }

    @Override
    public boolean isValid(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);
        Instant currentTime = Instant.now();
        if(refreshToken.isRevoked() || currentTime.isAfter(refreshToken.getExpiresAt()))
        return false;
        return true;
    }

    @Override
    @Transactional
    public String rotateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token);
        refreshToken.setRevoked(true);
        return generateRefreshToken(refreshToken.getUser());
    }

    private String generateTokenString() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Scheduled(cron = "0 0 3 * * ?") // Every day at 3 AM
    public void purgeExpiredTokens() {
        refreshTokenRepo.deleteAllByExpiresAtBefore(Instant.now());
    }
}
