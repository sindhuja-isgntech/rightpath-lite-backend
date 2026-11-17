package com.isigntech.rightpath.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isigntech.rightpath.entity.ApplicationStatusHistory;

public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {
	
	List<ApplicationStatusHistory> findByApplicationId(Long applicationId);

}
