package com.projectmgt_monolith.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectmgt_monolith.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByName(String name);
	
	Optional<User> findByName(String name);
}
