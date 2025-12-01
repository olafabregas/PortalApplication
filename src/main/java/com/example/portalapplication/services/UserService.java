package com.example.portalapplication.services;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.User;
import com.example.portalapplication.models.enums.Role;
import com.example.portalapplication.repositories.CourseRepository;
import com.example.portalapplication.repositories.SubmissionRepository;
import com.example.portalapplication.repositories.TeamRepository;
import com.example.portalapplication.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private SubmissionRepository subRepo;
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private TeamRepository teamRepo;
    // all users
    public List<User> findAll() {
        return repo.findAll();
    }
    // one user
    public User findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }
    //search
    public List<User> search(String keyword){
        // if the keyword (first or last name)  is empty then show all users
        if (keyword == null || keyword.isBlank()){
            return repo.findAll();
        } else {
            // else show users who match search pattern
            return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
        }
    }
    public List<User> filterByRole(Role role){
        // if the role is empty then show all users
        if (role == null){
            return this.findAll();
        }else{
            return repo.findByRole(role); // else show users who are in that role
        }
    }
    public List<User> searchAndFilter(String keyword, Role role){
        boolean hasKeyword = keyword != null && !keyword.isBlank(); // sets to true if the keyword is not null and is not empty
        boolean hasRole = role != null;// sets to true if the role is not null and is not empty

        // there is a role and search keyword
        if (hasKeyword && hasRole){
            // show users that match
            return repo.findByRoleAndFirstNameContainingIgnoreCaseOrRoleAndLastNameContainingIgnoreCase
                    (role, keyword, role, keyword);
        }else if (hasKeyword){ // only search key word
            return this.search(keyword); // call function to search
        }else if (hasRole){
            return this.filterByRole(role);
        }else{
            return this.findAll();
        }
    }

    public User saveUser(User user){
        return repo.save(user);
    }
    public List<User> getActiveInstructors() {
        return repo.findByRole(Role.INSTRUCTOR).stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteOrDisableUser(Integer userId) {
        // Retrieve the user from DB
        User user = this.findById(userId);
        boolean hasSubmissions = false;
        boolean teachesCourse = false;
        if (user.getRole().equals(Role.STUDENT)){
            // Check if the student has ever made a submission
            hasSubmissions = subRepo.existsByStudent_Id(userId);

            // Remove the student from ALL courses they are enrolled in
            List<Course> enrolledCourses = courseRepo.findByStudents_Id(userId);
            for (Course course : enrolledCourses) {
                course.getStudents().remove(user);
                courseRepo.save(course);
            }

            // Remove the student from ALL teams they belong to
            List<Team> teams = teamRepo.findByMembers_Id(userId);
            for (Team team : teams) {
                team.getMembers().remove(user);
                teamRepo.save(team);
            }

            if  (hasSubmissions){
                // disable the student (soft delete)
                user.setActive(false);
                repo.save(user);
                throw new DeleteNotAllowedException(
                        "Cannot delete student. Student has submissions. Student was disabled and removed from all courses and teams."
                );
            }else{
                repo.delete(user);
            }
        }else if (user.getRole().equals(Role.INSTRUCTOR)){
            // Check if this instructor is teaching a course
            teachesCourse = courseRepo.existsByInstructor_Id(userId);
            // If so, do not allow deletion
            if (teachesCourse) {
                throw new DeleteNotAllowedException(
                        "Cannot delete instructor. Instructor assigned to a course."
                );
            }else{
                repo.delete(user);
            }
        }else{
            repo.delete(user);
        }


    }
}
