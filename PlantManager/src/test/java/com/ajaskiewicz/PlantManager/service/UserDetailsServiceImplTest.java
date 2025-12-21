package com.ajaskiewicz.PlantManager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ajaskiewicz.PlantManager.model.Role;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.repository.UserRepository;

public class UserDetailsServiceImplTest {

    private final static String USERNAME = "username";
    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    @BeforeEach
     void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void shouldLoadUserByUsername() {
        //given
        var roles = Set.of(new Role("USER"), new Role("ADMIN"));
        var user = new User(1L,USERNAME, "email", "password", roles);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        //then
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertThat(user.getRoles().stream().map(Role::getName).toList()).containsExactlyInAnyOrderElementsOf(
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        //when && then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(USERNAME));
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }
}
