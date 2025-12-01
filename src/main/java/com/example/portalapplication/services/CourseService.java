package com.example.portalapplication.services;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.User;
import com.example.portalapplication.models.enums.Role;
import com.example.portalapplication.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository repo;
    @Autowired
    private TeamRepository teamRepo;
    @Autowired
    private SubmissionRepository subRepo;
    @Autowired
    private UserRepository userRepo;

    public List<Course> findAll() {
        return repo.findAll();
    }
    public List<Course> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return this.findAll();
        }
        return repo.searchCourses(keyword);
    }
    public Course findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course saveCourse(Course course) {
        return repo.save(course);
    }
    public Course updateCourse(Course updatedCourse, List<Integer> studentIds) {
        Course course = this.findById(updatedCourse.getId());

        // Update simple fields from form
        course.setCode(updatedCourse.getCode());
        course.setName(updatedCourse.getName());
        course.setDescription(updatedCourse.getDescription());
        course.setInstructor(updatedCourse.getInstructor());
        course.setActive(updatedCourse.isActive());

        if(studentIds == null || studentIds.isEmpty()) {
            course.getStudents().clear();  // clear all students
        }else {
            // clear existing students
            course.getStudents().clear();
            // rebuild list using database
            for (Integer id : studentIds) {
                User s = userRepo.findById(id).orElse(null);
                if (s != null) {
                    course.getStudents().add(s);
                }
            }
        }

        return this.saveCourse(course);
    }

    public List<User> getAvailableStudents(Integer courseId) {

        Course course = this.findById(courseId);

        // all users with student role
        List<User> allStudents = userRepo.findByRole(Role.STUDENT);

        // remove already enrolled
        return allStudents.stream()
                .filter(User::isActive)                           // only active students
                .filter(s -> !course.getStudents().contains(s))    // exclude already enrolled
                .collect(Collectors.toList());
    }
    public List<User> getUsersInCourse(Integer courseId) {
        return this.findById(courseId).getStudents();
    }
    public void enrollStudents(List<Integer> studentIds, Integer courseId) {

        Course course = this.findById(courseId);
        List<User> students = userRepo.findAllById(studentIds);

        for (User student : students) {
            // only allow STUDENT role
            if (!student.getRole().equals(Role.STUDENT)) {
                continue; // skip
            }else if (course.getStudents().contains(student)) {
                // skip already enrolled
                continue;
            }else if (!student.isActive()){
                throw new RuntimeException("Cannot enroll disabled student.");
            } else{
                // add student
                course.getStudents().add(student);
            }
        }
        repo.save(course);
    }
    public List<Course> getCoursesForUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findAll().stream()
                .filter(course ->
                        course.getStudents().contains(user) ||   // enrolled as STUDENT
                                course.getInstructor() != null &&
                                        course.getInstructor().getId().equals(userId) // teaches as INSTRUCTOR
                )
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteOrDisableCourse(Integer courseId) {
        // Find course or throw
        Course course = this.findById(courseId);

        // Check if ANY team inside this course has submissions
        boolean teamHasSubmission = subRepo.existsByTeam_Course_Id(courseId);
        // If submissions exist disable and send error message
        if (teamHasSubmission) {
            course.setActive(false);
            repo.saveAndFlush(course);
            throw new DeleteNotAllowedException(
                    "Course has teams with submissions. It was disabled instead."
            );
        }else {
            teamRepo.deleteByCourse_Id(courseId);
            // Otherwise delete (even if teams exist but are clean)
            repo.delete(course);
        }
    }
}
