package com.example.portalapplication.models.dashboardDTO.instructor;

public class InstructorDashboardStatsDTO {
    private int pendingReview;
    private int submissionThisWeek;
    private int completedFeedback;

    //GETTERS AND SETTERS

    public int getPendingReview() {
        return pendingReview;
    }
    public void setPendingReview(int pendingReview) {
        this.pendingReview = pendingReview;
    }

    public int getCompletedFeedback() {
        return completedFeedback;
    }
    public void setCompletedFeedback(int completedFeedback) {
        this.completedFeedback = completedFeedback;
    }

    public int getSubmissionThisWeek() {
        return submissionThisWeek;
    }
    public void setSubmissionThisWeek(int submissionThisWeek) {
        this.submissionThisWeek = submissionThisWeek;
    }
}
