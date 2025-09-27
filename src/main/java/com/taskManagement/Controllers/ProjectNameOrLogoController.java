package com.taskManagement.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Service.ProjectNameOrLogoService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Add Log and Name")
public class ProjectNameOrLogoController {

	private final ProjectNameOrLogoService service;
	private final ResponseWithObject responseWithObject;

	public ProjectNameOrLogoController(ProjectNameOrLogoService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> addNameAndLogo(@RequestParam(required = false) String projectName,
			@RequestParam(required = false) MultipartFile fle) throws IOException, java.io.IOException {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				service.projectNameAndLogo(projectName, fle));
	}

	@GetMapping("/getLogo")
	public ResponseEntity<Object> getLogo() {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, service.getLogo());
	}

	@GetMapping("/getName")
	public ResponseEntity<Object> getName() {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, service.getName());
	}

}
