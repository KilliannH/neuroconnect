package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.exception.UserNotFoundException;
import com.killiann.neuroconnect.model.User;
import com.killiann.neuroconnect.repository.UserRepository;
import com.killiann.neuroconnect.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 🔍 Lister tous les utilisateurs
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 🔍 Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔍 Récupérer un utilisateur par email (optionnel)
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return ResponseEntity.ok(user);
    }

    // 🔄 Mettre à jour son profil
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updated) {
        return ResponseEntity.ok(userService.updateUser(id, updated));
    }

    // 👤 Récupérer l'utilisateur connecté via le token JWT
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String name = authentication.getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found with username: " + name));
        return ResponseEntity.ok(user);
    }
}
