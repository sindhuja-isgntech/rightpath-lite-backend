package com.isigntech.rightpath.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.isigntech.rightpath.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>{
	
	Optional<Job> findByJobCode(String jobCode);
	
	List<Job> findByArchivedFalse();
	

}
