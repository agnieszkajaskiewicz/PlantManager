package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;

import java.util.List;

public interface WateringScheduleService {

    List<WateringSchedule> findAll();

    WateringSchedule find(Integer id);

    WateringSchedule save(WateringSchedule wateringSchedule);
}
