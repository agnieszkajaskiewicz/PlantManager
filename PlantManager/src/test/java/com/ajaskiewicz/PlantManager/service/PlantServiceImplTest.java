package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import org.h2.table.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlantServiceImplTest {

    private static final Integer USER_ID = 1;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private PlantService plantService;

    private PlantRepository plantRepository;
    private WateringScheduleRepository wateringScheduleRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        plantRepository = mock(PlantRepository.class);
        wateringScheduleRepository = mock(WateringScheduleRepository.class);
        userRepository = mock(UserRepository.class);

        plantService = new PlantServiceImpl(plantRepository, wateringScheduleRepository, userRepository);
    }

    @Test
    public void shouldCalculateCorrectDifferenceInDays() {
        //given
        var allUserPlants = preparePlants();
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(allUserPlants);
        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);
        //then
        assertEquals(2, result.size());
        assertEquals(-2, result.get(0).getWateringDifferenceInDays());
        assertEquals(1, result.get(1).getWateringDifferenceInDays());
    }

    private List<Plant> preparePlants() {
        var plantThatShouldBeWateredTwoDaysAgo = new Plant();
        plantThatShouldBeWateredTwoDaysAgo.setId(1);
        var wateringScheduleThatShouldBeWateredTwoDaysAgo = new WateringSchedule();
        wateringScheduleThatShouldBeWateredTwoDaysAgo.setLastWateredDate(LocalDate.now().minusDays(5).format(FORMATTER));
        wateringScheduleThatShouldBeWateredTwoDaysAgo.setWateringInterval(3);
        plantThatShouldBeWateredTwoDaysAgo.setWateringSchedule(wateringScheduleThatShouldBeWateredTwoDaysAgo);

        var plantThatShouldBeWateredTomorrow = new Plant();
        plantThatShouldBeWateredTomorrow.setId(2);
        var wateringScheduleThatShouldBeWateredTomorrow = new WateringSchedule();
        wateringScheduleThatShouldBeWateredTomorrow.setLastWateredDate(LocalDate.now().minusDays(2).format(FORMATTER));
        wateringScheduleThatShouldBeWateredTomorrow.setWateringInterval(3);
        plantThatShouldBeWateredTomorrow.setWateringSchedule(wateringScheduleThatShouldBeWateredTomorrow);

        var plantThatShouldNotBeWatered = new Plant();
        plantThatShouldNotBeWatered.setId(3);
        var wateringScheduleThatShouldNotBeWatered = new WateringSchedule();
        wateringScheduleThatShouldNotBeWatered.setLastWateredDate(LocalDate.now().format(FORMATTER));
        wateringScheduleThatShouldNotBeWatered.setWateringInterval(4);
        plantThatShouldNotBeWatered.setWateringSchedule(wateringScheduleThatShouldNotBeWatered);


        return newArrayList(plantThatShouldBeWateredTwoDaysAgo, plantThatShouldBeWateredTomorrow, plantThatShouldNotBeWatered);
    }
}