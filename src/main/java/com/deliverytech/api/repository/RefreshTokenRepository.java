package com.deliverytech.api.repository;

import com.deliverytech.api.model.RefreshToken;
import com.deliverytech.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
