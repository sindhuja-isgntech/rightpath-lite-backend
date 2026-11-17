package com.isigntech.rightpath.dto;

import com.isigntech.rightpath.entity.Application;

import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(
        @NotNull(message = "Status is required")
        Application.ApplicationStatus status) {
}
