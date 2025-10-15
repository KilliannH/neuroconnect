package com.killiann.neuroconnect.repository;

import com.killiann.neuroconnect.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findByPostId(Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}