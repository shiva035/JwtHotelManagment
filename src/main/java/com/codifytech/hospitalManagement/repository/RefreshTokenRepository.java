package com.codifytech.hospitalManagement.repository;

import com.codifytech.hospitalManagement.entity.RefreshToken;
import com.codifytech.hospitalManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
