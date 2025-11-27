package com.example.portalapplication.models.dashboardDTO.student;

// this will send stats that will be shown at the top of the student dashboard
// the values will come form teams, submissions and Feedback models
public class StudentDashboardStatsDTO {
    // total num of teams student belongs to
    private int teamCount;

    // submissions that the student/team has received feedback on but not yet viewed
    private int activeSubmissions;

    // submission that the student hasn't received feedback on yet
    private int pendingFeedback;

    //GETTERS AND SETTERS
    public int getActiveSubmissions() {
        return activeSubmissions;
    }
    public void setActiveSubmissions(int activeSubmissions) {
        this.activeSubmissions = activeSubmissions;
    }

    public int getPendingFeedback() {
        return pendingFeedback;
    }
    public void setPendingFeedback(int pendingFeedback) {
        this.pendingFeedback = pendingFeedback;
    }

    public int getTeamCount() {
        return teamCount;
    }
    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }
}
