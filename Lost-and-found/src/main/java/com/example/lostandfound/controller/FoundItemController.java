package com.example.lostandfound.controller;

import com.example.lostandfound.model.Found_items;
import com.example.lostandfound.model.Users;
import com.example.lostandfound.repository.FoundItemRepository;
import com.example.lostandfound.repository.UserRepository;
import com.example.lostandfound.services.MatchingServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/api/found")
public class FoundItemController {

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
private MatchingServices matchingServices;

    private final Path uploadDir = Paths.get("uploads");
    

    @PostMapping
    public ResponseEntity<?> addFoundItem(
            @RequestParam("itemName") String itemName,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
           
            @RequestParam("email") String email,
            @RequestParam("usn") String usn, // USN sent from frontend
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Found_items foundItem = new Found_items();
            foundItem.setItemName(itemName);
            foundItem.setDescription(description);
            foundItem.setLocation(location);
            
            foundItem.setEmail(email);

            // Fetch the user by USN
            Users currentUser = userRepository.findByUsn(usn);
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User with USN " + usn + " not found");
            }
            foundItem.setUser(currentUser);

            // Handle image upload
            if (image != null && !image.isEmpty()) {
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = uploadDir.resolve(filename);
                Files.write(filePath, image.getBytes());
                foundItem.setImageUrl(filePath.toString());

                // SHA-256 hash
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = digest.digest(image.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) {
                    sb.append(String.format("%02x", b));
                }
                foundItem.setImageHash(sb.toString());
            }

            foundItemRepository.save(foundItem);
            matchingServices.findMatchesForFound(foundItem);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Found item added successfully");
            response.put("foundItem", foundItem);
          
            return ResponseEntity.ok(response);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving found item: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Found_items>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemRepository.findAll());
    }
    @GetMapping("/user/{usn}")
    public ResponseEntity<List<Found_items>> getLostItemsByUser(@PathVariable String usn) {
        List<Found_items> userLostItems = foundItemRepository.findByUser_Usn(usn);
        return ResponseEntity.ok(userLostItems);
    }

}
