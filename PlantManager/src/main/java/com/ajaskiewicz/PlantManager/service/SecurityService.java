package com.ajaskiewicz.PlantManager.service;

public interface SecurityService {

    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
