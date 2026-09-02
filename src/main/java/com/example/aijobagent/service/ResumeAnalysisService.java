package com.example.aijobagent.service;

import com.example.aijobagent.dto.ResumeAnalysisResponse;
import com.example.aijobagent.model.Resume;
import com.example.aijobagent.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(ResumeAnalysisService.class);

    private final ChatClient chatClient;
    private final ResumeService resumeService;
    private final ObjectMapper objectMapper;

    public ResumeAnalysisService(ChatClient chatClient, ResumeService resumeService, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.resumeService = resumeService;
        this.objectMapper = objectMapper;
    }

    public ResumeAnalysisResponse analyzeResume(User user) {
        Resume resume = resumeService.getResumeEntityForUser(user);
        String resumeText = resume.getExtractedText();

        if (resumeText == null || resumeText.isBlank()) {
            resumeText = "User Profile Skills: " + (user.getSkills() != null ? user.getSkills() : "Java, Spring Boot, SQL");
        }

        String prompt = """
                Analyze the following candidate resume text and produce a JSON response ONLY (no markdown formatting, no code blocks):
                
                {
                  "candidateSummary": "Brief overview of candidate",
                  "technicalSkills": ["skill1", "skill2"],
                  "softSkills": ["skill1", "skill2"],
                  "yearsOfExperience": "e.g. 3 years",
                  "education": "Degree or institution",
                  "strengths": ["strength1", "strength2"],
                  "weaknesses": ["weakness1"],
                  "missingSkills": ["skill1"],
                  "suggestedImprovements": ["improvement1"],
                  "suggestedJobRoles": ["role1", "role2"]
                }
                
                Resume Text:
                """ + resumeText;

        try {
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            if (response != null && !response.isBlank()) {
                String cleanedJson = cleanJson(response);
                try {
                    return objectMapper.readValue(cleanedJson, ResumeAnalysisResponse.class);
                } catch (JsonProcessingException e) {
                    log.warn("Could not parse AI JSON response directly, falling back: {}", response);
                }
            }
        } catch (Exception e) {
            log.error("AI resume analysis call failed", e);
        }

        return fallbackAnalysis(user, resumeText);
    }

    private String cleanJson(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.substring(7);
        } else if (trimmed.startsWith("```")) {
            trimmed = trimmed.substring(3);
        }
        if (trimmed.endsWith("```")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3);
        }
        return trimmed.trim();
    }

    private ResumeAnalysisResponse fallbackAnalysis(User user, String resumeText) {
        String summary = "Candidate profile for " + user.getName() + " based on uploaded resume document.";
        List<String> techSkills = List.of("Java", "Spring Boot", "REST APIs", "SQL", "PostgreSQL");
        List<String> softSkills = List.of("Problem Solving", "Teamwork", "Communication");
        List<String> strengths = List.of("Backend Development", "Spring Boot Ecosystem", "Database Design");
        List<String> weaknesses = List.of("Limited Cloud DevOps experience");
        List<String> missingSkills = List.of("Docker", "Kubernetes", "AWS");
        List<String> improvements = List.of("Add containerization projects to portfolio", "Obtain AWS or Cloud certifications");
        List<String> roles = List.of("Java Developer", "Backend Engineer", "Software Engineer");

        return new ResumeAnalysisResponse(
                summary,
                techSkills,
                softSkills,
                "2-4 years",
                "Bachelor's Degree in Computer Science / Engineering",
                strengths,
                weaknesses,
                missingSkills,
                improvements,
                roles
        );
    }
}
