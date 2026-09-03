package com.example.aijobagent.controller;

import com.example.aijobagent.dto.JobMatchResponse;
import com.example.aijobagent.dto.JobRecommendationResponse;
import com.example.aijobagent.dto.JobRequest;
import com.example.aijobagent.dto.JobResponse;
import com.example.aijobagent.dto.LiveJobResponse;
import com.example.aijobagent.dto.PageResponse;
import com.example.aijobagent.model.User;
import com.example.aijobagent.service.AuthService;
import com.example.aijobagent.service.JobMatchingService;
import com.example.aijobagent.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final JobMatchingService jobMatchingService;
    private final AuthService authService;
    private final com.example.aijobagent.service.LiveJobService liveJobService;

    public JobController(JobService jobService, JobMatchingService jobMatchingService, AuthService authService,
                         com.example.aijobagent.service.LiveJobService liveJobService) {
        this.jobService = jobService;
        this.jobMatchingService = jobMatchingService;
        this.authService = authService;
        this.liveJobService = liveJobService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> searchJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills) {
        return ResponseEntity.ok(jobService.searchJobs(page, size, title, location, skills));
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/live")
    public ResponseEntity<List<LiveJobResponse>> searchLiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String location) {
        if (page < 0 || size < 1 || size > 50) {
            throw new IllegalArgumentException("Page must be non-negative and size must be between 1 and 50.");
        }
        return ResponseEntity.ok(liveJobService.search(query, location, page, size));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<JobRecommendationResponse>> getRecommendations(Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(jobMatchingService.getRecommendations(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/{id}/match")
    public ResponseEntity<JobMatchResponse> matchJob(@PathVariable Long id, Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(jobMatchingService.matchJob(id, user));
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
