package com.reon.mcppersonalnotes.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoteResponse(
        Long id,
        String title,
        String content,
        String tags,
        LocalDateTime createdAt
) {
}
