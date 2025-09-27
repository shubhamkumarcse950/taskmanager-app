package com.taskManagement.Controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.WebsiteLeadDto;
import com.taskManagement.Service.websiteLeadService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Website-Lead-controller")

public class WebsiteLeadController {

	private final ResponseWithObject responseWithObject;
	private final websiteLeadService service;

	public WebsiteLeadController(ResponseWithObject responseWithObject, websiteLeadService service) {
		super();
		this.responseWithObject = responseWithObject;
		this.service = service;
	}

	@PostMapping("/website-lead")
	public ResponseEntity<Object> saveWebsiteLead(@RequestBody WebsiteLeadDto dto) {
		dto = service.saveLead(dto);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR, dto);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, dto);
	}

	@GetMapping("/website-lead-list")
	public ResponseEntity<Object> getAllLead() {
		List<WebsiteLeadDto> list = this.service.getAllLeads();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NA, HttpStatus.OK, list);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> delete(@RequestParam Long id) {
		return responseWithObject.generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK, service.delete(id));
	}
}
