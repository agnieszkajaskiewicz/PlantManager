package com.ajaskiewicz.PlantManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "This field is required.")
    @Size(min = 6, max = 32, message = "Please use between 6 and 32 characters.")
    private String username;

    @Email(message = "Use proper email format.")
    @NotEmpty(message = "This field is required.")
    private String email;

    @NotEmpty(message = "This field is required.")
    private String password;

    @Transient
    private String repeatPassword;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Plant> plant;

    private String resetPasswordToken;
}
