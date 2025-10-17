package com.killiann.neuroconnect.service;

import com.killiann.neuroconnect.dto.CommentDto;
import com.killiann.neuroconnect.dto.CommentRequest;
import com.killiann.neuroconnect.model.*;
import com.killiann.neuroconnect.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment createComment(CommentRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Post post = postRepository.findById(request.getPostId()).orElseThrow();

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor().getUsername(),
                        comment.getAuthor().getAvatarUrl(),
                        comment.getCreatedAt()
                ))
                .toList();
    }
}
