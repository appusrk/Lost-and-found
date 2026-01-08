package com.example.lostandfound.controller;

import com.example.lostandfound.model.Lost_items;
import com.example.lostandfound.model.Users;
import com.example.lostandfound.repository.LostItemRepository;
import com.example.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lostandfound.services.MatchingServices;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api/lost")
public class LostItemController {

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
private MatchingServices matchingServices;

    private final Path uploadDir = Paths.get("uploads");

    @PostMapping
    public ResponseEntity<?> addLostItem(
            @RequestParam("itemName") String itemName,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
           
            @RequestParam("email") String email,
            @RequestParam("usn") String usn, // USN sent from frontend
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Lost_items lostItem = new Lost_items();
            lostItem.setItemName(itemName);
            lostItem.setDescription(description);
            lostItem.setLocation(location);
           
            lostItem.setEmail(email);

            // Fetch the user by USN
            Users currentUser = userRepository.findByUsn(usn);
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User with USN " + usn + " not found");
            }
            lostItem.setUser(currentUser);

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = uploadDir.resolve(filename);
                Files.write(filePath, image.getBytes());
                lostItem.setImageUrl(filePath.toString());

                // SHA-256 hash
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(image.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) {
                    sb.append(String.format("%02x", b));
                }
                lostItem.setImageHash(sb.toString());
            }

            lostItemRepository.save(lostItem);
            matchingServices.findMatchesForLost(lostItem);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lost item added successfully");
            response.put("lostItem", lostItem);
            return ResponseEntity.ok(response);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving lost item: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Lost_items>> getAllLostItems() {
        return ResponseEntity.ok(lostItemRepository.findAll());
    }
    @GetMapping("/user/{usn}")
    public ResponseEntity<List<Lost_items>> getLostItemsByUser(@PathVariable String usn) {
        List<Lost_items> userLostItems = lostItemRepository.findByUser_Usn(usn);
        return ResponseEntity.ok(userLostItems);
    }

}
