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
public class WateringSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer watering_interval;

    private Date last_watered_date;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "wateringSchedule", fetch = FetchType.EAGER)
    private Plant plant;

    public WateringSchedule(Integer wateringInterval, Date lastWateredDate) {
        this.watering_interval = wateringInterval;
        this.last_watered_date = lastWateredDate;
    }
}
