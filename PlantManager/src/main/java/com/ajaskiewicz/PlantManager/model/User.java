package com.ajaskiewicz.PlantManager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
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

    @NotBlank
    @Size(min = 6, max = 32)
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @Transient
    @NotBlank
    @Size(min = 6, max = 32)
    private String passwordConfirm;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Plant> plant;

    private String resetPasswordToken;
}
