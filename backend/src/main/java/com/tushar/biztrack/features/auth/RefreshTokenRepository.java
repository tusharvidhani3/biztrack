package com.tushar.biztrack.features.auth;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    public RefreshToken findByToken(String token);

    public void deleteAllByExpiresAtBefore(Instant now);

    @Query("SELECT r FROM RefreshToken r JOIN FETCH r.user u JOIN FETCH u.role WHERE r.token = :token")
    public RefreshToken findByTokenWithUserAndRole(@Param("token") String token);

}