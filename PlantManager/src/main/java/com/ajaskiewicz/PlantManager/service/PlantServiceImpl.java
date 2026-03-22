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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service("plantService")
@Slf4j
public class PlantServiceImpl implements PlantService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final int SAFE_AMOUNT_OF_DAYS_IN_THE_FUTURE = 3;

    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlantServiceImpl(PlantRepository plantRepository, UserRepository userRepository) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Plant> findAll() {
        return plantRepository.findAll();
    }

    @Override
    public List<Plant> findAllByUserId(Long id) {
        log.info("Looking for all plants that belong to user with ID " + id);
        List<Plant> result = plantRepository.findAllByUserId(id);
        log.info(result.size() + " plants found");
        return result;
    }

    public Plant find(Long id) throws NotFoundException {
        Optional<Plant> plant = plantRepository.findById(id);
        return plant.orElseThrow(() -> new NotFoundException("Plant with ID " + id + " not found"));
    }

    @Override
    public Plant save(Plant plant) {
        log.info("Saving new plant");
        return plantRepository.save(plant);
    }

    public Plant createOrUpdatePlant(Plant plant) {
        log.info("Create/update request received for: " + plant.toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> queriedUser = userRepository.findByUsername(username);
        queriedUser.ifPresent(plant::setUser);

        plantRepository.save(plant);
        return plant;
    }

    @Transactional
    public void delete(Long id) throws NotFoundException {
        Optional<Plant> optionalPlant = plantRepository.findById(id);
        optionalPlant.orElseThrow(() -> new NotFoundException(String.format("Plant for ID %d not found", id)));

        log.info("Deleting plant with ID " + id);
        plantRepository.deleteById(id);
    }


    @Override
    public List<Plant> findPlantsToBeWateredSoon(Integer userId) {
        var allPlants = plantRepository.findAllByUserId(userId);
        var plantsToBeWatered = new ArrayList<Plant>();

        log.info("Looking for plants that should be watered soon for userId: {}", userId);
        for (Plant plant : allPlants) {
            var lastTimeWateredInDays = findWateringDifferenceInDays(plant.getWateringSchedule().getLastWateredDate(), plant.getWateringSchedule().getWateringInterval());

            if (lastTimeWateredInDays <= SAFE_AMOUNT_OF_DAYS_IN_THE_FUTURE) { //todo być może to mogła by być wartość personalizowana użytkownika - "chcę aby informować mnie o podlaniu nadchodzącym w przeciągu X dni" (?)
                plant.setWateringDifferenceInDays(lastTimeWateredInDays);
                plantsToBeWatered.add(plant);
            }
        }

        log.info(plantsToBeWatered.size() + " plants found");
        plantsToBeWatered.sort(Comparator.comparing(Plant::getWateringDifferenceInDays));

        return plantsToBeWatered;
    }

    private Integer findWateringDifferenceInDays(String lastWateredDate, Integer wateringInterval) {
        var today = LocalDate.now();
        var lwd = LocalDate.parse(lastWateredDate);

        log.info("Today: " + today);
        log.info("Last watered date: " + lastWateredDate);
        log.info("Watering interval: " + wateringInterval);

        log.info("Counting days that remain to the closest watering");
        var differenceInDays = (int) ChronoUnit.DAYS.between(today, lwd.plusDays(wateringInterval));

        log.info("Difference is: " + differenceInDays + " days");
        return differenceInDays;
    }

    @Override
    public Plant updateLastWateredDate(Plant plant) {
        Date date = Calendar.getInstance().getTime();
        String today = DATE_FORMAT.format(date);

        log.info("Setting last watered date to today");
        plant.getWateringSchedule().setLastWateredDate(today);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> queriedUser = userRepository.findByUsername(username);
        queriedUser.ifPresent(plant::setUser);

        plantRepository.save(plant);

        return plant;
    }
}
