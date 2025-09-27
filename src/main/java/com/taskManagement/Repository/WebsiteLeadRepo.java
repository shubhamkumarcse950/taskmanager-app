package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.WebsiteLead;

@Repository
public interface WebsiteLeadRepo extends JpaRepository<WebsiteLead, Long> {

	List<WebsiteLead> findByAssignStatus(boolean assignStatus);

	Page<WebsiteLead> findByAssignStatus(boolean b, Pageable pageable);
}
