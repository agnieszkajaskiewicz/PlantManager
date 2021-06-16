package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room find(Integer id) {
        return roomRepository.findById(id).get();
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }
}


