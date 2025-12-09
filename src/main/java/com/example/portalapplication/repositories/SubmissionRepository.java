package com.example.portalapplication.repositories;

import com.example.portalapplication.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    // Search by submission title OR team name OR course name
    List<Submission> findByNameContainingIgnoreCaseOrTeam_NameContainingIgnoreCaseOrTeam_Course_NameContainingIgnoreCase(
            String name,
            String teamName,
            String courseName
    );
    // Check if a specific student has submitted anything
    boolean existsByStudent_Id(Integer studentId);

    // Check if ANY team inside a course has ANY submission
    boolean existsByTeam_Course_Id(Integer courseId);

    // Check if a specific team has submissions
    boolean existsByTeam_Id(Integer teamId);
    List<Submission> findByStudent_IdAndTeam_Course_Id(Integer studentId, Integer courseId);
    List<Submission> findByTeam_Id(Integer teamId);
    // for student dashboard
    List<Submission> findByTeamIdIn(List<Integer> teamIds);

    // Instructor dashboard: count assignments in a course
    @Query("""
    SELECT COUNT(s)
    FROM Submission s
    WHERE s.team.course.id = :courseId
    AND s.grade IS NULL
""")
    int countActiveSubmissions(Integer courseId);









}
