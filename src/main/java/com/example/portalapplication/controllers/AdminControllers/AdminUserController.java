package com.example.portalapplication.controllers.AdminControllers;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.User;
import com.example.portalapplication.models.enums.Role;
import com.example.portalapplication.repositories.SubmissionRepository;
import com.example.portalapplication.services.CourseService;
import com.example.portalapplication.services.TeamService;
import com.example.portalapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    TeamService teamService;
    @Autowired
    SubmissionRepository submissionRepo;

    // view all users
    @GetMapping
    public String listUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) Role role,
            Model model
    ) {
        /*get query parameters from url
          for example:
            /admin/users?search=jen
            /admin/users?role=STUDENT
            /admin/users?search=jen&role=ADMIN
        * */

        /* searchAndFilter decides whether to s
            how all users,
            search by name,
            filter by role or
            do the last two

         */
        List<User> users = userService.searchAndFilter(search, role);

        model.addAttribute("users", users); // return users to html
        // if there is no search return empty string. the search keyword will remain in search bar
        model.addAttribute("search", search == null ? "" : search);
        // if there is no role filter return empty string. the role dropdown will remain as selected
        model.addAttribute("role", role == null ? "ALL" : role);

        return "admin/users/list";
    }
    @GetMapping("/{id}")
    public String viewUser(@PathVariable Integer id, Model model) {
        Map<Integer, Integer> teamCounts = new HashMap<>();
        List<Course> courses = courseService.getCoursesForUser(id);
        // Build map: courseId -> team
        Map<Integer, Team> teamMap = new HashMap<>();
        Map<Integer, List<Submission>> submissionMap = new HashMap<>();


        for (Course c : courses) {
            Team t = teamService.findTeamForUserInCourse(id, c.getId());
            teamMap.put(c.getId(), t);
            teamCounts.put(c.getId(), teamService.getTeamCountForCourse(c.getId()));
            List<Submission> submissions =
                    submissionRepo.findByStudent_IdAndTeam_Course_Id(id, c.getId());
            submissionMap.put(c.getId(), submissions);
        }
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("courses", courses);
        model.addAttribute("teamCounts", teamCounts);
        model.addAttribute("teamMap", teamMap);
        model.addAttribute("submissionMap", submissionMap);

        return "admin/users/view";
    }
    // form to create new user
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        User user = new User();

        model.addAttribute("user", user);
        // will help html know whether the form is to create a new user or edit an existing one
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admin/users/create");
        return "admin/users/form";
    }

    // Handle create submit
    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admin/users/edit");
        return "admin/users/form";
    }

    // Handle edit submit
    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User updatedUser) {
        userService.saveUser(updatedUser);
        return "redirect:/admin/users/" + updatedUser.getId();
    }
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            // Attempt to delete or disable the student
            userService.deleteOrDisableUser(id);
            // Success message for the admin UI
            redirect.addFlashAttribute("success", "User deleted successfully.");
        } catch (DeleteNotAllowedException e) {
            // Error message when student has submissions
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users/" + id;
        }
        // Redirect back to the student management page
        return "redirect:/admin/users";
    }

}
