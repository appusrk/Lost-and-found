package com.example.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lostandfound.model.Lost_items;

public interface LostItemRepository extends JpaRepository<Lost_items, Integer>{
}
