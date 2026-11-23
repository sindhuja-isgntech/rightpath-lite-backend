package com.isigntech.rightpath.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isigntech.rightpath.dto.ApplicationResponse;
import com.isigntech.rightpath.dto.ApplicationStatusHistoryResponse;
import com.isigntech.rightpath.dto.ApplicationStatusUpdateRequest;
import com.isigntech.rightpath.dto.MessageResponse;
import com.isigntech.rightpath.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin applications", description = "Admin application review and status management")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

	private final ApplicationService applicationService;

	@GetMapping
	@Operation(summary = "List all applications (admin only)")
	public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
		return ResponseEntity.ok(applicationService.getAllApplications());
	}

	@GetMapping("/{id}/history")
	@Operation(summary = "Get an application's status history (admin only)")
	public ResponseEntity<List<ApplicationStatusHistoryResponse>> getStatusHistory(@PathVariable Long id) {
		return ResponseEntity.ok(applicationService.getStatusHistory(id));
	}

	@PatchMapping("/{id}/status")
	@Operation(summary = "Update an application's status (admin only)")
	public ResponseEntity<MessageResponse> updateApplicationStatus(
			@PathVariable Long id,
			@Valid @RequestBody ApplicationStatusUpdateRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		applicationService.updateApplicationStatus(id, request.status(), userDetails.getUsername());
		return ResponseEntity.ok(new MessageResponse("Application status updated successfully"));
	}

}
