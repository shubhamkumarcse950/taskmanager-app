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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.JustDialLeadDto;
import com.taskManagement.Service.JustDialLeadService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Just-dial-lead-controller")
@RequestMapping("/just-dial")
public class JustDialLeadController {

	private final JustDialLeadService service;
	private final ResponseWithObject responseWithObject;

	public JustDialLeadController(JustDialLeadService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/new-lead")
	public ResponseEntity<Object> justDialLead(@RequestBody JustDialLeadDto lead) {
		lead = this.service.justDialLead(lead);
		if (Objects.isNull(lead)) {
			return responseWithObject.generateResponse(AppConstants.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong !");
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.CREATED, lead);

	}

	@GetMapping("/list")
	public ResponseEntity<Object> getAllLead() {
		List<JustDialLeadDto> list = service.getAllJustDialLead();
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
