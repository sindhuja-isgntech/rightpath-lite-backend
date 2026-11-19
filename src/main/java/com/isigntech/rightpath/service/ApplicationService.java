package com.isigntech.rightpath.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isigntech.rightpath.dto.ApplicationResponse;
import com.isigntech.rightpath.dto.ApplicationStatusHistoryResponse;
import com.isigntech.rightpath.entity.Application;
import com.isigntech.rightpath.entity.ApplicationStatusHistory;
import com.isigntech.rightpath.entity.Job;
import com.isigntech.rightpath.entity.User;
import com.isigntech.rightpath.mapper.DtoMapper;
import com.isigntech.rightpath.repository.ApplicationRepository;
import com.isigntech.rightpath.repository.ApplicationStatusHistoryRepository;
import com.isigntech.rightpath.repository.JobRepository;
import com.isigntech.rightpath.repository.UserRepository;

import com.isigntech.rightpath.exception.BadRequestException;
import com.isigntech.rightpath.exception.ConflictException;
import com.isigntech.rightpath.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final JobRepository jobRepository;
	private final UserRepository userRepository;
	private final ApplicationStatusHistoryRepository statusHistoryRepository;

	@Transactional
	public ApplicationResponse applyForJob(Long jobId, String candidateEmail) {
		Job job = jobRepository.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));

		if (job.isArchived()
				|| !"OPEN".equalsIgnoreCase(job.getStatus())
				|| !job.getDeadline().isAfter(LocalDateTime.now())) {
			throw new BadRequestException("Cannot apply to inactive or expired jobs");
		}

		User candidate = userRepository.findByEmail(candidateEmail)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

		if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
			throw new ConflictException("Already applied to this job");
		}

		Application application = Application.builder()
				.job(job)
				.candidate(candidate)
				.status(Application.ApplicationStatus.APPLIED)
				.build();

		try {
			return DtoMapper.toResponse(applicationRepository.saveAndFlush(application));
		} catch (DataIntegrityViolationException ex) {
			// The unique (job_id, candidate_id) constraint also protects concurrent submissions.
			throw new ConflictException("Already applied to this job");
		}
	}

	@Transactional
	public ApplicationResponse updateApplicationStatus(Long applicationId,
			Application.ApplicationStatus newStatus, String adminEmail) {
		Application app = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new ResourceNotFoundException("Application not found"));

		Application.ApplicationStatus oldStatus = app.getStatus();
		boolean validTransition = switch (oldStatus) {
			case APPLIED -> newStatus == Application.ApplicationStatus.SHORTLISTED
					|| newStatus == Application.ApplicationStatus.REJECTED;
			case SHORTLISTED -> newStatus == Application.ApplicationStatus.SELECTED
					|| newStatus == Application.ApplicationStatus.REJECTED;
			case SELECTED, REJECTED -> false;
		};
		if (!validTransition) {
			throw new BadRequestException(
					"Invalid application status transition from " + oldStatus + " to " + newStatus);
		}

		User admin = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

		app.setStatus(newStatus);
		Application saved = applicationRepository.save(app);

		// Record history for auditability
		statusHistoryRepository.save(ApplicationStatusHistory.builder()
				.application(saved)
				.fromStatus(oldStatus)
				.toStatus(newStatus)
				.changedBy(admin)
				.build());

		return DtoMapper.toResponse(saved);
	}

	@Transactional(readOnly = true)
	public List<ApplicationResponse> getCandidateApplications(String email) {
		User candidate = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return applicationRepository.findByCandidateId(candidate.getId()).stream()
				.map(DtoMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ApplicationResponse> getAllApplications() {
		return applicationRepository.findAll().stream()
				.map(DtoMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ApplicationStatusHistoryResponse> getStatusHistory(Long applicationId) {
		return statusHistoryRepository.findByApplicationId(applicationId).stream()
				.map(DtoMapper::toResponse)
				.toList();
	}

}
