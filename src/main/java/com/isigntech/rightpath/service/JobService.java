package com.isigntech.rightpath.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isigntech.rightpath.dto.JobRequest;
import com.isigntech.rightpath.dto.JobResponse;
import com.isigntech.rightpath.entity.Job;
import com.isigntech.rightpath.entity.User;
import com.isigntech.rightpath.mapper.DtoMapper;
import com.isigntech.rightpath.repository.JobRepository;
import com.isigntech.rightpath.repository.UserRepository;

import com.isigntech.rightpath.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

	private final JobRepository jobRepository;
	private final UserRepository userRepository;

	@Transactional
	public JobResponse createJob(JobRequest request, String adminEmail) {
		User admin = userRepository.findByEmail(adminEmail)
				.orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

		Job job = Job.builder()
				.jobCode(generateJobCode())
				.createdBy(admin)
				.archived(false)
				.build();
		DtoMapper.apply(request, job);

		return DtoMapper.toResponse(jobRepository.save(job));
	}

	/** Short, readable reference such as JOB-3F9A1C2B (8 hex chars), re-rolled on the rare collision. */
	private String generateJobCode() {
		String jobCode;
		do {
			jobCode = "JOB-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
		} while (jobRepository.existsByJobCode(jobCode));
		return jobCode;
	}

	@Transactional
	public JobResponse updateJob(Long id, JobRequest request) {
		Job job = jobRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));

		DtoMapper.apply(request, job);

		return DtoMapper.toResponse(jobRepository.save(job));
	}

	@Transactional
	public void archiveJob(Long id) {
		Job job = jobRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));
		job.setArchived(true);
		jobRepository.save(job);
	}

	@Transactional(readOnly = true)
	public List<JobResponse> getJobs(boolean isAdmin) {
		List<Job> jobs;
		if (isAdmin) {
			jobs = jobRepository.findAll();
		} else {
			// Candidates need expired listings too, so they can filter by deadline.
			jobs = jobRepository.findByArchivedFalse();
		}
		return jobs.stream().map(DtoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public JobResponse getJobById(Long id) {
		return jobRepository.findById(id)
				.map(DtoMapper::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found"));
	}
}
