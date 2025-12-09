package com.example.portalapplication.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // RELATIONSHIP TO VERSION
    @OneToOne
    @JoinColumn(name = "version_id", nullable = false)
    private SubmissionVersion version;

    // FEEDBACK DETAILS
    private String comments;      // Instructor written notes
    private LocalDateTime createdAt;  // When the feedback was given

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SubmissionVersion getVersion() {
        return version;
    }

    public void setVersion(SubmissionVersion version) {
        this.version = version;
    }
}
