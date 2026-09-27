package com.reon.mcppersonalnotes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateNote(
        @NotBlank(message = "title is required")
        String title,
        @Size(max = 5000, message = "Maximum 5k characters allowed")
        String content,
        String tags
) {
}
