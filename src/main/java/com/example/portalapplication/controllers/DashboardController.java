package com.example.portalapplication.controllers;

import com.example.portalapplication.services.dashboardServices.AdminDashboardService;
import com.example.portalapplication.services.dashboardServices.InstructorDashboardService;
import com.example.portalapplication.services.dashboardServices.StudentDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/student/{id}")
    public String studentDashboard(@PathVariable Integer id,  Model model) {
        model.addAttribute("stats", studentService.getStats(id));
        model.addAttribute("deadlines", studentService.getUpcomingDeadlines(id));
        model.addAttribute("teams", studentService.getTeams(id));
        model.addAttribute("recentSubmissions", studentService.getRecentSubmissions(id));
        return "dashboard/student-dashboard";
    }

    // -----------------------------
    // INSTRUCTOR DASHBOARD
    // -----------------------------
    @GetMapping("/instructor/{id}")
    public String instructorDashboard(@PathVariable Integer id, Model model) {
        model.addAttribute("stats", instructorService.getStats(id));
        model.addAttribute("pendingReviews", instructorService.getPendingReviews(id));
        model.addAttribute("courses", instructorService.getCourseOverview(id));
        model.addAttribute("recentFeedback", instructorService.getRecentFeedback(id));
        return "dashboard/instructor-dashboard";
    }

    // -----------------------------
    // ADMIN DASHBOARD
    // -----------------------------
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", adminService.getStats());
        return "dashboard/admin-dashboard";
    }
}
