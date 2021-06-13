package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;

public interface WateringScheduleService {

    Iterable<WateringSchedule> findAll();

    WateringSchedule find(int id);

    WateringSchedule save(WateringSchedule wateringSchedule);
}
