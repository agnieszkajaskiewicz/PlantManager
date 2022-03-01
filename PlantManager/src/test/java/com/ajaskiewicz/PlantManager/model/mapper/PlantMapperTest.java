package com.ajaskiewicz.PlantManager.model.mapper;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCreationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlantMapperTest {

    private static final Integer WATERING_DAYS = 3;
    private static final LocalDate LAST_WATERING_DATE = LocalDate.now().minusDays(2);
    private static final String ROOM_NAME = "roomName";
    private static final String PLANT_NAME = "plantName";

    private PlantMapper plantMapper;

    @BeforeEach
    void setUp() {
        plantMapper = Mappers.getMapper(PlantMapper.class);
    }

    @Test
    void shouldMapPlantEntityToPlantCardDto() {
        //given
        var id = 1;
        var entity = new Plant(id, PLANT_NAME, null, null, null);
        //when
        var result = plantMapper.plantToPlantCardDto(entity);
        //then
        assertEquals(id, result.getId());
        assertEquals(PLANT_NAME, result.getPlantName());
    }

    @Test
    void shouldMapPlantCreationDTOToPlantEntity() {
        //given
        var plantCreationDTO = new PlantCreationDTO(PLANT_NAME, ROOM_NAME, LAST_WATERING_DATE, WATERING_DAYS);
        //when
        var entity = plantMapper.plantToPlantEntity(plantCreationDTO);
        //then
        assertEquals(PLANT_NAME, entity.getPlantName());
        assertEquals(ROOM_NAME, entity.getRoom().getRoomName());
        assertEquals(LAST_WATERING_DATE.toString(), entity.getWateringSchedule().getLastWateredDate());
        assertEquals(WATERING_DAYS, entity.getWateringSchedule().getWateringInterval());
    }
}