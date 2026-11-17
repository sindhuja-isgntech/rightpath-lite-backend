package com.isigntech.rightpath.dto;

import java.time.LocalDateTime;

import com.isigntech.rightpath.entity.Application;

public record ApplicationStatusHistoryResponse(
        Long id,
        Long applicationId,
        Application.ApplicationStatus fromStatus,
        Application.ApplicationStatus toStatus,
        String changedByName,
        String changedByEmail,
        LocalDateTime changedAt) {
}
