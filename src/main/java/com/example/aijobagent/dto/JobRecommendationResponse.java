package com.example.aijobagent.dto;

public record JobRecommendationResponse(
        Long jobId,
        String title,
        String company,
        String location,
        String requiredSkills,
        String salary,
        String jobType,
        double matchPercentage,
        String reason
) {
}
