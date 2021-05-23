package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DefaultMessageCodesResolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public List<Plant> findPlantsToBeWateredSoon() {
        List<Plant> allPlants = (List<Plant>) plantRepository.findAll();
        List<Plant> plantsToBeWatered = new ArrayList<>();



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = Calendar.getInstance().getTime();
        String today = dateFormat.format(date);

        for (int index = 0; index < allPlants.size(); index++) {
            if (findDifference(allPlants.get(index).getWateringSchedule().getLast_watered_date(), today) <=3) {
                plantsToBeWatered.add(allPlants.get(index));
            }
        }

//        allPlants.stream()
//                .filter(p -> findDifference(p.getWateringSchedule().getLast_watered_date(), today) <= 3)
//                .forEach(System.out::println)
//                .forEach(plantsToBeWatered.add());

        return plantsToBeWatered;
    }

    public static long findDifference(String lastWateredDate, String today) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long differenceInDays;
        long difference_In_Time;

        try {

            Date d1 = sdf.parse(lastWateredDate);
            System.out.println(lastWateredDate);
            Date d2 = sdf.parse(today);
            System.out.println(today);

            difference_In_Time
                    = d2.getTime() - d1.getTime();

            differenceInDays
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            System.out.print(
                    "Difference between two dates is: ");
            System.out.println(differenceInDays
                    + " days");
            return differenceInDays;

        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return 1235;

    }


}
