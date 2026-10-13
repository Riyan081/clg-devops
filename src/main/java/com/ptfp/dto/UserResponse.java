package com.ptfp.dto;

/**
 * Response DTO returned after successful login/register.
 * Excludes password — only sends safe user info to the frontend.
 */
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String message;

    public UserResponse() {}

    public UserResponse(Long id, String fullName, String email, String phone, String message) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.message = message;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
