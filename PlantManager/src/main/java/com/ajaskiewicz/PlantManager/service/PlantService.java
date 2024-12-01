package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import javassist.NotFoundException;

import java.util.List;

public interface PlantService {

        List<Plant> findAll();

        Plant find(Long id) throws NotFoundException;

        List<Plant> findAllByUserId(Long userId);

        Plant save(Plant plant);

        Plant createOrUpdatePlant(Plant plant);

        void delete(Long id) throws NotFoundException;

        List<Plant> findPlantsToBeWateredSoon(Long userId);

        Plant updateLastWateredDate(Plant plant) throws NotFoundException;
}
