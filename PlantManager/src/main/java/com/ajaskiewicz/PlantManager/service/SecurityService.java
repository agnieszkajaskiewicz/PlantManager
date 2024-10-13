package com.ajaskiewicz.PlantManager.service;

public interface SecurityService {

    boolean isAuthenticated();
    String login(String username, String password);

}
