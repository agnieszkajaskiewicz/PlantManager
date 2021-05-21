package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Room;

public interface RoomService {

    public Iterable<Room> findAll();

    public Room find(int id);

    public Room save(Room room);
}
