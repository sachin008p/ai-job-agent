package com.example.aijobagent.controller;

import com.example.aijobagent.dto.ResumeAnalysisResponse;
import com.example.aijobagent.dto.ResumeResponse;
import com.example.aijobagent.model.User;
import com.example.aijobagent.service.AuthService;
import com.example.aijobagent.service.ResumeAnalysisService;
import com.example.aijobagent.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final AuthService authService;

    public ResumeController(ResumeService resumeService,
                            ResumeAnalysisService resumeAnalysisService,
                            AuthService authService) {
        this.resumeService = resumeService;
        this.resumeAnalysisService = resumeAnalysisService;
        this.authService = authService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(resumeService.uploadResume(file, user));
    }

    @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalysisResponse> analyzeResume(Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(resumeAnalysisService.analyzeResume(user));
    }

    @GetMapping("/me")
    public ResponseEntity<ResumeResponse> getMyResume(Authentication authentication) {
        User user = authService.getCurrentUserByEmail(authentication.getName());
        return ResponseEntity.ok(resumeService.getResumeForUser(user));
    }
}
