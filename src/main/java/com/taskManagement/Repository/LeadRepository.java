
package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
	List<Lead> findByNameContainingIgnoreCase(String name);

	List<Lead> findByLocationContainingIgnoreCase(String location);

	List<Lead> findByStatus(String status);

	List<Lead> findByRequirementContainingIgnoreCase(String requirement);

	List<Lead> findByAssignStatus(boolean assignStatus);

	Page<Lead> findByAssignStatus(boolean b, Pageable pageable);

	List<Lead> findByUserCode(String userCode);

	List<Lead> findByNameContainingIgnoreCaseAndUserCodeIsNull(String name);

	List<Lead> findByLocationContainingIgnoreCaseAndUserCodeIsNull(String location);

	List<Lead> findByStatusAndUserCodeIsNull(String status);

	List<Lead> findByRequirementContainingIgnoreCaseAndUserCodeIsNull(String requirement);

	List<Lead> findByUserCodeIsNull();

	List<Lead> findByAssignStatusFalseAndUserCodeIsNull();
}