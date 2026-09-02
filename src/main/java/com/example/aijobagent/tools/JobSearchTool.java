package com.example.aijobagent.tools;

import com.example.aijobagent.model.Job;
import com.example.aijobagent.repository.JobRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class JobSearchTool {

    private final JobRepository jobRepository;

    public JobSearchTool(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Tool(description = "Search jobs in the database by technology and location. Use this before answering job search questions.")
    @Transactional(readOnly = true)
    public List<JobToolResult> searchJobs(
            @ToolParam(description = "Technology, programming language, framework, or role keyword", required = false) String technology,
            @ToolParam(description = "City or location", required = false) String location) {
        return jobRepository.findByTechnologyContainingIgnoreCaseAndLocationContainingIgnoreCase(
                        safeValue(technology),
                        safeValue(location)
                )
                .stream()
                .map(this::toToolResult)
                .toList();
    }

    private String safeValue(String value) {
        return value == null ? "" : value.trim();
    }

    private JobToolResult toToolResult(Job job) {
        return new JobToolResult(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getTechnology(),
                job.getExperience(),
                job.getSalary(),
                job.getDescription()
        );
    }

    public record JobToolResult(
            Long id,
            String title,
            String company,
            String location,
            String technology,
            String experience,
            String salary,
            String description
    ) {
    }
}
