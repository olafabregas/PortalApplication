package com.example.portalapplication.controllers;

import com.example.portalapplication.services.dashboardServices.AdminDashboardService;
import com.example.portalapplication.services.dashboardServices.InstructorDashboardService;
import com.example.portalapplication.services.dashboardServices.StudentDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private  StudentDashboardService studentService;
    @Autowired
    private  InstructorDashboardService instructorService;
    @Autowired
    private  AdminDashboardService adminService;



    // -----------------------------
    // STUDENT DASHBOARD
    // -----------------------------
    @GetMapping("/student")
    public String studentDashboard(Model model) {
        int studentId = 1; // later retrieve logged in ID

        model.addAttribute("pageTitle", "Student Dashboard");
        model.addAttribute("stats", studentService.getStats(studentId));
        model.addAttribute("deadlines", studentService.getUpcomingDeadlines(studentId));
        model.addAttribute("teams", studentService.getTeams(studentId));
        model.addAttribute("recentSubmissions", studentService.getRecentSubmissions(studentId));
        return "dashboard/student-dashboard";
    }

    // -----------------------------
    // INSTRUCTOR DASHBOARD
    // -----------------------------
    @GetMapping("/instructor")
    public String instructorDashboard(Model model) {
        int instructorId = 101;

        model.addAttribute("pageTitle", "Instructor Dashboard");
        model.addAttribute("stats", instructorService.getStats(instructorId));
        model.addAttribute("pendingReviews", instructorService.getPendingReviews(instructorId));
        model.addAttribute("courses", instructorService.getCourseOverview(instructorId));
        model.addAttribute("recentFeedback", instructorService.getRecentFeedback(instructorId));
        return "dashboard/instructor-dashboard";
    }

    // -----------------------------
    // ADMIN DASHBOARD
    // -----------------------------
    @GetMapping("/admin")
    public String adminDashboard(Model model) {

        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("stats", adminService.getStats());
        return "dashboard/admin-dashboard";
    }
}
