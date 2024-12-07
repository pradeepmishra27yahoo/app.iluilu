package com.iluilu.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@SpringBootApplication
@RestController
public class Application {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Simulated social login (via email).
     */
    @GetMapping("/login")
    public String login(@RequestParam String email) {
        // Check if the user already exists
        String userCheckQuery = "SELECT * FROM Users WHERE email = ?";
        Map<String, Object> user = null;

        try {
            user = jdbcTemplate.queryForMap(userCheckQuery, email);
        } catch (Exception e) {
            user = null;
        }

        if (user == null) {
            // Insert new user
            jdbcTemplate.update("INSERT INTO Users (email) VALUES (?)", email);
            return "Welcome, new user! You are signed up with email: " + email;
        }
        return "Welcome back! You are logged in with email: " + email;
    }
}
