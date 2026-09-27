package com.reon.mcppersonalnotes.controller;

import com.reon.mcppersonalnotes.dto.CreateNote;
import com.reon.mcppersonalnotes.dto.NoteResponse;
import com.reon.mcppersonalnotes.dto.UpdateNote;
import com.reon.mcppersonalnotes.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/note")
@Slf4j
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody CreateNote createNote) {
        log.info("REST request to create note");
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(createNote));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id) {
        log.info("REST request to fetch note: {}", id);
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping
    public ResponseEntity<Page<NoteResponse>> getAllNotes(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("REST request to fetch all notes");
        return ResponseEntity.ok(noteService.getAllNotes(pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id, @Valid @RequestBody UpdateNote updateNote) {
        log.info("REST request to update note: {}", id);
        return ResponseEntity.ok(noteService.updateNote(id, updateNote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        log.info("REST request to delete note: {}", id);
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
