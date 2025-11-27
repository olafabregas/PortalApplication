package com.example.portalapplication.services.dashboardServices;

import com.example.portalapplication.models.dashboardDTO.admin.AdminDashboardDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {
    public AdminDashboardDTO getStats() {
        AdminDashboardDTO dto = new AdminDashboardDTO();
        dto.setUsers(120);
        dto.setCourses(14);
        dto.setTeams(32);
        dto.setSubmissions(260);
        return dto;
    }
}
