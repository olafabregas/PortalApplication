package com.example.portalapplication.models.dashboardDTO.student;

public class StudentRecentSubmissionDTO {
    private int submissionId; // used to link to submission detail view
    private String submissionTitle;
    private String courseName;
    private String status;
    private String lastUpdated;

    public StudentRecentSubmissionDTO(int submissionId, String submissionTitle, String courseName, String status, String lastUpdated) {
        this.courseName = courseName;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.submissionId = submissionId;
        this.submissionTitle = submissionTitle;
    }

    //GETTERS AND SETTERS
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubmissionId() {
        return submissionId;
    }
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }
    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }
}
