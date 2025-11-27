package com.example.portalapplication.models.dashboardDTO.instructor;

public class InstructorPendingReviewDTO {
    private int versionId; // to link instructor to correct version
    private String teamName;
    private String submissionTitle;
    private String version;
    private boolean late;
    private String submittedAt;// how long ago it was submitted

    //GETTERS AND SETTERS
    public boolean isLate() {
        return late;
    }
    public void setLate(boolean late) {
        this.late = late;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }
    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionId() {
        return versionId;
    }
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }
}
