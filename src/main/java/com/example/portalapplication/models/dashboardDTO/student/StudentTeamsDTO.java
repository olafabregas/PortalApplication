package com.example.portalapplication.models.dashboardDTO.student;

public class StudentTeamsDTO {
    private Long teamId; // for linking to team details
    private String teamName;
    private String courseName;

    //GETTERS AND SETTERS
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

}
