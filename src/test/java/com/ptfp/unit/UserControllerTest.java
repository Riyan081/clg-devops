package com.ptfp.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptfp.controller.UserController;
import com.ptfp.dto.UserLoginRequest;
import com.ptfp.dto.UserRegisterRequest;
import com.ptfp.dto.UserResponse;
import com.ptfp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testRegister_Success_Returns201() throws Exception {
        UserRegisterRequest req = new UserRegisterRequest();
        req.setFullName("Alice Commuter");
        req.setEmail("alice@example.com");
        req.setPassword("pass1234");

        UserResponse res = new UserResponse(1L, "Alice Commuter", "alice@example.com", null, "Registration successful!");
        when(userService.register(any(UserRegisterRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Alice Commuter"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testRegister_DuplicateEmail_Returns400() throws Exception {
        UserRegisterRequest req = new UserRegisterRequest();
        req.setFullName("Alice Commuter");
        req.setEmail("alice@example.com");
        req.setPassword("pass1234");

        when(userService.register(any(UserRegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("An account with this email already exists."));

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("An account with this email already exists."));
    }

    @Test
    void testLogin_Success_Returns200() throws Exception {
        UserLoginRequest req = new UserLoginRequest();
        req.setEmail("alice@example.com");
        req.setPassword("pass1234");

        UserResponse res = new UserResponse(1L, "Alice Commuter", "alice@example.com", null, "Login successful!");
        when(userService.login(any(UserLoginRequest.class))).thenReturn(res);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Alice Commuter"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testLogin_InvalidCredentials_Returns400() throws Exception {
        UserLoginRequest req = new UserLoginRequest();
        req.setEmail("alice@example.com");
        req.setPassword("wrongpass");

        when(userService.login(any(UserLoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid password. Please try again."));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid password. Please try again."));
    }
}
