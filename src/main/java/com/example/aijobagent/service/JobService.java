package com.example.aijobagent.service;

import com.example.aijobagent.dto.JobRequest;
import com.example.aijobagent.dto.JobResponse;
import com.example.aijobagent.exception.ResourceNotFoundException;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.repository.JobRepository;
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
    public JobResponse getJobById(Long id) {
        return toResponse(findJob(id));
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
                job.getExperience(),
                job.getSalary(),
                job.getDescription()
        );
    }
}
