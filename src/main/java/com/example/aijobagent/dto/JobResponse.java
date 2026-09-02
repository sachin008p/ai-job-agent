package com.example.aijobagent.dto;

import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        String title,
        String company,
        String location,
        String technology,
        String requiredSkills,
        String jobType,
        String experience,
        String salary,
        String description,
        LocalDateTime createdAt
) {
}
