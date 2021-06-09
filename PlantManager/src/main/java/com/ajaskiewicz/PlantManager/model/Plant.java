package com.ajaskiewicz.PlantManager.model;

import lombok.*;

import javax.persistence.*;
import java.beans.Transient;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String plantName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId")
    private Room room;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wateringScheduleId")
    private WateringSchedule wateringSchedule;

    private String imageName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    public Plant(String plantName, String room, Integer wateringInterval, String lastWateredDate) {
        this.plantName = plantName;
        this.room = new Room(room);
        this.wateringSchedule = new WateringSchedule(wateringInterval, lastWateredDate);
    }

    public Plant(Integer id, String plantName, String roomName, Integer wateringInterval, String lastWateredDate) {
        this.id = id;
        this.plantName = plantName;
        this.room = new Room(roomName);
        this.wateringSchedule = new WateringSchedule(wateringInterval, lastWateredDate);
    }

    @Transient
    public String getImageName() {
        if (imageName == null || id == null) return null;
        return imageName;
    }

}
