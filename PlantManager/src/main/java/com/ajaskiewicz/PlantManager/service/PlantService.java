package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import javassist.NotFoundException;

public interface PlantService {

        public Iterable<Plant> findAll();

        public Plant find(int id) throws NotFoundException;

        public Plant save(Plant plant);

        public Plant createOrUpdatePlant(Plant plant, Room room, WateringSchedule wateringSchedule);

}
