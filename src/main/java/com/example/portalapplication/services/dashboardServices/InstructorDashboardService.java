package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.dashboardDTO.instructor.InstructorCourseOverviewDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorDashboardStatsDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorPendingReviewDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorRecentFeedbackDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorDashboardService {
    public InstructorDashboardStatsDTO getStats(int instructorId) {
        InstructorDashboardStatsDTO dto = new InstructorDashboardStatsDTO();
        dto.setPendingReview(6);
        dto.setSubmissionThisWeek(12);
        dto.setCompletedFeedback(48);
        return dto;
    }

    public List<InstructorPendingReviewDTO> getPendingReviews(int instructorId) {
        InstructorPendingReviewDTO r1 = new InstructorPendingReviewDTO();
        r1.setVersionId(100);
        r1.setTeamName("Team Alpha");
        r1.setSubmissionTitle("Milestone 2");
        r1.setVersion("v3");
        r1.setLate(false);
        r1.setSubmittedAt("3 hours ago");

        InstructorPendingReviewDTO r2 = new InstructorPendingReviewDTO();
        r2.setVersionId(101);
        r2.setTeamName("UX Innovators");
        r2.setSubmissionTitle("Wireframe Draft");
        r2.setVersion("v1");
        r2.setLate(true);
        r2.setSubmittedAt("1 day ago");

        return List.of(r1, r2);
    }

    public List<InstructorCourseOverviewDTO> getCourseOverview(int instructorId) {
        InstructorCourseOverviewDTO c1 = new InstructorCourseOverviewDTO();
        c1.setCourseId(200);
        c1.setCourseName("COMP 3001");
        c1.setTeamCount(3);
        c1.setActiveSubmissions(5);

        InstructorCourseOverviewDTO c2 = new InstructorCourseOverviewDTO();
        c2.setCourseId(201);
        c2.setCourseName("UX 210");
        c2.setTeamCount(2);
        c2.setActiveSubmissions(3);

        return List.of(c1, c2);
    }

    public List<InstructorRecentFeedbackDTO> getRecentFeedback(int instructorId) {
        InstructorRecentFeedbackDTO f1 = new InstructorRecentFeedbackDTO();
        f1.setFeedbackId(900);
        f1.setSubmissionTitle("Milestone 2");
        f1.setTeamName("Team Alpha");
        f1.setDate("2025-02-15");
        f1.setGrade("A");

        InstructorRecentFeedbackDTO f2 = new InstructorRecentFeedbackDTO();
        f2.setFeedbackId(901);
        f2.setSubmissionTitle("Wireframe Draft");
        f2.setTeamName("UX Innovators");
        f2.setDate("2025-02-14");
        f2.setGrade("B+");

        return List.of(f1, f2);
    }
}
