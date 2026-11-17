package com.isigntech.rightpath.dto;

import java.time.LocalDateTime;

import com.isigntech.rightpath.entity.Application;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobCode,
        String jobTitle,
        String companyName,
        Long candidateId,
        String candidateName,
        String candidateEmail,
        Application.ApplicationStatus status,
        LocalDateTime appliedAt,
        LocalDateTime updatedAt) {
}
