package com.ajaskiewicz.PlantManager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@Entity
@Table
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String plantName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId")
    private Room room;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wateringScheduleId")
    private WateringSchedule wateringSchedule;

    private String picture_path;

    public Plant() {
    }

    public Plant(String plantName, Room room, Integer wateringInterval, Date lastWateredDate, String picturePath) {
        this.plantName = plantName;
        this.room = room;
        this.wateringSchedule = new WateringSchedule(wateringInterval, lastWateredDate);
        this.picture_path = picturePath;
    }

}
