package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    void save(User user);

    User findByUsername(String username);

    User findById(Integer id);

    Integer findIdOfLoggedUser();

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    User findByEmail(String email);
}
