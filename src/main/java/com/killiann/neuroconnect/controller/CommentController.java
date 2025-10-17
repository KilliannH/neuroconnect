package com.killiann.neuroconnect.controller;

import com.killiann.neuroconnect.dto.CommentDto;
import com.killiann.neuroconnect.dto.CommentRequest;
import com.killiann.neuroconnect.model.Comment;
import com.killiann.neuroconnect.service.CommentService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CommentDto>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}