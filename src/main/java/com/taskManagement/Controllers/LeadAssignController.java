package com.taskManagement.Controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.LeadAssignDto;
import com.taskManagement.Entitys.LeadFollowUp;
import com.taskManagement.Service.LeadAssignService;
import com.taskManagement.outputdto.LeadAssignOutPutDto;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Lead-assign")
@RequestMapping("/lead-assign")
public class LeadAssignController {

	private final LeadAssignService service;
	private final ResponseWithObject responseWithObject;

	public LeadAssignController(LeadAssignService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping
	public ResponseEntity<Object> postMethodName(@RequestBody LeadAssignDto dto) {
		dto = service.leadAssignToEmployee(dto);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, dto);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.CREATED, dto);

	}

	@GetMapping
	public ResponseEntity<Object> getLeadAssign(@RequestParam String userCode) {
		List<LeadAssignOutPutDto> leadAssignOutPutDtos = service.getAllAssignLeadByUserCode(userCode);
		if (leadAssignOutPutDtos.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.OK, leadAssignOutPutDtos);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, leadAssignOutPutDtos);
	}

	@PostMapping("/auto")
	public ResponseEntity<Object> getLeadAssignAuto(@RequestParam String userCode, @RequestParam List<Long> empIds) {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				service.autoAssignLeadsEqually(userCode, empIds));
	}

	@PostMapping("/follow-up")
	public ResponseEntity<Object> addLeadFollowUp(@RequestBody LeadFollowUp followUp) {
		String response = service.addLeadfollowUp(followUp);
		if ("SourceType is not found in this lead".equals(response)) {
			return responseWithObject.generateResponse(response, HttpStatus.BAD_REQUEST, response);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, response);
	}

	@GetMapping("/getAllFollowUps")
	public ResponseEntity<List<LeadFollowUp>> getAlLeadFollowUpsByLeadIdAndSourceType(@RequestParam Long LeadId,
			@RequestParam String sourceType) {
		List<LeadFollowUp> list = service.getAllFollowUps(LeadId, sourceType);
		System.out.println(list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
