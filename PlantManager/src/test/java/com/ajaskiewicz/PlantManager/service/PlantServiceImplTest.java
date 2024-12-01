package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlantServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final Long PLANT_ID = 2L;
    private static final String USERNAME = "username";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int DEFAULT_INTERVAL = 3;

    private PlantService plantService;

    private PlantRepository plantRepository;
    private UserRepository userRepository;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        plantRepository = mock(PlantRepository.class);
        userRepository = mock(UserRepository.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        plantService = new PlantServiceImpl(plantRepository, userRepository);
    }

    @Test
    void shouldFindAllPlants() {
        //given
        var allPlants = preparePlants();
        when(plantRepository.findAll()).thenReturn(allPlants);

        //when
        var plants = plantService.findAll();

        //then
        assertEquals(allPlants, plants);
        verify(plantRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllPlantsByUserId() {
        //given
        var allPlants = preparePlants();
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(allPlants);

        //when
        var plants = plantService.findAllByUserId(USER_ID);

        //then
        assertEquals(allPlants, plants);
        verify(plantRepository, times(1)).findAllByUserId(USER_ID);
    }

    @Test
    void shouldFindPlantById() throws NotFoundException {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        when(plantRepository.findById(PLANT_ID)).thenReturn(Optional.of(plant));

        //when
        var foundPlant = plantService.find(PLANT_ID);

        //then
        assertEquals(plant, foundPlant);
        verify(plantRepository, times(1)).findById(PLANT_ID);
    }

    @Test
    void shouldThrowExceptionWhenPlantNotFound() {
        //given
        when(plantRepository.findById(PLANT_ID)).thenReturn(Optional.empty());

        //when && then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> plantService.find(PLANT_ID));

        assertEquals("Plant with ID " + PLANT_ID + " not found", exception.getMessage());
        verify(plantRepository, times(1)).findById(PLANT_ID);
    }

    @Test
    void shouldSavePlant() {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        when(plantRepository.save(plant)).thenReturn(plant);

        //when
        var savedPlant = plantService.save(plant);

        //then
        assertEquals(plant, savedPlant);
        verify(plantRepository, times(1)).save(plant);
    }

    @Test
    void shouldCreateOrUpdatePlant() {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        var user = new User();
        user.setUsername(USERNAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(plantRepository.save(plant)).thenReturn(plant);

        //when
        var savedPlant = plantService.createOrUpdatePlant(plant);

        //then
        assertEquals(plant, savedPlant);
        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(plantRepository, times(1)).save(plant);
    }

    @Test
    void shouldDeletePlant() throws NotFoundException {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        when(plantRepository.findById(PLANT_ID)).thenReturn(Optional.of(plant));

        //when
        plantService.delete(PLANT_ID);

        //then
        verify(plantRepository, times(1)).findById(PLANT_ID);
        verify(plantRepository, times(1)).deleteById(PLANT_ID);
    }

    @Test
    void shouldThrowExceptionWhenDeletedPlantNotFound() {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        when(plantRepository.findById(PLANT_ID)).thenReturn(Optional.empty());

        //when && then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> plantService.delete(PLANT_ID));
        assertEquals(String.format("Plant for ID %d not found", PLANT_ID), exception.getMessage());
        verify(plantRepository, times(1)).findById(PLANT_ID);
        verify(plantRepository, never()).deleteById(PLANT_ID);
    }

    @Test
    public void shouldCalculateCorrectNegativeDifferenceInDays() {
        //given
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(1L, -2);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantToBeWateredTwoDaysAgo));

        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);

        //then
        assertEquals(1, result.size());
        assertEquals(-2, result.getFirst().getWateringDifferenceInDays());
    }

    @Test
    public void shouldCalculateCorrectPositiveDifferenceInDays() {
        //given
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1L, 1);
        when(plantRepository.findAllByUserId(USER_ID)).thenReturn(newArrayList(plantToBeWateredTomorrow));

        //when
        var result = plantService.findPlantsToBeWateredSoon(USER_ID);

        //then
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getWateringDifferenceInDays());
    }

    @Test
    public void shouldSortPlantFromTheOldestWateredToTheMostRecentlyWatered() {
        //given
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1L, 1);
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(2L, -2);
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
        var plantToBeWateredTomorrow = preparePlantThatShouldBeWatered(1L, 1);
        var plantToBeWateredTwoDaysAgo = preparePlantThatShouldBeWatered(2L, -2);
        var plantThatShouldNotBeWatered = new Plant();
        plantThatShouldNotBeWatered.setId(3L);
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

    @Test
    void shouldUpdateLastWateredDate() throws NotFoundException {
        //given
        var plant = new Plant();
        plant.setId(PLANT_ID);
        var date = Calendar.getInstance().getTime();
        var today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        var wateringSchedule = new WateringSchedule();
        plant.setWateringSchedule(wateringSchedule);
        plant.getWateringSchedule().setLastWateredDate(today);
        var user = new User();
        user.setUsername(USERNAME);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(plantRepository.save(plant)).thenReturn(plant);

        //when
        var savedPlant = plantService.updateLastWateredDate(plant);

        //then
        assertEquals(savedPlant, plant);
        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(plantRepository, times(1)).save(plant);
    }

    private Plant preparePlantThatShouldBeWatered(Long id, int wateringDifferenceInDays) {
        var plantThatShouldBeWatered = new Plant();
        plantThatShouldBeWatered.setId(id);
        var wateringScheduleThatShouldBeWatered = new WateringSchedule();
        wateringScheduleThatShouldBeWatered.setLastWateredDate(LocalDate.now().minusDays(-1 * wateringDifferenceInDays + DEFAULT_INTERVAL).format(FORMATTER));
        wateringScheduleThatShouldBeWatered.setWateringInterval(3);
        plantThatShouldBeWatered.setWateringSchedule(wateringScheduleThatShouldBeWatered);

        return plantThatShouldBeWatered;
    }

    private List<Plant> preparePlants() {
        var plant1 = new Plant();
        plant1.setId(1L);

        var plant2 = new Plant();
        plant2.setId(2L);

        var plant3 = new Plant();
        plant3.setId(3L);

        return List.of(plant1, plant2, plant3);
    }
}
