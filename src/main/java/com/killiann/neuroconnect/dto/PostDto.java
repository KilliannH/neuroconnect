package com.killiann.neuroconnect.dto;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        String authorUsername,
        String authorAvatar,
        int likeCount,
        int commentCount,
        boolean isLikedByCurrentUser
) {
}
