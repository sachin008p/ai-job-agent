package com.example.aijobagent.service;

import com.example.aijobagent.dto.ApplicationRequest;
import com.example.aijobagent.dto.ApplicationResponse;
import com.example.aijobagent.model.Application;
import com.example.aijobagent.model.Job;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobService jobService;

    @InjectMocks
    private ApplicationService applicationService;

    private User testUser;
    private Job testJob;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testJob = new Job();
        testJob.setId(10L);
        testJob.setTitle("Java Dev");
        testJob.setCompany("Acme");
        testJob.setLocation("Pune");
    }

    @Test
    void testApplyForJob_Success() {
        ApplicationRequest request = new ApplicationRequest(10L);

        when(applicationRepository.existsByUserIdAndJobId(1L, 10L)).thenReturn(false);
        when(jobService.getJobEntityById(10L)).thenReturn(testJob);

        Application application = new Application();
        application.setId(100L);
        application.setUser(testUser);
        application.setJob(testJob);
        application.setStatus("APPLIED");

        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplicationResponse response = applicationService.applyForJob(request, testUser);

        assertNotNull(response);
        assertEquals(10L, response.jobId());
        assertEquals("APPLIED", response.status());
    }

    @Test
    void testApplyForJob_Duplicate_ThrowsException() {
        ApplicationRequest request = new ApplicationRequest(10L);

        when(applicationRepository.existsByUserIdAndJobId(1L, 10L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> applicationService.applyForJob(request, testUser));
    }
}
