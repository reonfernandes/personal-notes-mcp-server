package com.reon.mcppersonalnotes.config;

import com.reon.mcppersonalnotes.service.impl.NoteServiceImpl;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider toolCallBackProvider(NoteServiceImpl noteService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(noteService)
                .build();
    }
}
