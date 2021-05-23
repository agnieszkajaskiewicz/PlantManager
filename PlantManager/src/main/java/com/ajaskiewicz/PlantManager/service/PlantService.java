package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import javassist.NotFoundException;

public interface PlantService {

        public Iterable<Plant> findAll();

        public Plant find(int id) throws NotFoundException;

        public Plant save(Plant plant);

        public Plant createOrUpdatePlant(Plant plant);

        public void delete(int id) throws NotFoundException;

}
