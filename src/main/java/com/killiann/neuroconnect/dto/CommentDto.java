package com.killiann.neuroconnect.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String content,
        String authorUsername,
        String authorAvatar,
        LocalDateTime createdAt
) {}