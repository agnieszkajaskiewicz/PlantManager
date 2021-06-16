package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service("plantService")
@Slf4j
public class PlantServiceImpl implements PlantService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private PlantRepository plantRepository;
    private WateringScheduleRepository wateringScheduleRepository;
    private UserRepository userRepository;

    @Autowired
    public PlantServiceImpl(PlantRepository plantRepository, WateringScheduleRepository wateringScheduleRepository, UserRepository userRepository) {
        this.plantRepository = plantRepository;
        this.wateringScheduleRepository = wateringScheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Plant> findAll() {
        var result = plantRepository.findAll();
        return result;
    }

    @Override
    public List<Plant> findAllByUserId(Integer id) {
        log.info("Looking for all plants that belong to user with ID " + id);
        var result = plantRepository.findAllByUserId(id);
        log.info(result.size() + " plants found");
        return result;
    }

    @Override
    public Plant find(Integer id) throws NotFoundException {
        var plant = plantRepository.findById(id);
        if (plant.isPresent()) {
            return plant.get();
        } else {
            throw new NotFoundException("No plant record exist for given ID " + id);
        }
    }

    @Override
    public Plant save(Plant plant) {
        log.info("Saving new plant");
        return plantRepository.save(plant);
    }

    public Plant createOrUpdatePlant(Plant plant) {
        log.info("Create/update request received for: " + plant.toString());

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var queriedUser = userRepository.findByUsername(username);
        plant.setUser(queriedUser);

        plantRepository.save(plant);
        return plant;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        var optionalPlant = plantRepository.findById(id);
        optionalPlant.orElseThrow(() -> new NotFoundException(String.format("Plant for id %d not found", id)));

        log.info("Deleting plant with ID " + id);
        plantRepository.deleteById(id);
    }

    @Override
    public List<Plant> findPlantsToBeWateredSoon(Integer id) {
        var allPlants = plantRepository.findAllByUserId(id);
        var plantsToBeWatered = new ArrayList<Plant>();

        log.info("Looking for plants that should be watered soon");
        for (var i = 0; i < allPlants.size(); i++) {
            var differenceInDays = findDifferenceInDays(allPlants.get(i).getWateringSchedule().getLastWateredDate(), allPlants.get(i).getWateringSchedule().getWateringInterval());

            if (differenceInDays <= 3) {
                var plantToBeWatered = allPlants.get(i);
                plantToBeWatered.setWateringDifferenceInDays(differenceInDays);
                plantsToBeWatered.add(plantToBeWatered);
            }
        }

        log.info(plantsToBeWatered.size() + " plants found");
        log.info("Sorting plants that should be watered soon by watering difference in days");
        plantsToBeWatered.sort(Comparator.comparing(Plant::getWateringDifferenceInDays));

        return plantsToBeWatered;
    }

    public Integer findDifferenceInDays(String lastWateredDate, Integer wateringInterval) {
        var date = Calendar.getInstance().getTime();
        var today = DATE_FORMAT.format(date);

        Integer differenceInDays;
        Integer differenceInTime;

        log.info("Counting days that remain to closest watering");
        try {
            var lwd = DATE_FORMAT.parse(lastWateredDate);
            log.info("Last watered date: " + lastWateredDate);
            log.info("Watering interval: " + wateringInterval);

            var t = DATE_FORMAT.parse(today);
            log.info("Today: " + today);

            differenceInTime = Math.toIntExact(lwd.getTime() - t.getTime());

            differenceInDays = ((differenceInTime / (1000 * 60 * 60 * 24)) + wateringInterval) % 365;

            log.info("Difference is: " + differenceInDays + " days");
            return differenceInDays;

        } catch (ParseException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    @Override
    public Plant updateLastWateredDate(Plant plant) {
        var date = Calendar.getInstance().getTime();
        var today = DATE_FORMAT.format(date);

        log.info("Setting last watered date to today");
        plant.getWateringSchedule().setLastWateredDate(today);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var queriedUser = userRepository.findByUsername(username);
        plant.setUser(queriedUser);

        plantRepository.save(plant);

        return plant;
    }
}
