package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("plantService")
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;

    RoomService roomService;
    WateringScheduleService wateringScheduleService;

    @Override
    public List<Plant> findAll() {
        List<Plant> result = (List<Plant>) plantRepository.findAll();
        return result;
    }

    @Override
    public Plant find(int id) throws NotFoundException {
        Optional<Plant> plant = plantRepository.findById(id);
        if (plant.isPresent()) {
            return plant.get();
        } else {
            throw new NotFoundException("No plant record exist for given ID.");
        }
    }

    @Override
    public Plant save(Plant plant) {
        return plantRepository.save(plant);
    }

    public Plant createOrUpdatePlant(Plant plant) {
        System.out.println(plant);

        if (plant.getId() == null) {
            plant = plantRepository.save(plant);
            return plant;
        } else {
            plantRepository.save(plant);
            return plant;
        }
    }

    @Override
    public void delete(int id) throws NotFoundException {
        Optional<Plant> plant = plantRepository.findById(id);
        plantRepository.deleteById(id);
    }


}
