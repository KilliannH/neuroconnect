package com.killiann.neuroconnect.dto;

import java.util.List;

public record UserProfileDto(
        Long id,
        String username,
        String email,
        String neuroType,
        String avatarUrl,
        List<UserPostDto> posts
) {}
