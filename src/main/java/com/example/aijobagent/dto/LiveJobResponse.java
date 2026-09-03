package com.example.aijobagent.dto;

public record LiveJobResponse(
        String id,
        String title,
        String company,
        String location,
        String description,
        String url,
        String salary,
        String jobType
) {
}
