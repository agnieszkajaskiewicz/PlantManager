package com.ajaskiewicz.PlantManager.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class WateringSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer watering_interval;

    private String last_watered_date;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "wateringSchedule", fetch = FetchType.EAGER)
    private Plant plant;

    public WateringSchedule(Integer wateringInterval, String lastWateredDate) {
        this.watering_interval = wateringInterval;
        this.last_watered_date = lastWateredDate;
    }

}
