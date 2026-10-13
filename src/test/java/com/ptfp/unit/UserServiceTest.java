package com.ptfp.unit;

import com.ptfp.dto.UserLoginRequest;
import com.ptfp.dto.UserRegisterRequest;
import com.ptfp.dto.UserResponse;
import com.ptfp.model.User;
import com.ptfp.repository.UserRepository;
import com.ptfp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void testRegister_Success() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFullName("John Commuter");
        request.setEmail("john@example.com");
        request.setPassword("securepass");
        request.setPhone("9876543210");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName("John Commuter");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("securepass");
        savedUser.setPhone("9876543210");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Commuter", response.getFullName());
        assertEquals("john@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateEmail_ThrowsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFullName("Duplicate User");
        request.setEmail("duplicate@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(request);
        });

        assertTrue(ex.getMessage().contains("already exists"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("securepass");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFullName("John Commuter");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("securepass");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        UserResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("John Commuter", response.getFullName());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("pass");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(request);
        });

        assertTrue(ex.getMessage().contains("No account found"));
    }

    @Test
    void testLogin_WrongPassword_ThrowsException() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrongpass");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFullName("John Commuter");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("correctpass");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(request);
        });

        assertTrue(ex.getMessage().contains("Invalid password"));
    }
}
