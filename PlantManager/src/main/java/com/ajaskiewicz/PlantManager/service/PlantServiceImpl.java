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
        }
        else {
            throw new NotFoundException("No plant record exist for given ID.");
        }
    }

    @Override
    public Plant save(Plant plant) {
        return plantRepository.save(plant);
    }


    public Plant createOrUpdatePlant(Plant plant, Room room, WateringSchedule wateringSchedule) {
        if (plant.getId() == null) {
            plant = plantRepository.save(plant);
            return plant;
        }
        else {
            Optional<Plant> entity = plantRepository.findById(plant.getId());

            if (entity.isPresent()) {

                Plant newPlant = entity.get();

//                newPlant.setId(newPlant.getId());
//                newPlant.setPlantName(newPlant.getPlantName());
//                newPlant.setRoom(room);
//                newPlant.setWateringSchedule(wateringSchedule);

                newPlant.setId(newPlant.getId());
                newPlant.setPlantName(newPlant.getPlantName());
                newPlant.setRoom(newPlant.getRoom());
                newPlant.setWateringSchedule(newPlant.getWateringSchedule());

                System.out.println(newPlant.getId());
//                System.out.println(newPlant.getRoom());
                System.out.println(newPlant.getPlantName());
//                System.out.println(newPlant.getWateringSchedule());

                plantRepository.save(newPlant);

                return newPlant;
            } else {
                plant = plantRepository.save(plant);
                return plant;
            }
        }
    }

}
