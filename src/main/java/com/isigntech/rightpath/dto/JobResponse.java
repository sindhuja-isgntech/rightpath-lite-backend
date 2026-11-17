package com.isigntech.rightpath.dto;

import java.time.LocalDateTime;

import com.isigntech.rightpath.entity.Job;

public record JobResponse(
        Long id,
        String jobCode,
        String title,
        String companyName,
        String description,
        String location,
        Job.EmploymentType employmentType,
        String requiredSkills,
        LocalDateTime deadline,
        String status,
        boolean archived,
        Long createdById,
        String createdByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
