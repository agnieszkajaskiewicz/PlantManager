package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository <Plant, Integer> {
    List<Plant> findAllById(Integer searchValue);

}
