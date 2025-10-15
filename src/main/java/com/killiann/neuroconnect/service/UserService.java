package com.killiann.neuroconnect.service;

import com.killiann.neuroconnect.model.User;
import com.killiann.neuroconnect.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // hash password
        if (user.getRole() == null) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updatedUser.getUsername());
            existing.setBio(updatedUser.getBio());
            existing.setNeuroType(updatedUser.getNeuroType());
            existing.setAvatarUrl(updatedUser.getAvatarUrl());
            return userRepository.save(existing);
        }).orElseThrow();
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
