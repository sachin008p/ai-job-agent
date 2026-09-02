package com.example.aijobagent.dto;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobTitle,
        String company,
        String location,
        Long userId,
        String userName,
        String userEmail,
        String status,
        LocalDateTime appliedAt
) {
}
