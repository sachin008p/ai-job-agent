package com.example.aijobagent.controller;

import com.example.aijobagent.dto.SavedJobRequest;
import com.example.aijobagent.dto.SavedJobResponse;
import com.example.aijobagent.service.SavedJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @PostMapping
    public ResponseEntity<SavedJobResponse> saveJob(@RequestBody SavedJobRequest request, 
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        SavedJobResponse response = savedJobService.saveJob(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SavedJobResponse>> getSavedJobs(@AuthenticationPrincipal UserDetails userDetails) {
        List<SavedJobResponse> jobs = savedJobService.getSavedJobs(userDetails.getUsername());
        return ResponseEntity.ok(jobs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsaveJob(@PathVariable Long id, 
                                          @AuthenticationPrincipal UserDetails userDetails) {
        savedJobService.unsaveJob(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
