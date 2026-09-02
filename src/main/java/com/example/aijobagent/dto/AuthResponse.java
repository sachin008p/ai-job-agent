package com.example.aijobagent.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String name,
        String email,
        String role,
        String skills
) {
    public AuthResponse(String token, Long userId, String name, String email, String role, String skills) {
        this(token, "Bearer", userId, name, email, role, skills);
    }
}
