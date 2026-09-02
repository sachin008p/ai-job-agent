package com.example.aijobagent.dto;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(
        @NotNull(message = "Job ID is required")
        Long jobId
) {
}
