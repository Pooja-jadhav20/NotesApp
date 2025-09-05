package com.example.notes.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.Repositories.NoteRepository;
import com.example.notes.models.Note;

@RestController
@RequestMapping("/api/share")
public class ShareController {
  @Autowired NoteRepository noteRepo;

  @GetMapping("/{token}")
  public ResponseEntity<?> getShared(@PathVariable String token) {
    Note n = noteRepo.findByShareTokenAndIsPublicTrue(token).orElse(null);
    if (n == null) return ResponseEntity.status(404).body("not found");
    // Return limited fields
    return ResponseEntity.ok(Map.of(
      "id", n.getId(),
      "title", n.getTitle(),
      "content", n.getContent(),
      "createdAt", n.getCreatedAt(),
      "updatedAt", n.getUpdatedAt()
    ));
  }
}

