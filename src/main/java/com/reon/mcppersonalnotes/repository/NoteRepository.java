package com.reon.mcppersonalnotes.repository;

import com.reon.mcppersonalnotes.entity.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Sort sort);

    List<Note> findByTagsContainingIgnoreCase(String tag, Sort sort);
}
