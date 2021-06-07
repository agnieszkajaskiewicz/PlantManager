package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WateringScheduleRepository extends JpaRepository<WateringSchedule, Integer> {
}
