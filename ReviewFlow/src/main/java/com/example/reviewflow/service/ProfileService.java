package com.example.reviewflow.service;

import com.example.reviewflow.model.User;
import com.example.reviewflow.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public User updateProfile(Authentication authentication, String fullName) {
        User user = getCurrentUser(authentication);
        user.setFullName(fullName);
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }
}
