package com.ajaskiewicz.PlantManager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @Size(min = 6, max = 32, message = "Please use between 6 and 32 characters.")
    private String password;

    @Transient
    private String repeatPassword;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Plant> plant;

    private String resetPasswordToken;
}
