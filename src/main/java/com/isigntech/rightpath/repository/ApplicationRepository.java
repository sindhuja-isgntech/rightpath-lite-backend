package com.isigntech.rightpath.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isigntech.rightpath.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long>{
	
	List<Application> findByCandidateId(Long candidateId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    Optional<Application> findByIdAndJobId(Long id, Long jobId);

}


