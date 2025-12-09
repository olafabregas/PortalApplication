package com.example.portalapplication.services;

import com.example.portalapplication.models.SubmissionVersion;
import com.example.portalapplication.repositories.SubmissionVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionService {
    @Autowired
    SubmissionVersionRepository repo;

    public List<SubmissionVersion> findAllBySubmissionId(Integer submissionId) {
        return repo.findBySubmission_Id(submissionId);
    }
}
