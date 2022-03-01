package com.ajaskiewicz.PlantManager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PlantCreationDTO { //todo add image
    @NotEmpty
    private String plantName;
    @NotEmpty
    private String roomName;
    @NotNull
    private LocalDate lastWateringDate;
    @NotNull
    private Integer wateringDays;
}
