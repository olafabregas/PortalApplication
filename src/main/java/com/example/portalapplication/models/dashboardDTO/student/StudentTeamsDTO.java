package com.example.portalapplication.models.dashboardDTO.student;

public class StudentTeamsDTO {
    private Integer teamId; // for linking to team details
    private String teamName;
    private String courseName;

    public StudentTeamsDTO(Integer teamId, String teamName, String courseName) {
        this.courseName = courseName;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    //GETTERS AND SETTERS
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

}
