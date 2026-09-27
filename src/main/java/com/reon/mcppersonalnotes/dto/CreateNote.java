package com.reon.mcppersonalnotes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.ai.tool.annotation.ToolParam;

@Builder
public record CreateNote(
        @ToolParam(description = "Title of the note")
        @NotBlank(message = "title is required")
        String title,
        @ToolParam(description = "Body of the note, up to 5000 characters", required = false)
        @Size(max = 5000, message = "Maximum 5k characters allowed")
        String content,
        @ToolParam(description = "Comma-separated tags, e.g. 'work,ideas'", required = false)
        String tags
) {
}
