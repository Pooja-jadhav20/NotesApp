package com.example.notes.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notes")
public class Note {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public Boolean getIsPublic() {
	return isPublic;
}
public void setIsPublic(Boolean isPublic) {
	this.isPublic = isPublic;
}
public String getShareToken() {
	return shareToken;
}
public void setShareToken(String shareToken) {
	this.shareToken = shareToken;
}
public Instant getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
}
public Instant getUpdatedAt() {
	return updatedAt;
}
public void setUpdatedAt(Instant updatedAt) {
	this.updatedAt = updatedAt;
}
public Long getVersion() {
	return version;
}
public void setVersion(Long version) {
	this.version = version;
}
@ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  private Boolean isPublic = false;

  @Column(unique = true)
  private String shareToken;

  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();

  @Version
  private Long version;

  @PrePersist
  void prePersist() {
    createdAt = Instant.now();
    updatedAt = createdAt;
  }
  @PreUpdate
  void preUpdate() {
    updatedAt = Instant.now();
  }

  // getters and setters
}
