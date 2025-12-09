package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.Feedback;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.SubmissionVersion;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorCourseOverviewDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorDashboardStatsDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorPendingReviewDTO;
import com.example.portalapplication.models.dashboardDTO.instructor.InstructorRecentFeedbackDTO;
import com.example.portalapplication.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorDashboardService {
    @Autowired
    SubmissionRepository submissionRepo;
    @Autowired
    CourseRepository courseRepo;
    @Autowired
    TeamRepository teamRepo;
    @Autowired
    SubmissionVersionRepository versionRepo;
    @Autowired
    FeedbackRepository feedbackRepo;

    public InstructorDashboardStatsDTO getStats(int instructorId) {
        //How many submissions need review?
        int pendingReview = versionRepo.countPendingReviews(instructorId);
        // Submissions within the last 7 days
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        int submissionThisWeek = versionRepo.countVersionsSubmittedThisWeek(instructorId, weekAgo);
        // Completed feedback count
        int completedFeedback = feedbackRepo.countCompletedFeedback(instructorId);

        return new InstructorDashboardStatsDTO(
                pendingReview,
                submissionThisWeek,
                completedFeedback
        );
    }

    public List<InstructorPendingReviewDTO> getPendingReviews(int instructorId) {
        // Get all submissions that need review
        List<SubmissionVersion> versions = versionRepo.findPendingVersions(instructorId);

        List<InstructorPendingReviewDTO> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (SubmissionVersion v : versions) {
            Submission sub = v.getSubmission();
            int versionId = v.getId();
            String teamName = sub.getTeam().getName();
            String submissionTitle = sub.getName();
            String versionLabel = v.getVersionLabel();

            // Late logic based on deadline
            boolean late = false;
            if (sub.getDeadline() != null) {
                late = v.getSubmittedAt().isAfter(sub.getDeadline());
            }

            // Time difference "x hours ago"
            Duration duration = Duration.between(v.getSubmittedAt(), now);
            String submittedAgo;

            if (duration.toMinutes() < 1)
                submittedAgo = "just now";
            else if (duration.toMinutes() < 60)
                submittedAgo = duration.toMinutes() + " minutes ago";
            else if (duration.toHours() < 24)
                submittedAgo = duration.toHours() + " hours ago";
            else
                submittedAgo = duration.toDays() + " days ago";

            // Build DTO
            InstructorPendingReviewDTO dto = new InstructorPendingReviewDTO(
                    versionId,
                    teamName,
                    submissionTitle,
                    versionLabel,
                    late,
                    submittedAgo
            );

            result.add(dto);
        }
        return result;
    }

    public List<InstructorCourseOverviewDTO> getCourseOverview(int instructorId) {
        //Get all courses taught by this instructor
        List<Course> courses = courseRepo.findByInstructor_Id(instructorId);
        // Prepare list to return
        List<InstructorCourseOverviewDTO> result = new ArrayList<>();
        // Loop through each course
        for (Course course : courses) {
            int courseId = course.getId();
            String courseName = course.getName();
            // Count how many teams are in this course
            int teamCount = teamRepo.countByCourse_Id(courseId);
            // Count active (pending) submissions
            int activeSubmissions = submissionRepo.countActiveSubmissions(courseId);
            // Build DTO
            InstructorCourseOverviewDTO dto = new InstructorCourseOverviewDTO(
                            courseId,
                            courseName,
                            teamCount,
                            activeSubmissions
                    );

            result.add(dto);
        }

        return result;
    }

    public List<InstructorRecentFeedbackDTO> getRecentFeedback(int instructorId) {
        // Step 1: Fetch feedback entries given on versions of this instructor's courses
        List<Feedback> feedbackList = feedbackRepo.findRecentFeedback(instructorId);
        List<InstructorRecentFeedbackDTO> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

        // Step 2: Build DTOs
        for (Feedback f : feedbackList) {

            SubmissionVersion version = f.getVersion();
            Submission sub = version.getSubmission();

            int feedbackId = f.getId();
            String submissionTitle = sub.getName();
            String teamName = sub.getTeam().getName();

            // Feedback timestamp
            String date = f.getCreatedAt() != null
                    ? f.getCreatedAt().format(formatter)
                    : "N/A";

            // Final grade on Submission
            String grade = sub.getGrade() != null
                    ? String.valueOf(sub.getGrade())
                    : "Not graded";

            InstructorRecentFeedbackDTO dto = new InstructorRecentFeedbackDTO(
                    feedbackId,
                    submissionTitle,
                    teamName,
                    date,
                    grade
            );

            result.add(dto);
        }

        return result;
    }

}
