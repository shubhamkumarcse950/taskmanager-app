package com.taskManagement.Controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.CustomLeadDto;
import com.taskManagement.Service.CustomLeadService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/custom-leads")
@CrossOrigin(origins = "*")
@Tag(name = "custom-leads")
public class CustomLeadController {

	private final CustomLeadService leadService;
	private final ResponseWithObject responseWithObject;

	public CustomLeadController(CustomLeadService leadService, ResponseWithObject responseWithObject) {
		this.leadService = leadService;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createLead(@RequestBody CustomLeadDto dto) {
		dto = leadService.saveCustomLead(dto);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR, dto);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, dto);
	}

	@GetMapping("/list")
	public ResponseEntity<List<CustomLeadDto>> getAllLeads() {
		return ResponseEntity.ok(leadService.getAllLeads());
	}

	@PutMapping("/update-status")
	public ResponseEntity<Object> updateStatus(@RequestParam Long id, @RequestParam String status) {
		String response = leadService.updateLeadStatus(id, status);
		if (response.equals(AppConstants.SUCCESS)) {
			return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, response);
		}
		return responseWithObject.generateResponse(AppConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
				response);

	}

	@GetMapping("/getall-leads")
	public ResponseEntity<List<Object>> getAllPlateFormsleads() {
		return ResponseEntity.ok(leadService.getAllPlatformsLead());
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> delete(@RequestParam Long id) {
		return responseWithObject.generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK,
				leadService.delete(id));
	}

	@PutMapping("/updateLead-status")
	public ResponseEntity<Object> updateLeadStatus(@RequestParam Long leadId, @RequestParam String source,
			@RequestParam String status) {

		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				leadService.updateLeadStatus(leadId, source, status));
	}
}
