package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.dashboardDTO.student.StudentRecentSubmissionDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentDashboardStatsDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentTeamsDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentUpcomingDeadlineDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentDashboardService {
    public StudentDashboardStatsDTO getStats(int studentId) {
        StudentDashboardStatsDTO dto = new StudentDashboardStatsDTO();
        dto.setTeamCount(2);
        dto.setActiveSubmissions(4);
        dto.setPendingFeedback(1);
        return dto;
    }

    public List<StudentUpcomingDeadlineDTO> getUpcomingDeadlines(int studentId) {
        StudentUpcomingDeadlineDTO d1 = new StudentUpcomingDeadlineDTO();
        d1.setSubmissionTitle("Milestone 2");
        d1.setCourseName("COMP 3001");
        d1.setDueText("Due in 3 days");
        d1.setOverdue(false);

        StudentUpcomingDeadlineDTO d2 = new StudentUpcomingDeadlineDTO();
        d2.setSubmissionTitle("Wireframe Draft");
        d2.setCourseName("UX 210");
        d2.setDueText("Due tomorrow");
        d2.setOverdue(false);

        return List.of(d1, d2);
    }
    public List<StudentTeamsDTO> getTeams(int studentId) {
        StudentTeamsDTO  t1 = new StudentTeamsDTO();
        t1.setTeamId(1L);
        t1.setTeamName("Team Alpha");
        t1.setCourseName("COMP 3001");

        StudentTeamsDTO t2 = new StudentTeamsDTO();
        t2.setTeamId(2L);
        t2.setTeamName("UX Innovators");
        t2.setCourseName("UX 210");

        return List.of(t1, t2);
    }

    public List<StudentRecentSubmissionDTO> getRecentSubmissions(int studentId) {
        StudentRecentSubmissionDTO s1 = new StudentRecentSubmissionDTO();
        s1.setSubmissionId(10);
        s1.setSubmissionTitle("Milestone 2");
        s1.setCourseName("COMP 3001");
        s1.setStatus("Pending Review");
        s1.setLastUpdated("2 hours ago");

        StudentRecentSubmissionDTO s2 = new StudentRecentSubmissionDTO();
        s2.setSubmissionId(11);
        s2.setSubmissionTitle("Wireframe Draft");
        s2.setCourseName("UX 210");
        s2.setStatus("Graded");
        s2.setLastUpdated("1 day ago");

        return List.of(s1, s2);
    }

}
