package com.taskManagement.Service;

import java.util.List;

import com.taskManagement.Dtos.CustomLeadDto;

public interface CustomLeadService {
	CustomLeadDto saveCustomLead(CustomLeadDto dto);

	List<CustomLeadDto> getAllLeads();

	String updateLeadStatus(Long id, String status);

	boolean delete(Long id);

	String updateLeadStatus(Long leadId, String source, String status);

//	Map<String, Object> getAllPlatformsLead(int page, int size);

	List<Object> getAllPlatformsLead();
}
