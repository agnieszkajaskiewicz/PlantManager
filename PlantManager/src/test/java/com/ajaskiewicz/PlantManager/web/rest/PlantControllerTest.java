package com.ajaskiewicz.PlantManager.web.rest;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ajaskiewicz.PlantManager.exceptions.ControllerErrorHandler;
import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.PlantCreationDTO;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.model.mapper.PlantMapper;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.NotFoundException;

class PlantControllerTest {

    private MockMvc mockMvc;

    private PlantService plantService;
    private UserService userService;
    private SecurityService securityService;
    private PlantMapper plantMapper;

    private PlantController plantController;

    @BeforeEach
    void setUp() {
        plantService = mock(PlantService.class);
        userService = mock(UserService.class);
        securityService = mock(SecurityService.class);
        plantMapper = mock(PlantMapper.class);
        openMocks(this);
        plantController = new PlantController(plantService, userService, securityService, plantMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(plantController)
                .setControllerAdvice(new ControllerErrorHandler())
                .build();
    }

    @Test
    void shouldGetPlantsForLoggedUser() throws Exception {
        //given
        var loggedUserId = 52L;
        var id = 11L;
        var plantName = "Zdzisław";
        var plantEntity = new Plant();
        plantEntity.setId(id);
        plantEntity.setPlantName(plantName);

        var plantCardDto = new PlantCardDTO(id, plantName);

        when(securityService.isAuthenticated()).thenReturn(true);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.findAllByUserId(loggedUserId)).thenReturn(newArrayList(plantEntity));
        when(plantMapper.plantToPlantCardDto(plantEntity)).thenReturn(plantCardDto);

            //when && then
        mockMvc.perform(get("/dashboard/v2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) id)))
                .andExpect(jsonPath("$[0].plantName", is(plantName)));
        verify(userService).findIdOfLoggedUser();
        verify(plantService).findAllByUserId(loggedUserId);
        verify(plantMapper).plantToPlantCardDto(plantEntity);
        verifyNoMoreInteractions(securityService, userService, plantService, plantMapper);
    }

    @Test
    void shouldGetPlantByIdForLoggedUser() throws Exception {
        //given
        var loggedUserId = 52L;
        var plantId = 11L;
        var plantName = "Zdzisław";
        var plantEntity = new Plant();
        plantEntity.setId(plantId);
        plantEntity.setPlantName(plantName);
        plantEntity.setUser(new User(loggedUserId, "username", "email@test.com", "password", null));

        when(securityService.isAuthenticated()).thenReturn(true);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.find(plantId)).thenReturn(plantEntity);
        when(plantMapper.plantToPlantDetailDto(plantEntity)).thenReturn(mock(com.ajaskiewicz.PlantManager.model.PlantDetailDTO.class));

        //when && then
        mockMvc.perform(get("/dashboard/v2/11"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(userService).findIdOfLoggedUser();
        verify(plantService).find(plantId);
        verify(plantMapper).plantToPlantDetailDto(plantEntity);
        verifyNoMoreInteractions(securityService, userService, plantService, plantMapper);
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentPlant() throws Exception {
        var plantId = 999L;
        when(plantService.find(plantId)).thenThrow(new NotFoundException("Plant not found"));

        mockMvc.perform(get("/dashboard/v2/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePlantById() throws Exception {
        //given
        var plantId = 12L;
        var loggedUserId = 1L;
        var plantEntity = new Plant();
        plantEntity.setId(plantId);
        plantEntity.setUser(new User(loggedUserId, "username", "email@test.com", "password", null));
        
        when(plantService.find(plantId)).thenReturn(plantEntity);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);

        //when && then
        mockMvc.perform(delete("/dashboard/deletePlant/v2/12"))
                .andExpect(status().isOk());
        verify(plantService).find(plantId);
        verify(userService).findIdOfLoggedUser();
        verify(plantService).delete(plantId);
        verifyNoMoreInteractions(plantService, userService, securityService);
    }

    @Test
    void shouldCreatePlant() throws Exception {
        //given
        var plantCreationDTO = new PlantCreationDTO("plantName", "room", LocalDate.now().minusDays(2), 3);
        var entityToSave = new Plant();
        var plantCardDto = new PlantCardDTO(1L, "plantName");
        when(plantMapper.plantToPlantEntity(any())).thenReturn(entityToSave);
        when(plantService.createOrUpdatePlant(entityToSave)).thenReturn(entityToSave);
        when(plantMapper.plantToPlantCardDto(entityToSave)).thenReturn(plantCardDto);

        //when && then
        mockMvc.perform(post("/dashboard/addPlant/v2")
                        .contentType(MediaType.APPLICATION_JSON)
                .content((new ObjectMapper()).writeValueAsString(plantCreationDTO)))
                .andExpect(status().isCreated());
        verify(plantMapper).plantToPlantEntity(any(PlantCreationDTO.class)); //todo use captor(?)
        verify(plantService).createOrUpdatePlant(entityToSave);
        verify(plantMapper).plantToPlantCardDto(entityToSave);

        verifyNoMoreInteractions(plantMapper, plantService);
    }

    @Test
    void shouldUpdatePlant() throws Exception {
        //given
        var plantId = 15L;
        var loggedUserId = 1L;
        var plantCreationDTO = new PlantCreationDTO("new plant name", "new room", LocalDate.now().minusDays(2), 3);
        var existingPlant = new Plant(plantId, "existing plant name", "existing room", 5, LocalDate.now().minusDays(5).toString());
        existingPlant.setUser(new User(loggedUserId, "username", "email@test.com", "password", null));
        when(plantService.find(plantId)).thenReturn(existingPlant);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.createOrUpdatePlant(existingPlant)).thenReturn(existingPlant);

        //when && then
        mockMvc.perform(put("/dashboard/updatePlant/v2/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(plantCreationDTO)))
                .andExpect(status().isOk());
        verify(plantService).find(plantId);
        verify(userService).findIdOfLoggedUser();
        verify(plantMapper).updatePlantFromDto(any(PlantCreationDTO.class), any(Plant.class));
        verify(plantService).createOrUpdatePlant(existingPlant);
        verify(plantMapper).plantToPlantCardDto(any(Plant.class));

        verifyNoMoreInteractions(plantMapper, plantService, userService);
    }

    @Test
    void shouldReturnForbiddenWhenGettingPlantThatDoesNotBelongToUser() throws Exception {
        //given
        var loggedUserId = 52L;
        var plantOwnerId = 99L;
        var plantId = 11L;
        var plantEntity = new Plant();
        plantEntity.setId(plantId);
        plantEntity.setPlantName("Zdzisław");
        plantEntity.setUser(new User(plantOwnerId, "other_user", "other@test.com", "password", null));

        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);
        when(plantService.find(plantId)).thenReturn(plantEntity);

        //when && then
        mockMvc.perform(get("/dashboard/v2/11"))
                .andExpect(status().isForbidden());
        verify(userService).findIdOfLoggedUser();
        verify(plantService).find(plantId);
        verifyNoMoreInteractions(securityService, userService, plantService, plantMapper);
    }

    @Test
    void shouldReturnForbiddenWhenUpdatingPlantThatDoesNotBelongToUser() throws Exception {
        //given
        var plantId = 15L;
        var loggedUserId = 1L;
        var plantOwnerId = 99L;
        var plantCreationDTO = new PlantCreationDTO("new plant name", "new room", LocalDate.now().minusDays(2), 3);
        var existingPlant = new Plant(plantId, "existing plant name", "existing room", 5, LocalDate.now().minusDays(5).toString());
        existingPlant.setUser(new User(plantOwnerId, "other_user", "other@test.com", "password", null));
        
        when(plantService.find(plantId)).thenReturn(existingPlant);
        when(userService.findIdOfLoggedUser()).thenReturn(loggedUserId);

        //when && then
        mockMvc.perform(put("/dashboard/updatePlant/v2/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(plantCreationDTO)))
                .andExpect(status().isForbidden());
        verify(plantService).find(plantId);
        verify(userService).findIdOfLoggedUser();
        verifyNoMoreInteractions(plantMapper, plantService, userService);
    }
}