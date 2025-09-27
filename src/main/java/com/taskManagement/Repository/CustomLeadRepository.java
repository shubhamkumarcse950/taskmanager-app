package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.CustomLead;

@Repository
public interface CustomLeadRepository extends JpaRepository<CustomLead, Long> {

	List<CustomLead> findByAssignStatus(boolean assignStatus);

	Page<CustomLead> findByAssignStatus(boolean b, Pageable pageable);
}
