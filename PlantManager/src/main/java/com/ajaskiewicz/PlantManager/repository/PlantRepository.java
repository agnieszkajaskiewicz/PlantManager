package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository <Plant, Integer> {
    List<Plant> findAllById(Integer searchValue);
}
