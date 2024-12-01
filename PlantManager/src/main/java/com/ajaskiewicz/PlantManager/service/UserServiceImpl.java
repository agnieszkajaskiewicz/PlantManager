package com.ajaskiewicz.PlantManager.service;

import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.repository.RoleRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.orElseThrow(() -> new UsernameNotFoundException("Could not find user with username: " + username));
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(() -> new UsernameNotFoundException("Could not find user with ID: " + id));
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new UsernameNotFoundException("Could not find user with email: " + email));
    }

    @Override
    public User findByResetPasswordToken(String token) {
        Optional<User> user = userRepository.findByResetPasswordToken(token);

        return user.orElseThrow(() -> new UsernameNotFoundException("Could not find user with token: " + token));
    }

    @Override
    public Long findIdOfLoggedUser() {
        log.info("Checking ID of logged user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> queriedUser = userRepository.findByUsername(username);

        if (queriedUser.isPresent()) {
            Long userId = queriedUser.get().getId();
            log.info("ID of logged user: " + userId);
            return userId;
        } else {
            throw new UsernameNotFoundException("Could not find user with username: " + username);
        }
    }

    @Override
    public void save(User user) {
        log.info("Saving new user");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
        log.info("New user saved: " + user.getUsername());
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setResetPasswordToken(token);
            userRepository.save(user.get());
        } else {
            throw new UsernameNotFoundException("Could not find any user with the email: " + email);
        }
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}
