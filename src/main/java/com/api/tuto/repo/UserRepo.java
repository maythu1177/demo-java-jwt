package com.api.tuto.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.tuto.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
}
