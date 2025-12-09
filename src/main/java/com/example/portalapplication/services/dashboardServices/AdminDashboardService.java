package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.dashboardDTO.admin.AdminDashboardDTO;
import com.example.portalapplication.repositories.CourseRepository;
import com.example.portalapplication.repositories.SubmissionRepository;
import com.example.portalapplication.repositories.TeamRepository;
import com.example.portalapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    CourseRepository courseRepo;
    @Autowired
    TeamRepository teamRepo;
    @Autowired
    SubmissionRepository submissionRepo;
    public AdminDashboardDTO getStats() {
        return new AdminDashboardDTO((int) userRepo.count(),
                (int) courseRepo.count(),
                (int) teamRepo.count(),
                (int) submissionRepo.count());
    }
}
