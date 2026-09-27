package com.reon.mcppersonalnotes.mapper;

import com.reon.mcppersonalnotes.dto.CreateNote;
import com.reon.mcppersonalnotes.dto.NoteResponse;
import com.reon.mcppersonalnotes.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public Note mapToEntity(CreateNote notes) {
        return Note.builder()
                .title(notes.title())
                .content(notes.content())
                .tags(notes.tags())
                .build();
    }

    public NoteResponse responseToUser(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .tags(note.getTags())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
