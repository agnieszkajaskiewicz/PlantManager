package com.ajaskiewicz.PlantManager.web.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ajaskiewicz.PlantManager.exceptions.ControllerErrorHandler;
import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.model.mapper.PlantMapper;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.UserService;

import javassist.NotFoundException;

class WateringControllerTest {

    private MockMvc mockMvc;

    private PlantService plantService;
    private UserService userService;
    private PlantMapper plantMapper;

    private WateringController wateringController;

    @BeforeEach
    void setUp() {
        plantService = mock(PlantService.class);
        userService = mock(UserService.class);
        plantMapper = mock(PlantMapper.class);
        openMocks(this);
        wateringController = new WateringController(plantService, userService, null, plantMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(wateringController)
                .setControllerAdvice(new ControllerErrorHandler())
                .build();
    }

    @Test
    void shouldGetPlantsToBeWateredSoonForLoggedUser() throws Exception {
        //given
        var loggedUserId = 1L;
        var plantId = 11L;
        var plantName = "Monstera";
        var plant = new Plant(plantId, plantName, "Living room", 4, LocalDate.now().minusDays(2).toString());
        plant.setUser(new User(loggedUserId, "username", "email@test.com", "password", null));
        var plantCardDto = new PlantCardDTO(plantId, plantName);

        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.findPlantsToBeWateredSoon(loggedUserId)).thenReturn(List.of(plant));
        when(plantMapper.plantToPlantCardDto(plant)).thenReturn(plantCardDto);

        //when && then
        mockMvc.perform(get("/toBeWateredSoon/v2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].plantName").value("Monstera"));
        verify(userService).findIdOfLoggedUser();
        verify(plantService).findPlantsToBeWateredSoon(loggedUserId);
        verify(plantMapper).plantToPlantCardDto(plant);

        verifyNoMoreInteractions(userService, plantService, plantMapper);
    }

    @Test
    void shouldConfirmWateringTodayV2() throws Exception {
        //given
        var loggedUserId = 1L;
        var plantId = 11L;
        var plantName = "Monstera";
        var plant = new Plant(plantId, plantName, "Living room", 4, LocalDate.now().minusDays(2).toString());
        plant.setUser(new User(loggedUserId, "username", "email@test.com", "password", null));
        var plantCardDto = new PlantCardDTO(plantId, plantName);

        when(plantService.find(plantId)).thenReturn(plant);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.updateLastWateredDate(plant)).thenReturn(plant);
        when(plantMapper.plantToPlantCardDto(plant)).thenReturn(plantCardDto);

        //when && then
        mockMvc.perform(post("/toBeWateredSoon/confirmWatering/v2/{id}", plantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(11L))
                .andExpect(jsonPath("$.plantName").value("Monstera"));

        verify(plantService).find(plantId);
        verify(userService).findIdOfLoggedUser();
        verify(plantService).updateLastWateredDate(plant);
        verify(plantMapper).plantToPlantCardDto(plant);
        
        verifyNoMoreInteractions(userService, plantService, plantMapper);
    }

    @Test
    void shouldReturnForbiddenWhenConfirmingWateringForPlantThatDoesNotBelongToUser() throws Exception {
        //given
        var loggedUserId = 1L;
        var plantOwnerId = 99L;
        var plantId = 11L;
        var plant = new Plant(plantId, "Monstera", "Living room", 4, LocalDate.now().minusDays(2).toString());
        plant.setUser(new User(plantOwnerId, "other_user", "other@test.com", "password", null));

        when(plantService.find(plantId)).thenReturn(plant);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);

        //when && then
        mockMvc.perform(post("/toBeWateredSoon/confirmWatering/v2/{id}", plantId))
                .andExpect(status().isForbidden());

        verify(plantService).find(plantId);
        verify(userService).findIdOfLoggedUser();
        
        verifyNoMoreInteractions(userService, plantService, plantMapper);
    }

    @Test
    void shouldReturnEmptyListWhenNoPlantsToBeWateredSoon() throws Exception {
        //given
        var loggedUserId = 1L;

        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.findPlantsToBeWateredSoon(loggedUserId)).thenReturn(List.of());

        //when && then
        mockMvc.perform(get("/toBeWateredSoon/v2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService).findIdOfLoggedUser();
        verify(plantService).findPlantsToBeWateredSoon(loggedUserId);

        verifyNoMoreInteractions(userService, plantService, plantMapper);
    }

    @Test
    void shouldReturnNotFoundWhenConfirmingWateringForNonExistentPlant() throws Exception {
        var plantId = 999L;
        when(plantService.find(plantId)).thenThrow(new NotFoundException("Plant not found"));

        mockMvc.perform(post("/toBeWateredSoon/confirmWatering/v2/{id}", plantId))
                .andExpect(status().isNotFound());
    }

}