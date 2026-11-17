package com.isigntech.rightpath.service;

import java.time.LocalDateTime;
import java.util.List;
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

		Job job = new Job();
		DtoMapper.apply(request, job);
		job.setJobCode("JOB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		job.setCreatedBy(admin);
		job.setArchived(false);

		return DtoMapper.toResponse(jobRepository.save(job));
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
			// Candidates see only non-archived, non-expired jobs
			jobs = jobRepository.findByArchivedFalse().stream()
					.filter(job -> job.getDeadline().isAfter(LocalDateTime.now()))
					.toList();
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
