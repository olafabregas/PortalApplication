package com.example.portalapplication.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SubmissionVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Each version belongs to a single Submission
    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    // Each version may have feedback (one-to-one)
    @OneToOne(mappedBy = "version", cascade = CascadeType.ALL)
    private Feedback feedback;

    // Label such as "v1", "Initial", "Final", etc.
    private String versionLabel;
    // When this version was submitted
    private LocalDateTime submittedAt;
    // Whether this version was submitted late (submittedAt > submission.deadline)
    private boolean late;

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }
}
