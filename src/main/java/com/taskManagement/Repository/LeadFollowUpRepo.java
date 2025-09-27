package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.LeadFollowUp;

@Repository
public interface LeadFollowUpRepo extends JpaRepository<LeadFollowUp, Integer> {

	List<LeadFollowUp> findByLeadIdAndSourceType(Long leadId, String sourceType);

}
