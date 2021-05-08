package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
