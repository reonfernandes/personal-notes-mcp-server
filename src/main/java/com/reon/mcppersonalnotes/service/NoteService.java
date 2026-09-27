package com.reon.mcppersonalnotes.service;

import com.reon.mcppersonalnotes.dto.CreateNote;
import com.reon.mcppersonalnotes.dto.NoteResponse;
import com.reon.mcppersonalnotes.dto.UpdateNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    NoteResponse createNote(CreateNote createNote);
    NoteResponse getNoteById(Long id);
    Page<NoteResponse> getAllNotes(Pageable pageable);
    NoteResponse updateNote(Long id, UpdateNote updateNote);
    void deleteNote(Long id);
}
