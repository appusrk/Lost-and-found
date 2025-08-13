package com.example.lostandfound.controller;

import com.example.lostandfound.model.Found_items;
import com.example.lostandfound.repository.FoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/found")
public class FoundItemController {

    @Autowired
    private FoundItemRepository foundItemRepository;

    // Get all found items
    @GetMapping
    public List<Found_items> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    // Get found item by ID
    @GetMapping("/{id}")
    public Optional<Found_items> getFoundItemById(@PathVariable int id) {
        return foundItemRepository.findById(id);
    }

    // Add a found item
    @PostMapping
    public Found_items addFoundItem(@RequestBody Found_items foundItem) {
        return foundItemRepository.save(foundItem);
    }

    // Delete found item
    @DeleteMapping("/{id}")
    public void deleteFoundItem(@PathVariable int id) {
        foundItemRepository.deleteById(id);
    }
}
