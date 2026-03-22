package com.ajaskiewicz.PlantManager.service;

import java.util.List;

import com.ajaskiewicz.PlantManager.model.User;

public interface UserService {

    List<User> findAll();

    void save(User user);

    User findByUsername(String username);

    User findById(Long id);

    Long findIdOfLoggedUser();

    void updateResetPasswordToken(String token, String email);

    User findByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
