package com.isigntech.rightpath.dto;

import java.time.LocalDateTime;

import com.isigntech.rightpath.entity.User;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        User.Role role,
        LocalDateTime createdAt) {
}
