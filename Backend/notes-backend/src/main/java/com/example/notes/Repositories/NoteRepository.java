package com.example.notes.Repositories;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notes.models.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
  Page<Note> findByUserId(Long userId, Pageable pageable);
  Optional<Note> findByShareTokenAndIsPublicTrue(String token);
}
