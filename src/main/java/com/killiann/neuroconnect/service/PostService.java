package com.killiann.neuroconnect.service;

import com.killiann.neuroconnect.dto.PostRequest;
import com.killiann.neuroconnect.model.*;
import com.killiann.neuroconnect.repository.*;
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

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}

