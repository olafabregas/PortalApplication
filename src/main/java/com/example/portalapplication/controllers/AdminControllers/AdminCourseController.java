package com.example.portalapplication.controllers.AdminControllers;

import com.example.portalapplication.exceptions.DeleteNotAllowedException;
import com.example.portalapplication.models.Course;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.models.User;
import com.example.portalapplication.models.enums.Role;
import com.example.portalapplication.repositories.SubmissionRepository;
import com.example.portalapplication.repositories.TeamRepository;
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
@RequestMapping("/admin/courses")
public class AdminCourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SubmissionRepository subRepo;

    // view all courses
    @GetMapping
    public String listCourses(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {
        /*get query parameters from url
          for example:
            /admin/courses?search=web
        * */
        List<Course> courses;

        if (search == null || search.isBlank()) {
            courses = courseService.findAll();
        } else {
            courses = courseService.search(search);
        }

        model.addAttribute("courses", courses);
        model.addAttribute("search", search == null ? "" : search);

        return "admin/courses/list";
    }
    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Integer id, Model model) {
        Course course = courseService.findById(id);
        List<Team> teams = teamService.findTeamsByCourse(id);

        // teamId → list of submissions
        Map<Integer, List<Submission>> teamSubmissions = new HashMap<>();

        for (Team t : teams) {
            List<Submission> subs = subRepo.findByTeam_Id(t.getId());
            teamSubmissions.put(t.getId(), subs);
        }

        model.addAttribute("course", course);
        model.addAttribute("teams", teams);
        model.addAttribute("teamSubmissions", teamSubmissions);
        model.addAttribute("students",teamService.getAvailableStudentsForCourse(course.getId()));
        return "admin/courses/view";
    }
    @GetMapping("/{courseId}/enroll")
    public String showEnrollStudentsPage(@PathVariable Integer courseId, Model model) {

        Course course = courseService.findById(courseId);
        List<User> availableStudents = courseService.getAvailableStudents(courseId);

        model.addAttribute("course", course);
        model.addAttribute("availableStudents", availableStudents);

        return "admin/courses/enroll";
    }
    @PostMapping("/{courseId}/enroll")
    public String enrollStudents(
            @PathVariable Integer courseId,
            @RequestParam("studentIds") List<Integer> studentIds,
            RedirectAttributes ra) {

        try {
            courseService.enrollStudents(studentIds, courseId);
            ra.addFlashAttribute("success", "Students enrolled successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/courses/" + courseId;
    }
    //create course
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Course course = new Course();

        // Only instructors
        List<User> instructors = userService.getActiveInstructors();

        model.addAttribute("course", course);
        model.addAttribute("instructors", instructors);
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admin/courses/create");

        return "admin/courses/form";
    }
    @PostMapping("/create")
    public String createCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/courses";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id,
                               Model model) {
        Course course = courseService.findById(id);
        if (course == null) {
            return "redirect:/admin/courses";
        }
        List<User> instructors = userService.getActiveInstructors();

        model.addAttribute("course", course);
        model.addAttribute("instructors", instructors);

        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admin/courses/edit");
        return "admin/courses/form";
    }
    @PostMapping("/edit")
    public String updateCourse(
            @RequestParam(value = "studentIds", required = false) List<Integer> studentIds,
            @ModelAttribute("course") Course courseForm) {

        Course course = courseService.updateCourse(courseForm,studentIds);
        return "redirect:/admin/courses/" + course.getId();
    }
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            // Try deleting or disabling the course
            courseService.deleteOrDisableCourse(id);
            redirect.addFlashAttribute("success", "Course deleted successfully.");

        } catch (DeleteNotAllowedException e) {
            // Course had teams with submissions → disabled instead
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/courses/" + id;
        }

        return "redirect:/admin/courses";
    }
}
