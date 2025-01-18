package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import javassist.NotFoundException;

import java.util.List;

public interface WateringScheduleService {

    List<WateringSchedule> findAll();

    WateringSchedule find(Integer id) throws NotFoundException;

    WateringSchedule save(WateringSchedule wateringSchedule);
}
