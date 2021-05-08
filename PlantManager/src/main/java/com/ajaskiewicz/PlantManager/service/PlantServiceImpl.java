package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("plantService")
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;

    @Override
    public Iterable<Plant> findAll() {
        return plantRepository.findAll();
    }

    @Override
    public Plant find(int id) {
        return plantRepository.findById(id).get();
    }

    @Override
    public Plant save(Plant plant) {
        return plantRepository.save(plant);
    }

}
