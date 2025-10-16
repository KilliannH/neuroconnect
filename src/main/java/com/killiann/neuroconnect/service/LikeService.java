package com.killiann.neuroconnect.service;


import com.killiann.neuroconnect.exception.LikeNotFoundException;
import com.killiann.neuroconnect.model.*;
import com.killiann.neuroconnect.repository.*;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void likePost(Long userId, Long postId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) return;

        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        PostLike postLike = PostLike.builder().user(user).post(post).build();
        likeRepository.save(postLike);
    }

    public void dislikePost(Long userId, Long postId) {
        PostLike like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new LikeNotFoundException("Like not found"));
        likeRepository.delete(like);
    }
}
