package com.example.notes.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.Repositories.NoteRepository;
import com.example.notes.Repositories.UserRepository;
import com.example.notes.models.Note;
import com.example.notes.models.User;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

  @Autowired NoteRepository noteRepo;
  @Autowired UserRepository userRepo;

  private User getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepo.findByUsername(username).orElseThrow();
  }

  @GetMapping
  public Page<Note> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    User u = getCurrentUser();
    return noteRepo.findByUserId(u.getId(), PageRequest.of(page, size, Sort.by("updatedAt").descending()));
  }

  @PostMapping
  public ResponseEntity<Note> create(@RequestBody Map<String,String> body) {
    User u = getCurrentUser();
    Note n = new Note();
    n.setUser(u);
    n.setTitle(body.getOrDefault("title", ""));
    n.setContent(body.getOrDefault("content", ""));
    noteRepo.save(n);
    return ResponseEntity.status(201).body(n);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable Long id) {
    Note n = noteRepo.findById(id).orElse(null);
    if (n == null) return ResponseEntity.status(404).body("not found");
    if (!n.getUser().getId().equals(getCurrentUser().getId())) return ResponseEntity.status(403).body("forbidden");
    return ResponseEntity.ok(n);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String,Object> body) {
    Note n = noteRepo.findById(id).orElse(null);
    if (n == null) return ResponseEntity.status(404).body("not found");
    if (!n.getUser().getId().equals(getCurrentUser().getId())) return ResponseEntity.status(403).body("forbidden");

    // optimistic locking handled by JPA @Version; if conflict, Jpa will throw OptimisticLockException
    n.setTitle((String) body.getOrDefault("title", n.getTitle()));
    n.setContent((String) body.getOrDefault("content", n.getContent()));
    noteRepo.save(n);
    return ResponseEntity.ok(n);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    Note n = noteRepo.findById(id).orElse(null);
    if (n == null) return ResponseEntity.status(404).body("not found");
    if (!n.getUser().getId().equals(getCurrentUser().getId())) return ResponseEntity.status(403).body("forbidden");
    noteRepo.delete(n);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/share")
  public ResponseEntity<?> share(@PathVariable Long id, @RequestBody Map<String,Object> body) {
    Note n = noteRepo.findById(id).orElse(null);
    if (n == null) return ResponseEntity.status(404).body("not found");
    if (!n.getUser().getId().equals(getCurrentUser().getId())) return ResponseEntity.status(403).body("forbidden");
    boolean makePublic = Boolean.TRUE.equals(body.get("makePublic"));
    if (makePublic) {
      if (n.getShareToken() == null) n.setShareToken(UUID.randomUUID().toString());
      n.setIsPublic(true);
      noteRepo.save(n);
      Map<String,Object> resp = new HashMap<>();
      resp.put("shareToken", n.getShareToken());
      resp.put("shareUrl", body.getOrDefault("frontendBase", "") + "/share/" + n.getShareToken());
      return ResponseEntity.ok(resp);
    } else {
      n.setIsPublic(false);
      n.setShareToken(null);
      noteRepo.save(n);
      return ResponseEntity.ok(Map.of("isPublic", false));
    }
  }

}

