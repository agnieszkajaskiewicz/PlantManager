package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;

public interface PlantService {

    public Iterable<Plant> findAll();

    public Plant find(int id);

    public Plant save(Plant plant);

}
