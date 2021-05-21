package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;

public interface WateringScheduleService {

    public Iterable<WateringSchedule> findAll();

    public WateringSchedule find(int id);

    public WateringSchedule save(WateringSchedule wateringSchedule);
}
