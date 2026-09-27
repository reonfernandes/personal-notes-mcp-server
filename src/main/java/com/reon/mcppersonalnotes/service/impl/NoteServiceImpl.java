package com.reon.mcppersonalnotes.service.impl;

import com.reon.mcppersonalnotes.dto.CreateNote;
import com.reon.mcppersonalnotes.dto.NoteResponse;
import com.reon.mcppersonalnotes.dto.UpdateNote;
import com.reon.mcppersonalnotes.entity.Note;
import com.reon.mcppersonalnotes.exception.NoteNotFoundException;
import com.reon.mcppersonalnotes.mapper.NoteMapper;
import com.reon.mcppersonalnotes.repository.NoteRepository;
import com.reon.mcppersonalnotes.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    @Tool(name = "addNote", description = "Add a new note to database")
    public NoteResponse createNote(@ToolParam(description = "Title, content and tags of the new note") CreateNote createNote) {
        log.info("Processing new note creation:................");
        Note note = noteMapper.mapToEntity(createNote);
        return noteMapper.responseToUser(noteRepository.save(note));
    }

    @Override
    @Tool(name = "getNoteById", description = "Fetch a single note by its id")
    public NoteResponse getNoteById(@ToolParam(description = "Id of the note to fetch") Long id) {
        log.info("Fetching note by id:..........");
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        return noteMapper.responseToUser(note);
    }

    @Override
    public Page<NoteResponse> getAllNotes(Pageable pageable) {
        log.info("Fetching all notes, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Note> notes = noteRepository.findAll(pageable);
        return notes.map(noteMapper::responseToUser);
    }

    // Pageable can't be built by an LLM, so the tool takes plain page/size and delegates
    @Tool(name = "getAllNotes", description = "List notes page by page, newest first. Page numbers start at 0")
    public List<NoteResponse> listNotes(
            @ToolParam(description = "Page number, starting at 0") int page,
            @ToolParam(description = "Number of notes per page") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return getAllNotes(pageable).getContent();
    }

    @Override
    @Tool(name = "updateNote", description = "Update an existing note. Only the fields provided are changed; omitted fields keep their current value")
    public NoteResponse updateNote(
            @ToolParam(description = "Id of the note to update") Long id,
            @ToolParam(description = "New values for title, content and/or tags") UpdateNote updateNote) {
        log.info("Updating note with id: {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));

        // Only overwrite the fields the caller actually sent
        if (updateNote.title() != null) {
            note.setTitle(updateNote.title());
        }
        if (updateNote.content() != null) {
            note.setContent(updateNote.content());
        }
        if (updateNote.tags() != null) {
            note.setTags(updateNote.tags());
        }

        return noteMapper.responseToUser(noteRepository.save(note));
    }

    @Override
    @Tool(name = "deleteNote", description = "Permanently delete a note by its id")
    public void deleteNote(@ToolParam(description = "Id of the note to delete") Long id) {
        log.info("Deleting note with id: {}", id);
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }
}
