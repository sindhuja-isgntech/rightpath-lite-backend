package com.isigntech.rightpath.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isigntech.rightpath.dto.ApplicationResponse;
import com.isigntech.rightpath.dto.MessageResponse;
import com.isigntech.rightpath.dto.JobRequest;
import com.isigntech.rightpath.dto.JobResponse;
import com.isigntech.rightpath.service.ApplicationService;
import com.isigntech.rightpath.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

	private final JobService jobService;
	private final ApplicationService applicationService;

	@GetMapping
	public ResponseEntity<List<JobResponse>> getAllJobs(@AuthenticationPrincipal UserDetails userDetails) {
		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		return ResponseEntity.ok(jobService.getJobs(isAdmin));
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
		return ResponseEntity.ok(jobService.getJobById(id));
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request,
			@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(jobService.createJob(request, userDetails.getUsername()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
		return ResponseEntity.ok(jobService.updateJob(id, request));
	}

	@PatchMapping("/{id}/archive")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageResponse> archiveJob(@PathVariable Long id) {
		jobService.archiveJob(id);
		return ResponseEntity.ok(new MessageResponse("Job archived successfully"));
	}

	@PostMapping("/{id}/apply")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<MessageResponse> applyForJob(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {
		applicationService.applyForJob(id, userDetails.getUsername());
		return ResponseEntity.ok(new MessageResponse("Application submitted successfully"));
	}

	@GetMapping("/me/applications")
	@PreAuthorize("hasRole('CANDIDATE')")
	public ResponseEntity<List<ApplicationResponse>> getMyApplications(
			@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(applicationService.getCandidateApplications(userDetails.getUsername()));
	}

}
