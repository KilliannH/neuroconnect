package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.dto.LoginRequest;
import com.killiann.neuroconnect.model.User;
import com.killiann.neuroconnect.security.JwtService;
import com.killiann.neuroconnect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username déjà utilisé");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        User saved = userService.register(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userService.findByEmail(req.getEmail())
                .filter(u -> userService.checkPassword(req.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(jwtService.generateToken(u.getUsername())))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}
