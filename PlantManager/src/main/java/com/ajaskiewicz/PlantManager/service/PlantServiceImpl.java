package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("plantService")
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private WateringScheduleRepository wateringScheduleRepository;

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
    public void delete(int id) {
        plantRepository.findById(id);
        plantRepository.deleteById(id);
    }

    @Override
    public List<Plant> findPlantsToBeWateredSoon() {
        List<Plant> allPlants = plantRepository.findAll();
        List<Plant> plantsToBeWatered = new ArrayList<>();

        for (int index = 0; index < allPlants.size(); index++) {
            if (findDifferenceInDays(allPlants.get(index).getWateringSchedule().getLast_watered_date(), allPlants.get(index).getWateringSchedule().getWatering_interval() ) <= 3
            && findDifferenceInDays(allPlants.get(index).getWateringSchedule().getLast_watered_date(), allPlants.get(index).getWateringSchedule().getWatering_interval() ) > 0) {
                plantsToBeWatered.add(allPlants.get(index));
            }
        }

        return plantsToBeWatered;
    }

    public static long findDifferenceInDays(String lastWateredDate, Integer wateringInterval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = Calendar.getInstance().getTime();
        String today = sdf.format(date);

        long differenceInDays;
        long differenceInTime;

        try {
            Date lwd = sdf.parse(lastWateredDate);
            System.out.println("Last watered date: " + lastWateredDate);
            System.out.println("Watering interval: " + wateringInterval);

            Date t = sdf.parse(today);
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
            differenceInDays.add(Math.toIntExact(findDifference(plantsToBeWateredSoon.get(index).getWateringSchedule().getLast_watered_date(), today)));
        }

        System.out.println(differenceInDays);

        return differenceInDays;
    }
     */

    @Override
    public Plant updateLastWateredDate(Plant plant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = Calendar.getInstance().getTime();
        String today = dateFormat.format(date);

        plant.getWateringSchedule().setLast_watered_date(today);
        plantRepository.save(plant);

        return plant;
    }


}
