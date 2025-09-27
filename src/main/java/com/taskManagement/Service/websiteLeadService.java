package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.WebsiteLeadDto;

@Service
public interface websiteLeadService {

	WebsiteLeadDto saveLead(WebsiteLeadDto dto);

	List<WebsiteLeadDto> getAllLeads();

	boolean delete(Long id);

}
