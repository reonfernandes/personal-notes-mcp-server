package com.reon.mcppersonalnotes.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.ai.tool.annotation.ToolParam;

public record UpdateNote(
        @ToolParam(description = "New title; omit to keep the current one", required = false)
        @Pattern(regexp = "(?s).*\\S.*", message = "title must not be blank")
        String title,
        @ToolParam(description = "New body, up to 5000 characters; omit to keep the current one", required = false)
        @Size(max = 5000, message = "Maximum 5k characters allowed")
        String content,
        @ToolParam(description = "New comma-separated tags; omit to keep the current ones", required = false)
        String tags
) {
}
