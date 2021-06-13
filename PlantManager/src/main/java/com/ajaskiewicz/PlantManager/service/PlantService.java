package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import javassist.NotFoundException;

import java.util.List;

public interface PlantService {

        Iterable<Plant> findAll();

        Plant find(int id) throws NotFoundException;

        Iterable<Plant> findAllByUserId(int id);

        Plant save(Plant plant);

        Plant createOrUpdatePlant(Plant plant);

        void delete(int id) throws NotFoundException;

        List<Plant> findPlantsToBeWateredSoon(Integer id);

        Plant updateLastWateredDate(Plant plant) throws NotFoundException;
}
