package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlantServiceImplTest {

    private static final Integer USER_ID = 1;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int DEFAULT_INTERVAL = 3;

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
    public void shouldCalculateCorrectNegativeDifferenceInDays() {
        //given
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(1, -2);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantToBeWateredTwoDaysAgo));
        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);
        //then
        assertEquals(1, result.size());
        assertEquals(-2, result.get(0).getWateringDifferenceInDays());
    }

    @Test
    public void shouldCalculateCorrectPositiveDifferenceInDays() {
        //given
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1, 1);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantToBeWateredTomorrow));
        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);
        //then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getWateringDifferenceInDays());
    }

    @Test
    public void shouldSortPlantFromTheOldestWateredToTheMostRecentlyWatered() {
        //given
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1, 1);
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(2, -2);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantToBeWateredTomorrow, plantToBeWateredTwoDaysAgo));
        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);
        //then
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
        assertEquals(1, result.get(1).getId());
    }

    @Test
    public void shouldFilterOutPlantThatShouldBeWateredInMoreThanThreeDaysInTheFuture() {
        //given
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1, 1);
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(2, -2);
        var plantThatShouldNotBeWatered = new Plant();
        plantThatShouldNotBeWatered.setId(3);
        var wateringScheduleThatShouldNotBeWatered = new WateringSchedule();
        wateringScheduleThatShouldNotBeWatered.setLastWateredDate(LocalDate.now().format(FORMATTER));
        wateringScheduleThatShouldNotBeWatered.setWateringInterval(4);
        plantThatShouldNotBeWatered.setWateringSchedule(wateringScheduleThatShouldNotBeWatered);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantThatShouldNotBeWatered, plantToBeWateredTomorrow, plantToBeWateredTwoDaysAgo));
        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);
        //then
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getId());
        assertEquals(1, result.get(1).getId());
    }

    private Plant preparePlantThatShouldBeWatered(int id, int wateringDifferenceInDays) {
        var plantThatShouldBeWatered = new Plant();
        plantThatShouldBeWatered.setId(id);
        var wateringScheduleThatShouldBeWatered = new WateringSchedule();
        wateringScheduleThatShouldBeWatered.setLastWateredDate(LocalDate.now().minusDays(-1 * wateringDifferenceInDays + DEFAULT_INTERVAL).format(FORMATTER));
        wateringScheduleThatShouldBeWatered.setWateringInterval(3);
        plantThatShouldBeWatered.setWateringSchedule(wateringScheduleThatShouldBeWatered);

        return plantThatShouldBeWatered;
    }
}