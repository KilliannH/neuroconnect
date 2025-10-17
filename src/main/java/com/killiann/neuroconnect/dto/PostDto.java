package com.killiann.neuroconnect.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        String authorUsername,
        String authorAvatar,
        int likeCount,
        int commentCount,
        boolean isLikedByCurrentUser,
        List<CommentDto> lastComments
) {
}
