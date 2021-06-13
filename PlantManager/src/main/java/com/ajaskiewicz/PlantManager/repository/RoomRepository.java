package com.ajaskiewicz.PlantManager.repository;

import com.ajaskiewicz.PlantManager.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
