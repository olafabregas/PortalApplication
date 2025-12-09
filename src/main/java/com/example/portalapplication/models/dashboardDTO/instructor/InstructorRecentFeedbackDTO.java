package com.example.portalapplication.models.dashboardDTO.instructor;

public class InstructorRecentFeedbackDTO {
    private int feedbackId;
    private String submissionTitle;
    private String teamName;
    private String date;
    private String grade;

    public InstructorRecentFeedbackDTO(int feedbackId, String submissionTitle,String teamName,  String date, String grade) {
        this.date = date;
        this.feedbackId = feedbackId;
        this.grade = grade;
        this.submissionTitle = submissionTitle;
        this.teamName = teamName;
    }
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
