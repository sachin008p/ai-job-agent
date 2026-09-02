package com.example.aijobagent.dto;

import java.time.LocalDateTime;

public record ResumeResponse(
        Long id,
        String fileName,
        String fileType,
        LocalDateTime uploadedAt,
        String extractedText
) {
}
