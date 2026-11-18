package com.isigntech.rightpath.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be at most 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be at most 50 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
        String mobileNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        @Pattern(regexp = "^[A-Z].*$", message = "Password must start with an uppercase letter")
        @Pattern(regexp = "^.*[^A-Za-z0-9].*$", message = "Password must contain a special character")
        String password) {
}
