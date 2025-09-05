package com.example.notes.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.Repositories.UserRepository;
import com.example.notes.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private UserRepository userRepository;
  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @Value("${app.jwt.secret:secret123456789012345678901234567890}")
  private String jwtSecret;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
    String username = body.get("username");
    String email = body.get("email");
    String password = body.get("password");

    if (userRepository.findByUsername(username).isPresent())
      return ResponseEntity.status(409).body("username taken");

    if (userRepository.findByEmail(email).isPresent())
      return ResponseEntity.status(409).body("email taken");

    User u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setPasswordHash(passwordEncoder.encode(password));
    userRepository.save(u);

    Map<String,Object> resp = new HashMap<>();
    resp.put("id", u.getId());
    resp.put("username", u.getUsername());
    resp.put("email", u.getEmail());
    return ResponseEntity.status(201).body(resp);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
    String usernameOrEmail = body.get("usernameOrEmail");
    String password = body.get("password");

    User u = userRepository.findByUsername(usernameOrEmail)
            .orElseGet(() -> userRepository.findByEmail(usernameOrEmail).orElse(null));

    if (u == null || !passwordEncoder.matches(password, u.getPasswordHash())) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    String token = Jwts.builder()
        .setSubject(u.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1h expiry
        .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
        .compact();

    Map<String,Object> resp = new HashMap<>();
    resp.put("accessToken", token);
    resp.put("tokenType", "Bearer");
    return ResponseEntity.ok(resp);
  }
}
