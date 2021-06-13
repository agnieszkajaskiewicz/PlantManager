package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Room;

public interface RoomService {

    Iterable<Room> findAll();

    Room find(int id);

    Room save(Room room);
}
