package com.example.aijobagent.dto;

import jakarta.validation.constraints.NotBlank;

public record AgentAskRequest(
        @NotBlank(message = "Question is required")
        String question
) {
}
