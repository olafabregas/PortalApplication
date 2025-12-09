package com.example.portalapplication.repositories;

import com.example.portalapplication.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("""
    SELECT f
    FROM Feedback f
    WHERE f.version.submission.team.course.instructor.id = :instructorId
    ORDER BY f.createdAt DESC
    """)
    List<Feedback> findRecentFeedback(int instructorId);

    @Query("""
        SELECT COUNT(f)
        FROM Feedback f
        WHERE f.version.submission.team.course.instructor.id = :instructorId
    """)
    int countCompletedFeedback(int instructorId);

}
