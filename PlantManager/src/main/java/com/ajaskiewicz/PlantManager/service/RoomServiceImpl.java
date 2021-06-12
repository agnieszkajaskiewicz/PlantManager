package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

    @Autowired //@autowired na polu a nie konstruktorze
    private RoomRepository roomRepository;

    @Override
    public Iterable<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room find(int id) {
        return roomRepository.findById(id).get();
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }
}


