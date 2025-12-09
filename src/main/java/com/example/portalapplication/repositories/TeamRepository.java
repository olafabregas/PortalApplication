package com.example.portalapplication.repositories;

import com.example.portalapplication.models.Team;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    // Search by team name OR course name
    @Query("""
        SELECT t FROM Team t
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(t.course.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Team> searchTeams(@Param("keyword") String keyword);

    // Returns all teams that this student is part of
    List<Team> findByMembers_Id(Integer studentId);

    // Check if a course has ANY teams
    boolean existsByCourse_Id(Integer courseId);

    // Get all teams inside a course (useful for advanced logic)
    List<Team> findByCourse_Id(Integer courseId);

    int countByCourse_Id(Integer courseId);
    void deleteByCourse_Id(Integer courseId);
    int countByMembers_Id(Integer studentId);

}
