package com.example.portalapplication.services;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.User;
import com.example.portalapplication.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository repo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SubmissionRepository subRepo;
    @Autowired
    private CourseService courseService;

    public List<Team> findAll() {
        return repo.findAll();
    }
    public Team findById(int id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
    }
    public List<Team> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return this.findAll();
        }else {
            return repo.searchTeams(keyword);
        }
    }
    // CREATE or UPDATE team + members
    @Transactional
    public Team saveTeam(Team team, Integer courseId, List<Integer> studentIds) {
        if (studentIds == null){
            studentIds = new ArrayList<>();
        }

        List<User> members = userRepo.findAllById(studentIds); // find members
        team.setMembers(members); // add them to team

        Course c = courseService.findById(courseId);//find course
        team.setCourse(c); // add to team

        return repo.save(team);
    }
    public List<Team> findTeamsByCourse(Integer courseId) {
        return repo.findByCourse_Id(courseId);
    }
    // Get all students that are in not a team for a course
    public List<User> getAvailableStudentsForCourse(Integer courseId) {

        // 1. Get all students enrolled in the course
        List<User> enrolledStudents = courseService.findById(courseId).getStudents();

        // 2. Get all teams for this course
        List<Team> courseTeams = repo.findByCourse_Id(courseId);

        // 3. Collect all users who are already in a team
        Set<User> assignedMembers = courseTeams.stream()
                .flatMap(t -> t.getMembers().stream())
                .collect(Collectors.toSet());

        // 4. Filter → ONLY students NOT assigned to any team
        return enrolledStudents.stream()
                .filter(s -> !assignedMembers.contains(s))
                .collect(Collectors.toList());
    }
    public int getTeamCountForCourse(Integer courseId) {
        return repo.countByCourse_Id(courseId);
    }
    public Team findTeamForUserInCourse(Integer userId, Integer courseId) {
        return repo.findByCourse_Id(courseId).stream()
                .filter(team -> team.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(userId)))
                .findFirst()
                .orElse(null);
    }
    @Transactional
    public void deleteOrDisableTeam(Integer teamId) {

        // Find team or error
        Team team = this.findById(teamId);
        // Check if team has any submissions
        boolean hasSubmissions = subRepo.existsByTeam_Id(teamId);
        // If submissions exist → disable instead of delete
        if (hasSubmissions) {
            team.setActive(false);
            repo.save(team);
            throw new DeleteNotAllowedException(
                    "Team has submissions. It was disabled instead."
            );
        }else {
            // No submissions → delete normally
            repo.delete(team);
        }
    }
}
