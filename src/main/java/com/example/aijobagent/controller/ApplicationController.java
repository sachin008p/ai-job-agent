package com.example.aijobagent.controller;

import com.example.aijobagent.dto.ApplicationRequest;
import com.example.aijobagent.dto.ApplicationResponse;
import com.example.aijobagent.dto.ApplicationStatusUpdateRequest;
import com.example.aijobagent.model.User;
import com.example.aijobagent.service.ApplicationService;
import com.example.aijobagent.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final AuthService authService;

    public ApplicationController(ApplicationService applicationService, AuthService authService) {
        this.applicationService = applicationService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> applyForJob(@Valid @RequestBody ApplicationRequest request,
                                                           Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.applyForJob(request, user));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(applicationService.getMyApplications(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id,
                                                                   Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(applicationService.getApplicationById(id, user));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(@PathVariable Long id,
                                                                       @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(id, request.status()));
    }
}
