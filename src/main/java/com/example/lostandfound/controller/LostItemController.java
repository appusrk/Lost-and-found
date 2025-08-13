package com.example.lostandfound.controller;

import com.example.lostandfound.model.Lost_items;
import com.example.lostandfound.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lost")
public class LostItemController {

    @Autowired
    private LostItemRepository lostItemRepository;

    // Get all lost items
    @GetMapping
    public List<Lost_items> getAllLostItems() {
        return lostItemRepository.findAll();
    }

    // Get lost item by ID
    @GetMapping("/{id}")
    public Optional<Lost_items> getLostItemById(@PathVariable int id) {
        return lostItemRepository.findById(id);
    }

    // Add a lost item
    @PostMapping
    public Lost_items addLostItem(@RequestBody Lost_items lostItem) {
        return lostItemRepository.save(lostItem);
    }

    // Delete lost item
    @DeleteMapping("/{id}")
    public void deleteLostItem(@PathVariable int id) {
        lostItemRepository.deleteById(id);
    }
}
