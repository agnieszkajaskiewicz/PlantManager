package com.ajaskiewicz.PlantManager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @Transient
    private Integer wateringDifferenceInDays;

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

    public String getImageName() {
        if (imageName == null || id == null) {
            return null;
        }
        return imageName;
    }

}
