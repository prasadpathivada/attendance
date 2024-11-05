package com.example.attendance.controller;

import com.google.gson.Gson;
import com.example.attendance.Util.JwtUtil;

import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        System.out.println("Received login request for email: " + user.getEmail());
        // find user by email
        User foundUser = userRepository.findByEmail(user.getEmail());
        // if user is not found
        if (foundUser == null) {
            System.out.println("User not found for email: " + user.getEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        // Log stored and entered passwords for debugging
        System.out.println("Stored password hash: " + foundUser.getPassword());
        System.out.println("Entered password: " + user.getPassword());

        // Generate JWT token
        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            System.out.println("Password matches!");

            String token = jwtUtil.generateToken(foundUser.getEmail());

            // create response payload

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            Gson gson = new Gson();
            // Convert Map to JSON
            String json = gson.toJson(response);
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok().body(json);
        } else {
            System.out.println("Passwords do not match. ");
            return ResponseEntity.status(401).body("Invalid credentials");

        }

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Check if the username already exists
        System.out.println("Registering user: " + user);

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

        // Hash password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        System.out.println("Hashed password: " + hashedPassword);

        user.setPassword(hashedPassword);

        // Save the new user to the database
        userRepository.save(user);
        return ResponseEntity.ok("Registration successful");
    }

    // @SuppressWarnings("unused")
    // private Map<String, Object>createResponse(String message, String token){
    // Map<String, Object> response = new HashMap<>();
    // response.put("message", message);
    // response.put("token", token);
    // return response;

    // }
}
