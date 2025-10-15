package com.killiann.neuroconnect.dto;

import lombok.Data;

@Data
public class PostRequest {
    private Long userId;
    private String content;
}
