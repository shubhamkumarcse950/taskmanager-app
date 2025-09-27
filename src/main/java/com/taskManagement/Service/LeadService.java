
package com.taskManagement.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.LeadDTO;

public interface LeadService {

	Page<LeadDTO> getAllLeads(Pageable pageable, String name, String location, String status, String requirement);

	ByteArrayInputStream downloadTemplate();

	String updateLeadStatus(Long id, String status);

	boolean delete(Long id);

	Map<String, Object> uploadLeads(MultipartFile file, String userCode);

	List<LeadDTO> getAllLeads(String userCode);

	String saveLead(LeadDTO dto);
}