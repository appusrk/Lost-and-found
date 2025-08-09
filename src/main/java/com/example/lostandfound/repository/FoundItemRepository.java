package com.example.lostandfound.repository;

import com.example.lostandfound.model.Found_items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoundItemRepository extends JpaRepository<Found_items, Integer> {
    
}
