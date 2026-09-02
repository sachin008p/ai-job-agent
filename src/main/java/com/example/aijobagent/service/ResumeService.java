package com.example.aijobagent.service;

import com.example.aijobagent.dto.ResumeResponse;
import com.example.aijobagent.exception.ResourceNotFoundException;
import com.example.aijobagent.model.Resume;
import com.example.aijobagent.model.User;
import com.example.aijobagent.repository.ResumeRepository;
import com.example.aijobagent.repository.UserRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResumeResponse uploadResume(MultipartFile file, User user) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded resume file cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".pdf") &&
                (contentType != null && !contentType.equalsIgnoreCase("application/pdf")))) {
            throw new IllegalArgumentException("Only PDF files are supported for resume upload.");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Resume file size exceeds the 5MB limit.");
        }

        String extractedText;
        try {
            extractedText = extractTextFromPdf(file.getBytes());
        } catch (IOException e) {
            log.error("Failed to extract text from PDF resume", e);
            throw new IllegalArgumentException("Could not read PDF resume file: " + e.getMessage());
        }

        Optional<Resume> existingResume = resumeRepository.findByUserId(user.getId());
        Resume resume = existingResume.orElseGet(() -> {
            Resume newResume = new Resume();
            newResume.setUser(user);
            return newResume;
        });

        resume.setFileName(originalFilename);
        resume.setFileType(contentType != null ? contentType : "application/pdf");
        resume.setExtractedText(extractedText);

        Resume saved = resumeRepository.save(resume);

        if (extractedText != null && !extractedText.isBlank()) {
            if (user.getSkills() == null || user.getSkills().isBlank()) {
                user.setSkills(extractedText.substring(0, Math.min(extractedText.length(), 1000)));
                userRepository.save(user);
            }
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ResumeResponse getResumeForUser(User user) {
        Resume resume = resumeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for user: " + user.getEmail()));
        return toResponse(resume);
    }

    @Transactional(readOnly = true)
    public Resume getResumeEntityForUser(User user) {
        return resumeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No resume uploaded yet. Please upload a PDF resume first."));
    }

    private String extractTextFromPdf(byte[] pdfBytes) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text != null ? text.trim() : "";
        }
    }

    private ResumeResponse toResponse(Resume resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getFileType(),
                resume.getUploadedAt(),
                resume.getExtractedText()
        );
    }
}
