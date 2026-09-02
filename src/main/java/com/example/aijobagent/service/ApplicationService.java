package com.example.aijobagent.service;

import com.example.aijobagent.dto.ApplicationRequest;
import com.example.aijobagent.dto.ApplicationResponse;
import com.example.aijobagent.exception.ResourceNotFoundException;
import com.example.aijobagent.model.Application;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobService jobService;

    public ApplicationService(ApplicationRepository applicationRepository, JobService jobService) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
    }

    @Transactional
    public ApplicationResponse applyForJob(ApplicationRequest request, User user) {
        if (applicationRepository.existsByUserIdAndJobId(user.getId(), request.jobId())) {
            throw new IllegalArgumentException("You have already applied for this job posting.");
        }

        Job job = jobService.getJobEntityById(request.jobId());

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");

        Application saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(User user) {
        return applicationRepository.findByUserIdOrderByAppliedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long id, User user) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(user.getId()) && !"ROLE_ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Access denied to application details.");
        }

        return toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(Long id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        application.setStatus(status.toUpperCase());
        Application saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    private ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getJob().getCompany(),
                application.getJob().getLocation(),
                application.getUser().getId(),
                application.getUser().getName(),
                application.getUser().getEmail(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}
