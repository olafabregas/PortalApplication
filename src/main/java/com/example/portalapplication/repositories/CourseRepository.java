package com.example.portalapplication.repositories;

import com.example.portalapplication.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    // search by course name or code or instructor first or last name
    @Query("""
    SELECT c FROM Course c
    LEFT JOIN c.instructor i
    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(i.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(i.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Course> searchCourses(@Param("keyword") String keyword);

    // Find every course that a specific student is enrolled in
    List<Course> findByStudents_Id(Integer studentId);

    // Used to prevent deleting an instructor that is teaching a course
    boolean existsByInstructor_Id(Integer instructorId);

    List<Course> findByInstructor_Id(Integer instructorId);


}
