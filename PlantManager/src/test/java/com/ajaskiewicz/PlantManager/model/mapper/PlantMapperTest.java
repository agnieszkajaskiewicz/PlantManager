package com.ajaskiewicz.PlantManager.model.mapper;

import com.ajaskiewicz.PlantManager.model.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

class PlantMapperTest {

    private PlantMapper plantMapper;

    @BeforeEach
    void setUp() {
        plantMapper = Mappers.getMapper(PlantMapper.class);
    }

    @Test
    void shouldMapPlantEntityToPlantCardDto() {
        //given
        var id = 1;
        var plantName = "plantName";
        var entity = new Plant(id, plantName, null, null, null);
        //when
        var result = plantMapper.plantToPlantCardDto(entity);
        //then
        assertEquals(id, result.getId());
        assertEquals(plantName, result.getPlantName());
    }
}