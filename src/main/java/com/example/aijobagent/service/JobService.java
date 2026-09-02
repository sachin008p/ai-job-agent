package com.example.aijobagent.service;

import com.example.aijobagent.dto.JobRequest;
import com.example.aijobagent.dto.JobResponse;
import com.example.aijobagent.dto.PageResponse;
import com.example.aijobagent.exception.ResourceNotFoundException;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<JobResponse> searchJobs(int page, int size, String title, String location, String skills) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        String safeTitle = title == null ? "" : title.trim();
        String safeLocation = location == null ? "" : location.trim();
        String safeSkills = skills == null ? "" : skills.trim();

        Page<Job> jobPage = jobRepository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndTechnologyContainingIgnoreCase(
                safeTitle, safeLocation, safeSkills, pageable);

        List<JobResponse> content = jobPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages(),
                jobPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(Long id) {
        return toResponse(findJob(id));
    }

    @Transactional(readOnly = true)
    public Job getJobEntityById(Long id) {
        return findJob(id);
    }

    @Transactional
    public JobResponse createJob(JobRequest request) {
        Job job = new Job();
        applyRequest(job, request);
        return toResponse(jobRepository.save(job));
    }

    @Transactional
    public JobResponse updateJob(Long id, JobRequest request) {
        Job job = findJob(id);
        applyRequest(job, request);
        return toResponse(jobRepository.save(job));
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = findJob(id);
        jobRepository.delete(job);
    }

    private Job findJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    private void applyRequest(Job job, JobRequest request) {
        job.setTitle(request.title());
        job.setCompany(request.company());
        job.setLocation(request.location());
        job.setTechnology(request.technology());
        job.setRequiredSkills(request.requiredSkills() != null && !request.requiredSkills().isBlank() ? request.requiredSkills() : request.technology());
        job.setJobType(request.jobType() != null && !request.jobType().isBlank() ? request.jobType() : "Full-time");
        job.setExperience(request.experience());
        job.setSalary(request.salary());
        job.setDescription(request.description());
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getTechnology(),
                job.getRequiredSkills(),
                job.getJobType(),
                job.getExperience(),
                job.getSalary(),
                job.getDescription(),
                job.getCreatedAt()
        );
    }
}
