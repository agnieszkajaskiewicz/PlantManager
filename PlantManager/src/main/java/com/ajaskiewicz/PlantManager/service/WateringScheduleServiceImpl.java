package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("wateringScheduleService")
public class WateringScheduleServiceImpl implements WateringScheduleService {

    private final WateringScheduleRepository wateringScheduleRepository;

    @Autowired
    public WateringScheduleServiceImpl(WateringScheduleRepository wateringScheduleRepository) {
        this.wateringScheduleRepository = wateringScheduleRepository;
    }

    @Override
    public List<WateringSchedule> findAll() {
        return wateringScheduleRepository.findAll();
    }

    @Override
    public WateringSchedule find(Integer id) throws NotFoundException {
        Optional<WateringSchedule> wateringSchedule = wateringScheduleRepository.findById(id);
        return wateringSchedule.orElseThrow(() -> new NotFoundException("Watering schedule with ID " + id + " not found"));
    }

    @Override
    public WateringSchedule save(WateringSchedule wateringSchedule) {
        return wateringScheduleRepository.save(wateringSchedule);
    }
}
