package com.isigntech.rightpath.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isigntech.rightpath.dto.AuthResponse;
import com.isigntech.rightpath.dto.LoginRequest;
import com.isigntech.rightpath.dto.RegisterRequest;
import com.isigntech.rightpath.dto.UserResponse;
import com.isigntech.rightpath.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, login, and current-user endpoints")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	@Operation(summary = "Register a candidate account")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	@Operation(summary = "Log in and obtain a JWT")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/me")
	@Operation(summary = "Get the authenticated user's profile")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(authService.getCurrentUser(userDetails.getUsername()));
	}

}
