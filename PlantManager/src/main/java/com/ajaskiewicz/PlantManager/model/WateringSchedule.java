package com.ajaskiewicz.PlantManager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.Positive;

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

    @Positive
    private Integer wateringInterval;

    private String lastWateredDate;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "wateringSchedule", fetch = FetchType.EAGER)
    private Plant plant;

    public WateringSchedule(Integer wateringInterval, String lastWateredDate) {
        this.wateringInterval = wateringInterval;
        this.lastWateredDate = lastWateredDate;
    }

}
