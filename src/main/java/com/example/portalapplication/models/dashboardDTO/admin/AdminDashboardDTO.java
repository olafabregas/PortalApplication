package com.example.portalapplication.models.dashboardDTO.admin;

public class AdminDashboardDTO {
    private Integer users;
    private Integer courses;
    private Integer teams;
    private Integer submissions;

    public AdminDashboardDTO(Integer users, Integer courses, Integer teams,  Integer submissions) {
        this.courses = courses;
        this.submissions = submissions;
        this.teams = teams;
        this.users = users;
    }

    public Integer getCourses() {
        return courses;
    }

    public void setCourses(Integer courses) {
        this.courses = courses;
    }

    public Integer getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Integer submissions) {
        this.submissions = submissions;
    }

    public Integer getTeams() {
        return teams;
    }

    public void setTeams(Integer teams) {
        this.teams = teams;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }
}
