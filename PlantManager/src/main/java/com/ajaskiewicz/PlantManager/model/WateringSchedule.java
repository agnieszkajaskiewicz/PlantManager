package com.ajaskiewicz.PlantManager.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class WateringSchedule { //prawdopodobnie można przenieść te 2 pola na poziom Plant, jako integralne kolumny w bazie danych

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
