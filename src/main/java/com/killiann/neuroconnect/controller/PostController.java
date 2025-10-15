package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.dto.PostRequest;
import com.killiann.neuroconnect.model.Post;
import com.killiann.neuroconnect.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post create(@RequestBody PostRequest request) {
        return postService.createPost(request);
    }

    @GetMapping
    public List<Post> getAll() {
        return postService.getAllPosts();
    }
}

