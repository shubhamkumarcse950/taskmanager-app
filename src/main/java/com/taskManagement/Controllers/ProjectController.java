package com.taskManagement.Controllers;

import java.util.List;

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

import com.taskManagement.Dtos.ProjectDto;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Service.ProjectService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Project-API")
@CrossOrigin(origins = "*")
@RequestMapping("/project")
public class ProjectController {

	private final ProjectService service;
	private final ResponseWithObject responseWithObject;

	public ProjectController(ProjectService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/addProjectDetails")
	public ResponseEntity<Object> addProjectDetails(@Valid @RequestBody ProjectDto entity) {
		ProjectDto dto = this.service.saveProjectDetails(entity);
		if (java.util.Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.INVALID_INPUT_DATA, HttpStatus.BAD_REQUEST,
					AppConstants.ERROR);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, dto);
	}

	@GetMapping("/getAllProjectData")
	public ResponseEntity<Object> getAllProjectDtetails() {
		List<ProjectDto> list = this.service.getAllProjectDetails();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NA, HttpStatus.NO_CONTENT,
					AppConstants.NO_DATA_FOUND);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, list);
	}

	@GetMapping("/getProjectById")
	public ResponseEntity<Object> getProjectDetails(@RequestParam("projectId") Long projectId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.getOneProject(projectId));
	}

	@PutMapping("/updataProjectDetails")
	public ResponseEntity<Object> updataProjectDetails(@RequestBody ProjectDto entity) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.updataProjectDetails(entity));

	}

	@DeleteMapping("/deleteProjectById")
	public ResponseEntity<Object> deleteProjectbyId(@RequestParam("projectId") Long projectId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, service.deletProject(projectId));
	}

	@PostMapping("/addDeveloperInProject")
	public ResponseEntity<Object> addDeveloperInProject(@RequestBody ProjectDto entity) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.addDeveloperInProject(entity));

	}

	@GetMapping("/getAllAssignDeveloperInProject")
	public ResponseEntity<Object> getAllAssignDeveloperInProject(@RequestParam Long projectId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.getAllAssignDeveloperInProject(projectId));

	}

	@GetMapping("/getAllAssignProject")
	public ResponseEntity<Object> getAllAssignProject(@RequestParam String userCode) {
		List<Project> list = this.service.getAllAssignProject(userCode);
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NOT_FOUND, list);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

}
