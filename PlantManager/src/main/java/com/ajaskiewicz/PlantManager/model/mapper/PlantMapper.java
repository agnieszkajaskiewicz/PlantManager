package com.ajaskiewicz.PlantManager.model.mapper;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlantMapper {
    PlantCardDTO plantToPlantCardDto(Plant plant);
}
