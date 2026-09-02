package com.example.aijobagent.service;

import com.example.aijobagent.dto.JobRequest;
import com.example.aijobagent.dto.JobResponse;
import com.example.aijobagent.dto.PageResponse;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job testJob;

    @BeforeEach
    void setUp() {
        testJob = new Job();
        testJob.setId(1L);
        testJob.setTitle("Java Developer");
        testJob.setCompany("Acme Inc");
        testJob.setLocation("Pune");
        testJob.setTechnology("Java, Spring Boot");
        testJob.setRequiredSkills("Java, Spring Boot, SQL");
        testJob.setSalary("15 LPA");
    }

    @Test
    void testGetJobById_Success() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));

        JobResponse response = jobService.getJobById(1L);

        assertNotNull(response);
        assertEquals("Java Developer", response.title());
        assertEquals("Pune", response.location());
    }

    @Test
    void testSearchJobs_Pagination() {
        when(jobRepository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndTechnologyContainingIgnoreCase(
                anyString(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testJob)));

        PageResponse<JobResponse> pageResponse = jobService.searchJobs(0, 10, "Java", "Pune", null);

        assertNotNull(pageResponse);
        assertEquals(1, pageResponse.totalElements());
        assertEquals("Java Developer", pageResponse.content().get(0).title());
    }

    @Test
    void testCreateJob_Success() {
        JobRequest request = new JobRequest("Java Developer", "Acme Inc", "Pune", "Java", "Java, SQL", "Full-time", "2 yrs", "15 LPA", "Description");

        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        JobResponse response = jobService.createJob(request);

        assertNotNull(response);
        verify(jobRepository, times(1)).save(any(Job.class));
    }
}
