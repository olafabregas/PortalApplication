package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.SubmissionVersion;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.dashboardDTO.student.StudentRecentSubmissionDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentDashboardStatsDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentTeamsDTO;
import com.example.portalapplication.models.dashboardDTO.student.StudentUpcomingDeadlineDTO;
import com.example.portalapplication.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.http.FastHttpDateFormat.formatDate;

@Service
public class StudentDashboardService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SubmissionRepository submissionRepo;
    @Autowired
    private TeamRepository teamRepo;
    @Autowired
    private SubmissionVersionRepository versionRepo;

    // RETURN STATS
    public StudentDashboardStatsDTO getStats(Integer studentId) {
        return new StudentDashboardStatsDTO(teamRepo.countByMembers_Id(studentId),
                versionRepo.countTotalVersions(studentId),
                versionRepo.countVersionsWithoutFeedback(studentId));
    }

    // RETURN TEAMS
    public List<StudentTeamsDTO> getTeams(Integer studentId) {
        // Get all teams that the student belongs to
        List<Team> teams = teamRepo.findByMembers_Id(studentId);

        // Prepare a list to store the DTOs
        List<StudentTeamsDTO> teamDTOs = new ArrayList<>();

        // Loop through each team and manually build the DTO
        for (Team team : teams) {
            // Create the DTO for this team
            StudentTeamsDTO dto = new StudentTeamsDTO(team.getId(), team.getName(), team.getCourse().getName());
            // Add to the result list
            teamDTOs.add(dto);
        }
        //Return the DTO list
        return teamDTOs;
    }

    // RETURN UPCOMING DEADLINES
    public List<StudentUpcomingDeadlineDTO> getUpcomingDeadlines(Integer studentId) {
        // Get all teams the student belongs to
        List<Team> teams = teamRepo.findByMembers_Id(studentId);
        // Convert to team IDs
        List<Integer> teamIds = new ArrayList<>();
        for (Team team : teams) {
            teamIds.add(team.getId());
        }

        //Get all submissions assigned to these teams
        List<Submission> submissions = submissionRepo.findByTeamIdIn(teamIds);
        List<StudentUpcomingDeadlineDTO> deadlines = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Filter only submissions with deadlines and Sort by deadline
        List<Submission> validSubs = submissions.stream()
                .filter(s -> s.getDeadline() != null)
                .sorted(Comparator.comparing(Submission::getDeadline))
                .toList();
        for (Submission submission : validSubs) {
            LocalDateTime deadlineDateTime = submission.getDeadline();
            if (deadlineDateTime != null) {
                LocalDate deadlineDate = deadlineDateTime.toLocalDate();
                boolean overdue = deadlineDate.isBefore(today);
                long daysBetween = ChronoUnit.DAYS.between(today, deadlineDate);
                String dueText;

                if (overdue) {
                    dueText = "Overdue by " + Math.abs(daysBetween) + " days";
                } else if (daysBetween == 0) {
                    dueText = "Due today";
                } else {
                    dueText = "Due in " + daysBetween + " days";
                }

                // Build the DTO
                StudentUpcomingDeadlineDTO dto = new StudentUpcomingDeadlineDTO(
                        submission.getName(),                 // submissionTitle
                        submission.getTeam().getCourse().getName(),  // courseName
                        dueText,
                        overdue
                );

                deadlines.add(dto);
            }
        }
        // Limit to 3
        if (deadlines.size() > 3) {
            return deadlines.subList(0, 3);
        }
        return deadlines;
    }

    // RETURN RECENT SUBMISSIONS
    public List<StudentRecentSubmissionDTO> getRecentSubmissions(Integer studentId) {
        // Get the 5 most recent versions submitted by student
        List<SubmissionVersion> versions =
                versionRepo.findTop5BySubmission_Team_Members_IdOrderBySubmittedAtDesc(studentId);

        List<StudentRecentSubmissionDTO> result = new ArrayList<>();

        for (SubmissionVersion v : versions) {
            Submission sub = v.getSubmission();
            int versionId = v.getId();                          // Identify the version (submission event)
            String submissionTitle = sub.getName();             // Assignment name
            String courseName = sub.getTeam().getCourse().getName();

            // Determine status
            String status;
            if (sub.getGrade() != null) {
                status = "Graded";
            } else if (v.getFeedback() == null) {
                status = "Pending Review";
            } else {
                status = "Submitted";
            }

            // Format "submittedAt"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            String lastUpdated = v.getSubmittedAt() != null
                    ? v.getSubmittedAt().format(formatter)
                    : "N/A";

            StudentRecentSubmissionDTO dto = new StudentRecentSubmissionDTO(
                    versionId,           // now version ID not submission ID
                    submissionTitle,
                    courseName,
                    status,
                    lastUpdated
            );

            result.add(dto);
        }

        return result;
    }

}
