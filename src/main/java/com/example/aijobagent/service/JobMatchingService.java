package com.example.aijobagent.service;

import com.example.aijobagent.dto.JobMatchResponse;
import com.example.aijobagent.dto.JobRecommendationResponse;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.model.Resume;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.JobRepository;
import com.example.aijobagent.repository.ResumeRepository;
import com.example.aijobagent.tools.SkillMatchTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobMatchingService {

    private static final Logger log = LoggerFactory.getLogger(JobMatchingService.class);

    private final JobService jobService;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final SkillMatchTool skillMatchTool;

    public JobMatchingService(JobService jobService,
                              JobRepository jobRepository,
                              ResumeRepository resumeRepository,
                              SkillMatchTool skillMatchTool) {
        this.jobService = jobService;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.skillMatchTool = skillMatchTool;
    }

    @Transactional(readOnly = true)
    public JobMatchResponse matchJob(Long jobId, User user) {
        Job job = jobService.getJobEntityById(jobId);
        String candidateSkillsText = extractCandidateSkills(user);

        String requiredSkills = job.getRequiredSkills() != null && !job.getRequiredSkills().isBlank()
                ? job.getRequiredSkills()
                : job.getTechnology();

        SkillMatchTool.MatchResult matchResult = skillMatchTool.calculateMatch(candidateSkillsText, requiredSkills);

        Set<String> candidateSkillSet = normalizeSkills(candidateSkillsText);
        Set<String> requiredSkillSet = normalizeSkills(requiredSkills);

        List<String> matchedSkills = requiredSkillSet.stream()
                .filter(candidateSkillSet::contains)
                .map(this::capitalize)
                .toList();

        List<String> missingSkills = requiredSkillSet.stream()
                .filter(skill -> !candidateSkillSet.contains(skill))
                .map(this::capitalize)
                .toList();

        double percentage = matchResult.matchPercentage();
        String recommendation = percentage >= 80 ? "Strong Match"
                : percentage >= 50 ? "Good Match"
                : "Moderate Match";

        String explanation = "Matched " + matchedSkills.size() + " of " + requiredSkillSet.size() + " required skills (" +
                (requiredSkills != null ? requiredSkills : "") + "). " +
                (missingSkills.isEmpty() ? "Candidate possesses all requested technical requirements."
                        : "Candidate would benefit from learning: " + String.join(", ", missingSkills) + ".");

        return new JobMatchResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                percentage,
                matchedSkills,
                missingSkills,
                "Matches experience requirement: " + (job.getExperience() != null ? job.getExperience() : "Not specified"),
                recommendation,
                explanation
        );
    }

    @Transactional(readOnly = true)
    public List<JobRecommendationResponse> getRecommendations(User user) {
        String candidateSkillsText = extractCandidateSkills(user);
        List<Job> allJobs = jobRepository.findAll();

        return allJobs.stream()
                .map(job -> {
                    String reqSkills = job.getRequiredSkills() != null && !job.getRequiredSkills().isBlank()
                            ? job.getRequiredSkills()
                            : job.getTechnology();

                    SkillMatchTool.MatchResult result = skillMatchTool.calculateMatch(candidateSkillsText, reqSkills);

                    String reason = "Skill match: " + result.matchPercentage() + "% (" + result.summary() + ")";

                    return new JobRecommendationResponse(
                            job.getId(),
                            job.getTitle(),
                            job.getCompany(),
                            job.getLocation(),
                            reqSkills,
                            job.getSalary(),
                            job.getJobType(),
                            result.matchPercentage(),
                            reason
                    );
                })
                .sorted(Comparator.comparingDouble(JobRecommendationResponse::matchPercentage).reversed())
                .limit(10)
                .toList();
    }

    private String extractCandidateSkills(User user) {
        Optional<Resume> resumeOpt = resumeRepository.findByUserId(user.getId());
        if (resumeOpt.isPresent() && resumeOpt.get().getExtractedText() != null && !resumeOpt.get().getExtractedText().isBlank()) {
            return resumeOpt.get().getExtractedText();
        }
        if (user.getSkills() != null && !user.getSkills().isBlank()) {
            return user.getSkills();
        }
        return "Java, Spring Boot, REST APIs, SQL, PostgreSQL, Git, Maven";
    }

    private Set<String> normalizeSkills(String skills) {
        if (skills == null || skills.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(skills.split("[,;\\n]"))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }

    private String capitalize(String text) {
        if (text == null || text.isBlank()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
