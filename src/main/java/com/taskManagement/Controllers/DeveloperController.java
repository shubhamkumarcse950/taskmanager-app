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

import com.taskManagement.Dtos.DeveloperDto;
import com.taskManagement.Service.DeveloperService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/deveoperInfo")
@Tag(name = "Developer-API")
@CrossOrigin(origins = "*")
@Slf4j
public class DeveloperController {

	private final DeveloperService service;
	private final ResponseWithObject responseWithObject;

	public DeveloperController(DeveloperService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/addDeveloperInfo")
	public ResponseEntity<Object> addDevINfo(@Valid @RequestBody DeveloperDto entity) {
		DeveloperDto dto = this.service.saveDeveloperInfo(entity);
		if (dto.getDeveloperId() == null) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST,
					AppConstants.INVALID_INPUT_DATA);
		}

		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, dto);
	}

	@GetMapping("/getAllDevelopers")
	public ResponseEntity<Object> getAllDevelopers() {

		List<DeveloperDto> list = service.getAllDeveloper();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NA, HttpStatus.NO_CONTENT,
					AppConstants.NO_DATA_FOUND);
		}
		log.warn("Product Fetched succesfull on the controller Level !");
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, list);
	}

	@GetMapping("/getOneDeveloperById")
	public ResponseEntity<Object> getOneDevelopers(@RequestParam Long developerId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.getOneDeveloperById(developerId));
	}

	@PutMapping("/updateDeveloperInfo")
	public ResponseEntity<Object> updataDeveloperINfo(@RequestBody DeveloperDto entity) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.updataDeveloperInfo(entity));

	}

	@DeleteMapping("/deleteDeveloperInfo")
	public ResponseEntity<Object> deletebyId(@RequestParam("developerId") Long developerId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.deleteDeveloperInfo(developerId));
	}

	@GetMapping("/getDeveloperByIds")
	public ResponseEntity<Object> getDeveloperByIds(@RequestParam List<Long> developerId) {
		List<DeveloperDto> list = this.service.getDeveloperByIds(developerId);
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NA, HttpStatus.NOT_FOUND,
					"NO developer avliable in this projct!!");
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, list);

	}

}
