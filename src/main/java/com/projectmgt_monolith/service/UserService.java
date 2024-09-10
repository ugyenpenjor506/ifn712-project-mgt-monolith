package com.projectmgt_monolith.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectmgt_monolith.dto.LoginDto;
import com.projectmgt_monolith.dto.UserRegistrationDto;
import com.projectmgt_monolith.model.User;
import com.projectmgt_monolith.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByName(registrationDto.getName())) {
            throw new IllegalStateException("Username already taken");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setPassword_hash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        user.setStatus("active");

        return userRepository.save(user);
    }
    
 // Login method
    public User loginUser(LoginDto loginDto) {
        User user = userRepository.findByName(loginDto.getName())
            .orElseThrow(() -> new IllegalStateException("Invalid username or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword_hash())) {
            throw new IllegalStateException("Invalid username or password");
        }

        // Update last login time
        user.setLast_login(LocalDateTime.now());
        userRepository.save(user);

        return user;  // Return user or a token based on your preference
    }
}
