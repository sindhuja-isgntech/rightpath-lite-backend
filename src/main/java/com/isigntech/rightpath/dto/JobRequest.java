package com.isigntech.rightpath.dto;

import java.time.LocalDateTime;

import com.isigntech.rightpath.entity.Job;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Company name is required")
        String companyName,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Location is required")
        String location,

        @NotNull(message = "Employment type is required")
        Job.EmploymentType employmentType,

        @NotBlank(message = "Required skills are required")
        String requiredSkills,

        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        LocalDateTime deadline,

        @NotBlank(message = "Status is required")
        String status) {
}
