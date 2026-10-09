package com.example.aijobagent.service;

import com.example.aijobagent.dto.SavedJobRequest;
import com.example.aijobagent.dto.SavedJobResponse;
import com.example.aijobagent.exception.ResourceNotFoundException;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.model.SavedJob;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.JobRepository;
import com.example.aijobagent.repository.SavedJobRepository;
import com.example.aijobagent.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public SavedJobService(SavedJobRepository savedJobRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.savedJobRepository = savedJobRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional
    public SavedJobResponse saveJob(String email, SavedJobRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getJobId() != null) {
            // Internal job
            Job job = jobRepository.findById(request.getJobId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
            
            // Check if already saved
            Optional<SavedJob> existing = savedJobRepository.findByUserAndJobId(user, job.getId());
            if (existing.isPresent()) {
                return mapToResponse(existing.get());
            }

            SavedJob savedJob = new SavedJob();
            savedJob.setUser(user);
            savedJob.setJob(job);
            return mapToResponse(savedJobRepository.save(savedJob));
        } else if (request.getExternalJobId() != null) {
            // External job
            Optional<SavedJob> existing = savedJobRepository.findByUserAndExternalJobId(user, request.getExternalJobId());
            if (existing.isPresent()) {
                return mapToResponse(existing.get());
            }

            SavedJob savedJob = new SavedJob();
            savedJob.setUser(user);
            savedJob.setExternalJobId(request.getExternalJobId());
            savedJob.setExternalJobTitle(request.getExternalJobTitle());
            savedJob.setExternalCompany(request.getExternalCompany());
            savedJob.setExternalLocation(request.getExternalLocation());
            savedJob.setExternalUrl(request.getExternalUrl());
            return mapToResponse(savedJobRepository.save(savedJob));
        } else {
            throw new IllegalArgumentException("Either jobId or externalJobId must be provided");
        }
    }

    @Transactional(readOnly = true)
    public List<SavedJobResponse> getSavedJobs(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return savedJobRepository.findByUserOrderBySavedAtDesc(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unsaveJob(String email, Long savedJobId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SavedJob savedJob = savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));

        if (!savedJob.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to remove this saved job");
        }

        savedJobRepository.delete(savedJob);
    }

    private SavedJobResponse mapToResponse(SavedJob savedJob) {
        SavedJobResponse response = new SavedJobResponse();
        response.setId(savedJob.getId());
        response.setSavedAt(savedJob.getSavedAt());

        if (savedJob.getJob() != null) {
            response.setJobId(savedJob.getJob().getId());
            response.setTitle(savedJob.getJob().getTitle());
            response.setCompany(savedJob.getJob().getCompany());
            response.setLocation(savedJob.getJob().getLocation());
        } else {
            response.setTitle(savedJob.getExternalJobTitle());
            response.setCompany(savedJob.getExternalCompany());
            response.setLocation(savedJob.getExternalLocation());
            response.setExternalUrl(savedJob.getExternalUrl());
        }

        return response;
    }
}
