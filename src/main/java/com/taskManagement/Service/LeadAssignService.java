package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.LeadAssignDto;
import com.taskManagement.Entitys.LeadFollowUp;
import com.taskManagement.outputdto.LeadAssignOutPutDto;

@Service
public interface LeadAssignService {

	LeadAssignDto leadAssignToEmployee(LeadAssignDto leadAssignDto);

	List<LeadAssignOutPutDto> getAllAssignLeadByUserCode(String userCode);

	List<LeadFollowUp> getAllFollowUps(Long leadId, String sourceType);

	String addLeadfollowUp(LeadFollowUp folloUp);

	String autoAssignLeadsEqually(String userCode, List<Long> empIds);

}
