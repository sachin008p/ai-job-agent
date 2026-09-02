package com.example.aijobagent.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillMatchToolTest {

    private SkillMatchTool skillMatchTool;

    @BeforeEach
    void setUp() {
        skillMatchTool = new SkillMatchTool();
    }

    @Test
    void testCalculateMatch_FullMatch() {
        String candidateSkills = "Java, Spring Boot, PostgreSQL, Docker";
        String requiredSkills = "Java, Spring Boot, PostgreSQL";

        SkillMatchTool.MatchResult result = skillMatchTool.calculateMatch(candidateSkills, requiredSkills);

        assertEquals(100.0, result.matchPercentage());
        assertEquals(3, result.matchedSkills());
        assertEquals(3, result.requiredSkills());
    }

    @Test
    void testCalculateMatch_PartialMatch() {
        String candidateSkills = "Java, HTML, CSS";
        String requiredSkills = "Java, Spring Boot, PostgreSQL, Docker";

        SkillMatchTool.MatchResult result = skillMatchTool.calculateMatch(candidateSkills, requiredSkills);

        assertEquals(25.0, result.matchPercentage());
        assertEquals(1, result.matchedSkills());
        assertEquals(4, result.requiredSkills());
    }

    @Test
    void testCalculateMatch_EmptyRequiredSkills() {
        SkillMatchTool.MatchResult result = skillMatchTool.calculateMatch("Java", "");

        assertEquals(0.0, result.matchPercentage());
        assertEquals(0, result.matchedSkills());
    }
}
