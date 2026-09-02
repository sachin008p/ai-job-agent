package com.example.aijobagent.dto;

import java.util.List;

public record JobMatchResponse(
        Long jobId,
        String jobTitle,
        String company,
        double matchPercentage,
        List<String> matchedSkills,
        List<String> missingSkills,
        String experienceMatch,
        String recommendation,
        String explanation
) {
}
