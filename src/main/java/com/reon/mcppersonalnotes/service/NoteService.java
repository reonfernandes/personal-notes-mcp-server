package com.reon.mcppersonalnotes.service;

import com.reon.mcppersonalnotes.dto.CreateNote;
import com.reon.mcppersonalnotes.dto.NoteResponse;
import com.reon.mcppersonalnotes.dto.UpdateNote;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {
    NoteResponse createNote(@NotNull(message = "note is required") @Valid CreateNote createNote);
    NoteResponse getNoteById(Long id);
    Page<NoteResponse> getAllNotes(Pageable pageable);
    NoteResponse updateNote(Long id, @NotNull(message = "update is required") @Valid UpdateNote updateNote);
    void deleteNote(Long id);

    List<NoteResponse> searchNotes(String query);
    List<NoteResponse> getNotesByTag(String tag);
    List<NoteResponse> getRecentNotes(int limit);
    long countNotes();
}
