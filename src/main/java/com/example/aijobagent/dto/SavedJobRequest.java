package com.example.aijobagent.dto;

public class SavedJobRequest {
    private Long jobId; // For internal jobs
    private String externalJobId; // For external jobs
    private String externalJobTitle;
    private String externalCompany;
    private String externalLocation;
    private String externalUrl;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
}
