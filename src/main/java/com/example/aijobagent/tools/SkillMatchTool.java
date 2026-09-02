package com.example.aijobagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillMatchTool {

    @Tool(description = "Calculate candidate skill match percentage against required job skills.")
    public MatchResult calculateMatch(
            @ToolParam(description = "Comma-separated candidate skills") String candidateSkills,
            @ToolParam(description = "Comma-separated required job skills") String requiredSkills) {
        Set<String> candidateSkillSet = normalizeSkills(candidateSkills);
        Set<String> requiredSkillSet = normalizeSkills(requiredSkills);

        if (requiredSkillSet.isEmpty()) {
            return new MatchResult(0.0, 0, 0, "No required skills were provided.");
        }

        long matchedSkills = requiredSkillSet.stream()
                .filter(candidateSkillSet::contains)
                .count();
        double percentage = (matchedSkills * 100.0) / requiredSkillSet.size();

        String summary = matchedSkills + " of " + requiredSkillSet.size() + " required skills matched.";
        return new MatchResult(Math.round(percentage * 100.0) / 100.0, matchedSkills, requiredSkillSet.size(), summary);
    }

    private Set<String> normalizeSkills(String skills) {
        if (skills == null || skills.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(skills.split("[,;\\n]"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(skill -> !skill.isBlank())
                .collect(Collectors.toSet());
    }

    public record MatchResult(double matchPercentage, long matchedSkills, int requiredSkills, String summary) {
    }
}
