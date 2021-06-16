package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> findAll();

    Room find(Integer id);

    Room save(Room room);
}
