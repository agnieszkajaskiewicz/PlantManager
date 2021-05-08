package com.ajaskiewicz.PlantManager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roomName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.EAGER)
    List<Plant> plant;

}
