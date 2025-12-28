package com.example.lostandfound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lostandfound.model.Issues;
import com.example.lostandfound.model.Lost_items;

	public interface IssueRepository extends JpaRepository<Issues, Integer> {
		List<Issues> findAll();
		List<Issues> findByIssueDept(String issueDept);


	}


