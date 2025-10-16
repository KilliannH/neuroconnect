package com.killiann.neuroconnect.dto;

import java.time.LocalDateTime;

public record UserPostDto(
        Long id,
        String content,
        LocalDateTime createdAt
) {
}
