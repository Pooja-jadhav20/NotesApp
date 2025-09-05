package com.example.notes.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notes.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
}
