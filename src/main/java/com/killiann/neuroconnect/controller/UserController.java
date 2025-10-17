package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.dto.CommentDto;
import com.killiann.neuroconnect.dto.PostDto;
import com.killiann.neuroconnect.dto.UserPostDto;
import com.killiann.neuroconnect.dto.UserProfileDto;
import com.killiann.neuroconnect.exception.UserNotFoundException;
import com.killiann.neuroconnect.model.User;
import com.killiann.neuroconnect.model.Comment;
import com.killiann.neuroconnect.repository.PostRepository;
import com.killiann.neuroconnect.repository.UserRepository;
import com.killiann.neuroconnect.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserController(UserService userService, UserRepository userRepository, PostRepository postRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
    public UserProfileDto me(Authentication authentication) {
        String name = authentication.getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found with username: " + name));
        List<UserPostDto> posts = user.getPosts().stream()
                .map(p -> new UserPostDto(p.getId(), p.getContent(), p.getCreatedAt()))
                .toList();

        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNeuroType(),
                user.getAvatarUrl(),
                posts
        );
    }

    @GetMapping("/{id}/posts")
    public List<PostDto> getUserPosts(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return postRepository.findByAuthorIdOrderByCreatedAtDesc(id).stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getContent(),
                        post.getCreatedAt(),
                        user.getUsername(),
                        user.getAvatarUrl(),
                        post.getPostLikes() != null ? post.getPostLikes().size() : 0,
                        post.getComments() != null ? post.getComments().size() : 0,
                        false, // ou calculé
                        post.getComments() != null
                                ? post.getComments().stream()
                                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                                .limit(5)
                                .map(comment -> new CommentDto(
                                        comment.getId(),
                                        comment.getContent(),
                                        comment.getAuthor() != null ? comment.getAuthor().getUsername() : "Anonyme",
                                        comment.getAuthor() != null ? comment.getAuthor().getAvatarUrl(): null,
                                        comment.getCreatedAt()
                                ))
                                .toList()
                                : List.of()
                ))
                .toList();
    }
}
