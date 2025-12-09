package com.example.portalapplication.repositories;

import com.example.portalapplication.models.SubmissionVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmissionVersionRepository extends JpaRepository<SubmissionVersion,Integer> {
    List<SubmissionVersion> findTop5BySubmission_Team_Members_IdOrderBySubmittedAtDesc(Integer studentId);

    // Get pending reviews for instructor
    @Query("""
        SELECT v FROM SubmissionVersion v
        WHERE v.submission.team.course.instructor.id = :instructorId
        AND v.feedback IS NULL
        ORDER BY v.submittedAt ASC
    """)
    List<SubmissionVersion> findPendingVersions(int instructorId);

    // Count how many versions were submitted this week
    @Query("""
        SELECT COUNT(v)
        FROM SubmissionVersion v
        WHERE v.submission.team.course.instructor.id = :instructorId
        AND v.submittedAt >= :startDate
    """)
    int countVersionsSubmittedThisWeek(int instructorId, LocalDateTime startDate);

    // Get all versions for a submission (admin/student views)
    List<SubmissionVersion> findBySubmission_Id(Integer submissionId);

    @Query("""
    SELECT COUNT(v)
    FROM SubmissionVersion v
    JOIN v.submission.team.members m
    WHERE m.id = :studentId
""")
    int countTotalVersions(int studentId);

    @Query("""
    SELECT COUNT(v)
    FROM SubmissionVersion v
    JOIN v.submission.team.members m
    WHERE m.id = :studentId
    AND v.feedback IS NULL
""")
    int countVersionsWithoutFeedback(int studentId);


    @Query("""
    SELECT COUNT(v)
    FROM SubmissionVersion v
    WHERE v.submission.team.course.instructor.id = :instructorId
    AND v.feedback IS NULL
""")
    int countPendingReviews(int instructorId);


}
