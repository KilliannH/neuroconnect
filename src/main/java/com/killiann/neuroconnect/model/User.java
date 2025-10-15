package com.killiann.neuroconnect.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String role;

    private String bio;
    private String password;
    private String neuroType;
    private String avatarUrl;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;
}