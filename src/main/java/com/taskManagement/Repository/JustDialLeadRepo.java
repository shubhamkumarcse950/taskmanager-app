package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.JustDialLead;

@Repository
public interface JustDialLeadRepo extends JpaRepository<JustDialLead, Long> {

	List<JustDialLead> findByAssignStatus(boolean assignStatus);

	Page<JustDialLead> findByAssignStatus(boolean b, Pageable pageable);
}
