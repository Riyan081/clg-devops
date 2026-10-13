package com.ptfp.service;

import com.ptfp.dto.UserLoginRequest;
import com.ptfp.dto.UserRegisterRequest;
import com.ptfp.dto.UserResponse;
import com.ptfp.model.User;
import com.ptfp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("An account with this email already exists. Please login instead.");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(request.getPassword()); // Plain text for simplicity (DevOps project)
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhone(),
                "Registration successful! You can now raise issues."
        );
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("No account found with this email. Please register first."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid password. Please try again.");
        }

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                "Login successful!"
        );
    }
}
