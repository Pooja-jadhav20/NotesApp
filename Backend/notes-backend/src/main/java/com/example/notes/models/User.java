package com.example.notes.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  private Instant createdAt = Instant.now();

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPasswordHash() {
	return passwordHash;
}

public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
}

public Instant getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
}

  // getters and setters
  // (for brevity - generate them in your IDE)
}

