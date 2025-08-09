package com.example.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lostandfound.model.Match_history;

public interface MatchingHistoryRepository extends JpaRepository<Match_history, Integer> {

}
