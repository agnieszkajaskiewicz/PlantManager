package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import javassist.NotFoundException;

import java.util.List;

public interface PlantService {

        List<Plant> findAll();

        Plant find(Integer id) throws NotFoundException;

        List<Plant> findAllByUserId(Integer id);

        Plant save(Plant plant);

        Plant createOrUpdatePlant(Plant plant);

        void delete(Integer id) throws NotFoundException;

        List<Plant> findPlantsToBeWateredSoon(Integer id);

        Plant updateLastWateredDate(Plant plant) throws NotFoundException;
}
