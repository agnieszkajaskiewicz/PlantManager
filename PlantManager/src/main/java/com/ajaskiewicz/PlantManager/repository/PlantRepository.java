package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository <Plant, Integer> {
    List<Plant> findAllById(Integer searchValue);

    List<Plant> findAllByUserId(Integer userId);

}
