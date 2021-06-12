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
        List<Plant> result = (List<Plant>) plantRepository.findAll();
        return result;
    }

    @Override
    public List<Plant> findAllByUserId(int id) {
        List<Plant> result = plantRepository.findAllByUserId(id);
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
        log.info("Create request received for: " + plant.toString());

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var queriedUser = userRepository.findByUsername(username);
        plant.setUser(queriedUser);


        plantRepository.save(plant);
        return plant;
    }

    @Override
    public void delete(int id) throws NotFoundException {
        /*var optionalPlant = plantRepository.findById(id);
        optionalPlant.orElseThrow(() -> new NotFoundException(String.format("Plant for id %d not found", id)));*/

        var doesExist = plantRepository.existsById(id);

        if(!doesExist) {
            throw new NotFoundException(String.format("Plant for id %d not found", id));
        }

        plantRepository.deleteById(id);
    }

    @Override
    public List<Plant> findPlantsToBeWateredSoon() {
        var allPlants = plantRepository.findAll();
        var plantsToBeWatered = new ArrayList<Plant>();

        for (var i = 0; i < allPlants.size(); i++) {
            var differenceInDays = findDifferenceInDays(allPlants.get(i).getWateringSchedule().getLastWateredDate(), allPlants.get(i).getWateringSchedule().getWateringInterval());
            if (differenceInDays <= 3 && differenceInDays > 0) {
                var plantToBeWatered = allPlants.get(i);
                plantToBeWatered.setWateringDifferenceInDays(differenceInDays);
                plantsToBeWatered.add(plantToBeWatered);
            }
        }

        return plantsToBeWatered;
    }

    public static long findDifferenceInDays(String lastWateredDate, Integer wateringInterval) {
        Date date = Calendar.getInstance().getTime(); //todo change to LocalDate(in WateringSchedule)
        String today = DATE_FORMAT.format(date);

        long differenceInDays;
        long differenceInTime;

        try {
            Date lwd = DATE_FORMAT.parse(lastWateredDate);
            System.out.println("Last watered date: " + lastWateredDate);
            System.out.println("Watering interval: " + wateringInterval);

            Date t = DATE_FORMAT.parse(today);
            System.out.println("Today: " + today);

            differenceInTime = lwd.getTime() - t.getTime();

            differenceInDays = ((differenceInTime / (1000 * 60 * 60 * 24)) + wateringInterval) % 365;

            System.out.println("Difference between two dates is: " + differenceInDays + " days");
            return differenceInDays;

        } catch (ParseException ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    /*
    public List<Integer> differenceInDays() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = Calendar.getInstance().getTime();
        String today = dateFormat.format(date);

        List<Plant> plantsToBeWateredSoon = findPlantsToBeWateredSoon();
        List<Integer> differenceInDays = new ArrayList<>();

        for (int index = 0; index < plantsToBeWateredSoon.size(); index++) {
            differenceInDays.add(Math.toIntExact(findDifference(plantsToBeWateredSoon.get(index).getWateringSchedule().getlastWateredDate(), today)));
        }

        System.out.println(differenceInDays);

        return differenceInDays;
    }
     */

    @Override
    public Plant updateLastWateredDate(Plant plant) {
        var date = Calendar.getInstance().getTime();
        var today = DATE_FORMAT.format(date);

        plant.getWateringSchedule().setLastWateredDate(today);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        var queriedUser = userRepository.findByUsername(username);
        plant.setUser(queriedUser);

        plantRepository.save(plant);

        return plant;
    }


}
