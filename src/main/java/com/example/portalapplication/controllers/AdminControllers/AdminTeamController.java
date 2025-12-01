package com.example.portalapplication.controllers.AdminControllers;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.User;
import com.example.portalapplication.repositories.SubmissionRepository;
import com.example.portalapplication.services.CourseService;
import com.example.portalapplication.services.TeamService;
import com.example.portalapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/teams")
public class AdminTeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private SubmissionRepository subRepo;


    // LIST + SEARCH
    @GetMapping
    public String listTeams(
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        List<Team> teams = teamService.search(search);

        model.addAttribute("teams", teams);
        model.addAttribute("search", search == null ? "" : search);

        return "admin/teams/list";
    }
    @GetMapping("/{id}")
    public String viewTeam(@PathVariable Integer id, Model model) {
        List<Submission> teamSubmissions = subRepo.findByTeam_Id(id);

        model.addAttribute("teamSubmissions", teamSubmissions);
        model.addAttribute("team", teamService.findById(id));
        return "admin/teams/view";
    }
    // CREATE FORM
    @GetMapping("/create")
    public String showCreateForm(@RequestParam Integer courseId,
                                 Model model) {
        Team team = new Team();
        model.addAttribute("team", team);

        List<User> availableStudents = teamService.getAvailableStudentsForCourse(courseId);
        model.addAttribute("students", availableStudents);

        model.addAttribute("courseId", courseId);
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admin/teams/create");

        return "admin/teams/form";
    }

    // SAVE NEW TEAM
    @PostMapping("/create")
    public String createTeam(@ModelAttribute("team") Team team,
                             @RequestParam Integer courseId,
                             @RequestParam (required=false) List<Integer> memberIds) {
        teamService.saveTeam(team, courseId, memberIds);
        return "redirect:/admin/courses/" + courseId;
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id,
                               Model model) {
        Team team = teamService.findById(id);
        if (team == null) {
            return "redirect:/admin/teams";
        }
        model.addAttribute("team", team);

        List<User> availableStudents = teamService.getAvailableStudentsForCourse(team.getCourse().getId());
        availableStudents.addAll(team.getMembers());
        model.addAttribute("students", availableStudents);
        model.addAttribute("courseId", team.getCourse().getId());
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admin/teams/edit");


        return "admin/teams/form";
    }
    // UPDATE TEAM
    @PostMapping("/edit")
    public String updateTeam(
            @ModelAttribute("team") Team updatedTeam,
            @RequestParam Integer courseId,
            @RequestParam List<Integer> memberIds
    ) {
        teamService.saveTeam(updatedTeam, courseId, memberIds);

        return "redirect:/admin/teams/" + updatedTeam.getId();
    }

    // DELETE TEAM
    @PostMapping("/delete/{id}")
    public String deleteTeam(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            // Try deleting or disabling the team
            teamService.deleteOrDisableTeam(id);
            redirect.addFlashAttribute("success", "Team deleted successfully.");
            return "redirect:/admin/teams/" + id;
        } catch (DeleteNotAllowedException e) {

            // Team had submissions → soft delete occurred
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/teams";
    }
}
