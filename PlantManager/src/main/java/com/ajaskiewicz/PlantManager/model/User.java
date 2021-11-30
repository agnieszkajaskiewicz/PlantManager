package com.ajaskiewicz.PlantManager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Email
    private String email;

    private String password;

    @Transient
    private String repeatPassword;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Plant> plant;

    private String resetPasswordToken;
}
