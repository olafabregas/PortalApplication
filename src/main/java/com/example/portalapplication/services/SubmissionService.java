package com.example.portalapplication.services;

import java.time.LocalDateTime;
import com.example.portalapplication.models.Submission;
import com.example.portalapplication.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository repo;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    public List<Submission> findAll() {
        return repo.findAll();
    }
    public List<Submission> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return this.findAll();
        }
        return repo.findByNameContainingIgnoreCaseOrTeam_NameContainingIgnoreCaseOrTeam_Course_NameContainingIgnoreCase(
                keyword, keyword, keyword
        );
    }

    public Submission findById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Submission saveSubmission(Submission submission, Integer studentId, Integer teamId) {
        submission.setStudent(userService.findById(studentId));
        submission.setTeam(teamService.findById(teamId));
        submission.setSubmittedAt(LocalDateTime.now());

        // calculate late
        if (submission.getDeadline() != null && submission.getSubmittedAt() != null) {
            submission.setLate(submission.getSubmittedAt().isAfter(submission.getDeadline()));
        }

        return repo.save(submission);
    }
    public void deleteSubmission(int id) {
        repo.deleteById(id);
    }
}
