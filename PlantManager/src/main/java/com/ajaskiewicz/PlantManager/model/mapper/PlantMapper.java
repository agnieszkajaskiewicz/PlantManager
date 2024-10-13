package com.ajaskiewicz.PlantManager.model.mapper;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.PlantCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlantMapper {
    PlantCardDTO plantToPlantCardDto(Plant plant);

    @Mapping(target = "imageName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "roomName", target = "room.roomName")
    @Mapping(source = "wateringDays", target = "wateringSchedule.wateringInterval")
    @Mapping(source = "lastWateringDate", target = "wateringSchedule.lastWateredDate")
    @Mapping(target = "wateringDifferenceInDays", ignore = true)
    Plant plantToPlantEntity(PlantCreationDTO plantCreationDTO); //todo add image

}
