package com.reon.mcppersonalnotes.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateNote(
        @Pattern(regexp = "(?s).*\\S.*", message = "title must not be blank")
        String title,
        @Size(max = 5000, message = "Maximum 5k characters allowed")
        String content,
        String tags
) {
}
