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
@Controller
public class FrontendController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
package com.iluilu.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
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
     * Simulate social login/authentication.
     */
    @GetMapping("/login")
    public String login(@RequestParam String email) {
        String userCheckQuery = "SELECT * FROM Users WHERE email = ?";
        Map<String, Object> user = null;

        try {
            user = jdbcTemplate.queryForMap(userCheckQuery, email);
        } catch (Exception e) {
            user = null;
        }

        if (user == null) {
            jdbcTemplate.update("INSERT INTO Users (email) VALUES (?)", email);
            return "Welcome, new user! You are signed up with email: " + email;
        }
        return "Welcome back! You are logged in with email: " + email;
    }

    /**
     * Publish a new ride.
     */
    @PostMapping("/rides/publish")
    public String publishRide(@RequestBody Map<String, Object> rideDetails) {
        String userEmail = (String) rideDetails.get("email");
        String fromLocation = (String) rideDetails.get("fromLocation");
        String toLocation = (String) rideDetails.get("toLocation");
        String rideDate = (String) rideDetails.get("rideDate");
        Integer seatsAvailable = (Integer) rideDetails.get("seatsAvailable");
        Double price = (Double) rideDetails.get("price");

        String userCheckQuery = "SELECT id FROM Users WHERE email = ?";
        Integer userId = jdbcTemplate.queryForObject(userCheckQuery, new Object[]{userEmail}, Integer.class);

        jdbcTemplate.update(
                "INSERT INTO Rides (user_id, from_location, to_location, ride_date, seats_available, price) VALUES (?, ?, ?, ?, ?, ?)",
                userId, fromLocation, toLocation, rideDate, seatsAvailable, price
        );

        return "Ride successfully published.";
    }

    /**
     * Get all available rides.
     */
    @GetMapping("/rides")
    public List<Map<String, Object>> getAllRides() {
        return jdbcTemplate.queryForList("SELECT * FROM Rides");
    }

    /**
     * Book a ride.
     */
    @PostMapping("/rides/book")
    public String bookRide(@RequestBody Map<String, Object> bookingDetails) {
        String userEmail = (String) bookingDetails.get("email");
        Integer rideId = (Integer) bookingDetails.get("rideId");

        String userCheckQuery = "SELECT id FROM Users WHERE email = ?";
        Integer userId = jdbcTemplate.queryForObject(userCheckQuery, new Object[]{userEmail}, Integer.class);

        jdbcTemplate.update(
                "INSERT INTO Bookings (ride_id, user_id) VALUES (?, ?)",
                rideId, userId
        );

        return "Ride successfully booked.";
    }
}
