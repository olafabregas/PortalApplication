package com.example.portalapplication.models.dashboardDTO.student;

public class StudentUpcomingDeadlineDTO {
    private String submissionTitle;
    private String courseName;
    private String dueText; // text that described deadline (due in ... days, overdue by ... days)
    private boolean overdue;

    public StudentUpcomingDeadlineDTO(String submissionTitle, String courseName, String dueText, boolean overdue) {
        this.courseName = courseName;
        this.dueText = dueText;
        this.overdue = overdue;
        this.submissionTitle = submissionTitle;
    }

    //GETTERS AND SETTERS
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDueText() {
        return dueText;
    }
    public void setDueText(String dueText) {
        this.dueText = dueText;
    }

    public boolean isOverdue() {
        return overdue;
    }
    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }

    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }
}
