package com.example.aijobagent.dto;

import java.util.List;

public record ResumeAnalysisResponse(
        String candidateSummary,
        List<String> technicalSkills,
        List<String> softSkills,
        String yearsOfExperience,
        String education,
        List<String> strengths,
        List<String> weaknesses,
        List<String> missingSkills,
        List<String> suggestedImprovements,
        List<String> suggestedJobRoles
) {
}
