package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //TODO: Is it necessary to return optionals or throw not found errors? What about @NonNullApi?
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);
}
