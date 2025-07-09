package com.taskManagement.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Service.UserServices;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/gourav")
public class PersonalController {

	private final UserServices userServices;
	private final ResponseWithObject responseWithObject;

	public PersonalController(UserServices userServices, ResponseWithObject responseWithObject) {
		super();
		this.userServices = userServices;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/testing-d")
	public ResponseEntity<Object> testingAccounts(@RequestParam(required = false) String email,
			@RequestParam Long userId, @RequestParam(required = true) String userName) {
		String response = userServices.userAccountDecative(userId, email, userName);
		if (response.equals(AppConstants.USER_ACCOUNT_DEACTIVE)) {
			return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, response);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, response);
	}

}
