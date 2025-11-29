package com.example.reviewflow.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfileUpdateRequest {

    @NotBlank
    private String fullName;

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }
}
