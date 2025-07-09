package com.taskManagement.Controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.ProfileDto;
import com.taskManagement.Service.ProfileService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Profile controller")
@RequestMapping("/profile")
public class ProfileController {

	private final ProfileService service;
	private final ResponseWithObject responseWithObject;

	public ProfileController(ProfileService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> createProfile(@ModelAttribute ProfileDto profileDto) {
		String response = service.createProfile(profileDto, profileDto.getLogo());
		if (response.equals(AppConstants.SUCCESS)) {
			return responseWithObject.generateResponse(response, HttpStatus.CREATED, "Profile created succesfuly!");
		}
		return responseWithObject.generateResponse(response, HttpStatus.BAD_REQUEST, AppConstants.ERROR);
	}

	@GetMapping("/getProfile")
	public ResponseEntity<Object> getProfile() {
		List<ProfileDto> list = service.getUserProfile();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NO_CONTENT, list);

		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@PutMapping(value = "/updateProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> updateProfile(@ModelAttribute ProfileDto dto) throws IOException {
		String response = service.updateProfile(dto, dto.getLogo());
		if (response.equals(AppConstants.SUCCESS)) {
			return responseWithObject.generateResponse(response, HttpStatus.OK, "Profile updated succesfully");
		}
		return responseWithObject.generateResponse(response, HttpStatus.BAD_REQUEST,
				"You did provide invalid profile Id");
	}

}
