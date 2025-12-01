package com.example.portalapplication.models.dashboardDTO.instructor;

public class InstructorRecentFeedbackDTO {
    private int feedbackId;
    private String submissionTitle;
    private String teamName;
    private String date;
    private String grade;

    //GETTERS AND SETTERS

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getFeedbackId() {
        return feedbackId;
    }
    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }
    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }

    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
