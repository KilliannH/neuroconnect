package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.dto.CommentRequest;
import com.killiann.neuroconnect.model.Comment;
import com.killiann.neuroconnect.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment create(@RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping("/{postId}")
    public List<Comment> getByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }
}