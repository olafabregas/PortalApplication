package com.example.portalapplication.repositories;

import com.example.portalapplication.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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


}
