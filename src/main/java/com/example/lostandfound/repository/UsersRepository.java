package com.example.lostandfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lostandfound.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	

}
