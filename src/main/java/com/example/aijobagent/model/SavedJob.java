package com.example.aijobagent.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_jobs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "job_id"})
})
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // We can save both internal jobs and external jobs (from Adzuna)
    // If it's an internal job, we use the ManyToOne mapping.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = true)
    private Job job;

    // For external jobs from Adzuna API
    @Column(name = "external_job_id")
    private String externalJobId;

    @Column(name = "external_job_title")
    private String externalJobTitle;

    @Column(name = "external_company")
    private String externalCompany;

    @Column(name = "external_location")
    private String externalLocation;

    @Column(name = "external_url")
    private String externalUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime savedAt;

    @PrePersist
    protected void onSave() {
        if (savedAt == null) {
            savedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getExternalJobId() {
        return externalJobId;
    }

    public void setExternalJobId(String externalJobId) {
        this.externalJobId = externalJobId;
    }

    public String getExternalJobTitle() {
        return externalJobTitle;
    }

    public void setExternalJobTitle(String externalJobTitle) {
        this.externalJobTitle = externalJobTitle;
    }

    public String getExternalCompany() {
        return externalCompany;
    }

    public void setExternalCompany(String externalCompany) {
        this.externalCompany = externalCompany;
    }

    public String getExternalLocation() {
        return externalLocation;
    }

    public void setExternalLocation(String externalLocation) {
        this.externalLocation = externalLocation;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
