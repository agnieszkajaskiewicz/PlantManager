package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.PlantCreationDTO;
import com.ajaskiewicz.PlantManager.model.mapper.PlantMapper;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.RoomService;
import com.ajaskiewicz.PlantManager.service.WateringScheduleService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlantControllerMockMvcTest {

    private MockMvc mockMvc;

    private PlantService plantService;
    private RoomService roomService;
    private WateringScheduleService wateringScheduleService;
    private UserService userService;
    private SecurityService securityService;
    private PlantMapper plantMapper;

    private PlantController plantController;

    @BeforeEach
    void setUp() {
        plantService = mock(PlantService.class);
        roomService = mock(RoomService.class);
        wateringScheduleService = mock(WateringScheduleService.class);
        userService = mock(UserService.class);
        securityService = mock(SecurityService.class);
        plantMapper = mock(PlantMapper.class);
        openMocks(this);
        plantController = new PlantController(plantService, roomService, wateringScheduleService, userService, securityService, plantMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(plantController).build();
    }

    @Test
    void shouldGetPlantsForLoggedUser() throws Exception {
        //given
        var loggedUserId = 52;
        var id = 11;
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
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].plantName", is(plantName)));
        verify(securityService).isAuthenticated();
        verify(userService).findIdOfLoggedUser();
        verify(plantService).findAllByUserId(loggedUserId);
        verify(plantMapper).plantToPlantCardDto(plantEntity);
        verifyNoMoreInteractions(securityService, userService, plantService, plantMapper);
    }

    @Test
    void shouldDeletePlantById() throws Exception {
        //given
        var plantId = 12;
        when(securityService.isAuthenticated()).thenReturn(true);
        //when && then
        mockMvc.perform(delete("/dashboard/deletePlant/v2/12"))
                .andExpect(status().isOk());
        verify(securityService).isAuthenticated();
        verify(plantService).delete(plantId);
        verifyNoMoreInteractions(plantService, securityService);
    }

    @Test
    void shouldCreatePlant() throws Exception {
        //given
        var plantCreationDTO = new PlantCreationDTO("plantName", "room", LocalDate.now().minusDays(2), 3);
        var entityToSave = new Plant();
        when(plantMapper.plantToPlantEntity(any())).thenReturn(entityToSave);
        when(plantService.createOrUpdatePlant(entityToSave)).thenReturn(entityToSave);
        //when && then
        mockMvc.perform(post("/dashboard/addPlant/v2")
                        .contentType(MediaType.APPLICATION_JSON)
                .content((new ObjectMapper()).writeValueAsString(plantCreationDTO)))
                .andExpect(status().isCreated());
        verify(plantMapper).plantToPlantEntity(any(PlantCreationDTO.class)); //todo use captor(?)
        verify(plantService).createOrUpdatePlant(entityToSave);

        verifyNoMoreInteractions(plantMapper, plantService);
    }
}