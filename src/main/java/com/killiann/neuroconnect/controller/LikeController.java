package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.service.LikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/user/{userId}")
    public void likePost(@PathVariable Long postId, @PathVariable Long userId) {
        likeService.likePost(userId, postId);
    }
}