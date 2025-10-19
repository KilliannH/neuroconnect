package com.killiann.neuroconnect.service;

import com.killiann.neuroconnect.dto.CommentDto;
import com.killiann.neuroconnect.dto.PostDto;
import com.killiann.neuroconnect.dto.PostRequest;
import com.killiann.neuroconnect.exception.PostNotFoundException;
import com.killiann.neuroconnect.exception.UserNotFoundException;
import com.killiann.neuroconnect.model.*;
import com.killiann.neuroconnect.repository.*;
import org.springframework.data.domain.PageRequest;
import org. springframework. security. core. Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private  final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
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
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // on récupère tous les posts
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> {
                    // Récupérer les 5 derniers commentaires pour ce post
                    List<CommentDto> lastComments = commentRepository
                            .findTop5ByPostId(post.getId(), PageRequest.of(0, 5))
                            .stream()
                            .map(comment -> new CommentDto(
                                    comment.getId(),
                                    comment.getContent(),
                                    comment.getAuthor().getUsername(),
                                    comment.getAuthor().getAvatarUrl(),
                                    comment.getCreatedAt()
                            ))
                            .toList();

                    return new PostDto(
                            post.getId(),
                            post.getContent(),
                            post.getCreatedAt(),
                            post.getAuthor() != null ? post.getAuthor().getUsername() : null,
                            post.getAuthor() != null ? post.getAuthor().getAvatarUrl() : null,
                            post.getPostLikes() != null ? post.getPostLikes().size() : 0,
                            post.getComments() != null ? post.getComments().size() : 0,
                            post.getPostLikes() != null &&
                                    post.getPostLikes().stream()
                                            .anyMatch(like -> like.getUser().getId().equals(currentUser.getId())),
                            lastComments
                    );
                })
                .toList();
    }
    public PostDto getById(Long id, Authentication auth) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElseThrow();

        Post post = postRepository.findById(id).orElseThrow(() ->
                new PostNotFoundException("Post introuvable"));

        return new PostDto(
                post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getAuthor() != null ? post.getAuthor().getUsername() : null,
                post.getAuthor() != null ? post.getAuthor().getAvatarUrl() : null,
                post.getPostLikes().size(),
                post.getComments().size(),
                post.getPostLikes().stream().anyMatch(like -> like.getUser().getId().equals(currentUser.getId())),
                post.getComments().stream()
                        .filter(c -> c.getCreatedAt() != null)
                        .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                        .limit(20)
                        .map(c -> new CommentDto(
                                c.getId(),
                                c.getContent(),
                                c.getAuthor() != null ? c.getAuthor().getUsername() : "Anonyme",
                                c.getAuthor() != null ? c.getAuthor().getAvatarUrl() : null,
                                c.getCreatedAt()
                        ))
                        .toList()
        );
    }
}

