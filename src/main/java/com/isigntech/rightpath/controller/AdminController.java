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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final ApplicationService applicationService;

	@GetMapping
	public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
		return ResponseEntity.ok(applicationService.getAllApplications());
	}

	@GetMapping("/{id}/history")
	public ResponseEntity<List<ApplicationStatusHistoryResponse>> getStatusHistory(@PathVariable Long id) {
		return ResponseEntity.ok(applicationService.getStatusHistory(id));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<MessageResponse> updateApplicationStatus(
			@PathVariable Long id,
			@Valid @RequestBody ApplicationStatusUpdateRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {

		applicationService.updateApplicationStatus(id, request.status(), userDetails.getUsername());
		return ResponseEntity.ok(new MessageResponse("Application status updated successfully"));
	}

}
