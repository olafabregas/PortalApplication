package com.example.portalapplication.models.dashboardDTO.instructor;

public class InstructorCourseOverviewDTO {
    private int courseId; // link to view course
    private String courseName;
    private int teamCount;
    private int activeSubmissions;

    public InstructorCourseOverviewDTO(int courseId, String courseName, int teamCount, int activeSubmissions) {
        this.activeSubmissions = activeSubmissions;
        this.courseId = courseId;
        this.courseName = courseName;
        this.teamCount = teamCount;
    }

    //GETTERS AND SETTERS

    public int getActiveSubmissions() {
        return activeSubmissions;
    }
    public void setActiveSubmissions(int activeSubmissions) {
        this.activeSubmissions = activeSubmissions;
    }

    public int getCourseId() {
        return courseId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTeamCount() {
        return teamCount;
    }
    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }
}
