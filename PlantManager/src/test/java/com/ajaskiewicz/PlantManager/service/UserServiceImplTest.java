package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.Role;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.repository.RoleRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String EMAIL = "email@email.com";
    private static final Long USER_ID = 1L;
    private static final String TOKEN = "token";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecurityContext securityContext;
    private Authentication authentication;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        userService = new UserServiceImpl(userRepository, roleRepository, bCryptPasswordEncoder);
    }

    @Test
    void shouldFindAllUsers() {
        //given
        var allUsers = prepareUsers();
        when(userRepository.findAll()).thenReturn(allUsers);

        //when
        var foundUsers = userRepository.findAll();

        //then
        assertEquals(allUsers, foundUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldFindUserByUsername() {
        //given
        var user = new User();
        user.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        //when
        var foundUser = userService.findByUsername(USERNAME);

        //then
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        //when && then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(USERNAME));

        assertEquals("Could not find user with username: " + USERNAME, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void shouldFindUserById() {
        //given
        var user = new User();
        user.setId(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        var foundUser = userService.findById(USER_ID);

        //then
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        //given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        //when && then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.findById(USER_ID));

        assertEquals("Could not find user with ID: " + USER_ID, exception.getMessage());
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void shouldFindUserByEmail() {
        //given
        var user = new User();
        user.setEmail(EMAIL);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //when
        var foundUser = userService.findByEmail(user.getEmail());

        //then
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        //given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        //when && then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.findByEmail(EMAIL));

        assertEquals("Could not find user with email: " + EMAIL, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void shouldFindUserByResetPasswordToken() {
        var user = new User();
        user.setResetPasswordToken(TOKEN);
        when(userRepository.findByResetPasswordToken(TOKEN)).thenReturn(Optional.of(user));

        //when
        var foundUser = userService.findByResetPasswordToken(TOKEN);

        //then

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByResetPasswordToken(TOKEN);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByResetPasswordToken() {
        //given
        when(userRepository.findByResetPasswordToken(TOKEN)).thenReturn(Optional.empty());

        //when && then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.findByResetPasswordToken(TOKEN));

        assertEquals("Could not find user with token: " + TOKEN, exception.getMessage());
        verify(userRepository, times(1)).findByResetPasswordToken(TOKEN);
    }

    @Test
    void shouldFindIdOfLoggedInUser() {
        //given
        var user = new User();
        user.setUsername(USERNAME);
        user.setId(USER_ID);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);

        //when
        var foundId = userService.findIdOfLoggedUser();

        //then
        assertEquals(USER_ID, foundId);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void shouldSaveNewUser() {
        //given
        var user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        var newRole = new Role();
        newRole.setName("USER");
        var roles = List.of(newRole);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(roleRepository.findAll()).thenReturn(roles);

        //when
        userService.save(user);

        //then
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        assertEquals(new HashSet<>(roles), user.getRoles());
        verify(bCryptPasswordEncoder, times(1)).encode(PASSWORD);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateResetPasswordToken() {
        //given
        var user = new User();
        user.setEmail(EMAIL);
        user.setResetPasswordToken(null);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        //when
        userService.updateResetPasswordToken(TOKEN, EMAIL);

        //then
        assertEquals(TOKEN, user.getResetPasswordToken());
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnResetPasswordTokenUpdate() {
        //given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        //when && then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.updateResetPasswordToken(TOKEN, EMAIL));

        assertEquals("Could not find any user with the email: " + EMAIL, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void shouldUpdatePassword() {
        //given
        var user = new User();
        user.setPassword("oldPassword");
        user.setResetPasswordToken(TOKEN);
        when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

        //when
        userService.updatePassword(user, PASSWORD);

        //then
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        assertNull(user.getResetPasswordToken());
        verify(bCryptPasswordEncoder, times(1)).encode(PASSWORD);
        verify(userRepository, times(1)).save(user);
    }

    private List<User> prepareUsers() {
        var user1 = new User();
        user1.setId(1L);
        var user2 = new User();
        user2.setId(2L);
        var user3 = new User();
        user3.setId(3L);
        return List.of(user1, user2, user3);
    }
}
