package com.killiann.neuroconnect.service;

import com.killiann.neuroconnect.dto.PostDto;
import com.killiann.neuroconnect.dto.PostRequest;
import com.killiann.neuroconnect.exception.UserNotFoundException;
import com.killiann.neuroconnect.model.*;
import com.killiann.neuroconnect.repository.*;
import org. springframework. security. core. Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(PostRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Post post = Post.builder()
                .content(request.getContent())
                .author(user)
                .build();
        return postRepository.save(post);
    }

    public List<PostDto> getAllPosts(Authentication authentication) {
        String name = authentication.getName();
        User currentUser = userRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException("User not found with username: " + name));
        return postRepository.findAll().stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getContent(),
                        post.getCreatedAt(),
                        post.getAuthor() != null ? post.getAuthor().getUsername() : null,
                        post.getAuthor() != null ? post.getAuthor().getAvatarUrl() : null,
                        post.getPostLikes() != null ? post.getPostLikes().size() : 0,
                        post.getComments() != null ? post.getComments().size() : 0,
                        post.getPostLikes() != null && post.getPostLikes().stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId()))
                ))
                .toList();
    }
}

