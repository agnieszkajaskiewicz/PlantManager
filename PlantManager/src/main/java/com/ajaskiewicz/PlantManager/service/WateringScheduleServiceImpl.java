package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("wateringScheduleService")
public class WateringScheduleServiceImpl implements WateringScheduleService {

    @Autowired
    private WateringScheduleRepository wateringScheduleRepository;

    @Override
    public Iterable<WateringSchedule> findAll() {
        return wateringScheduleRepository.findAll();
    }

    @Override
    public WateringSchedule find(int id) {
        return wateringScheduleRepository.findById(id).get();
    }

    @Override
    public WateringSchedule save(WateringSchedule wateringSchedule) {
        return wateringScheduleRepository.save(wateringSchedule);
    }
}