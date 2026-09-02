package com.example.aijobagent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Company is required")
        String company,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Technology is required")
        String technology,

        String requiredSkills,

        String jobType,

        String experience,

        String salary,

        @Size(max = 4000, message = "Description must be 4000 characters or fewer")
        String description
) {
}
