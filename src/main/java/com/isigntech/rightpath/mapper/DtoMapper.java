package com.isigntech.rightpath.mapper;

import com.isigntech.rightpath.dto.ApplicationResponse;
import com.isigntech.rightpath.dto.ApplicationStatusHistoryResponse;
import com.isigntech.rightpath.dto.UserResponse;
import com.isigntech.rightpath.dto.JobRequest;
import com.isigntech.rightpath.dto.JobResponse;
import com.isigntech.rightpath.entity.Application;
import com.isigntech.rightpath.entity.ApplicationStatusHistory;
import com.isigntech.rightpath.entity.Job;
import com.isigntech.rightpath.entity.User;

/**
 * Entity to DTO conversion. Methods that touch lazy relations must be called
 * inside a transaction (the service layer), because open-in-view is disabled.
 */
public final class DtoMapper {

    private DtoMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRole(),
                user.getCreatedAt());
    }

    public static JobResponse toResponse(Job job) {
        User creator = job.getCreatedBy();
        return new JobResponse(
                job.getId(),
                job.getJobCode(),
                job.getTitle(),
                job.getCompanyName(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getRequiredSkills(),
                job.getDeadline(),
                job.getStatus(),
                job.isArchived(),
                creator != null ? creator.getId() : null,
                creator != null ? creator.getFullName() : null,
                job.getCreatedAt(),
                job.getUpdatedAt());
    }

    /** Copies the editable fields of a request onto an entity (create or update). */
    public static void apply(JobRequest request, Job job) {
        job.setTitle(request.title());
        job.setCompanyName(request.companyName());
        job.setDescription(request.description());
        job.setLocation(request.location());
        job.setEmploymentType(request.employmentType());
        job.setRequiredSkills(request.requiredSkills());
        job.setDeadline(request.deadline());
        job.setStatus(request.status());
    }

    public static ApplicationResponse toResponse(Application application) {
        Job job = application.getJob();
        User candidate = application.getCandidate();
        return new ApplicationResponse(
                application.getId(),
                job.getId(),
                job.getJobCode(),
                job.getTitle(),
                job.getCompanyName(),
                candidate.getId(),
                candidate.getFullName(),
                candidate.getEmail(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getUpdatedAt());
    }

    public static ApplicationStatusHistoryResponse toResponse(ApplicationStatusHistory history) {
        User changedBy = history.getChangedBy();
        return new ApplicationStatusHistoryResponse(
                history.getId(),
                history.getApplication().getId(),
                history.getFromStatus(),
                history.getToStatus(),
                changedBy.getFullName(),
                changedBy.getEmail(),
                history.getChangedAt());
    }
}
