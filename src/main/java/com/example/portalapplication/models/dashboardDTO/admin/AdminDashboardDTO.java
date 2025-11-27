package com.example.portalapplication.models.dashboardDTO.admin;

public class AdminDashboardDTO {
    private long users;
    private long courses;
    private long teams;
    private long submissions;

    public long getCourses() {
        return courses;
    }
    public void setCourses(long courses) {
        this.courses = courses;
    }

    public long getSubmissions() {
        return submissions;
    }
    public void setSubmissions(long submissions) {
        this.submissions = submissions;
    }

    public long getTeams() {
        return teams;
    }
    public void setTeams(long teams) {
        this.teams = teams;
    }

    public long getUsers() {
        return users;
    }
    public void setUsers(long users) {
        this.users = users;
    }
}
