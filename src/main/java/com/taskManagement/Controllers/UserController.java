package com.taskManagement.Controllers;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.JwtAuthResponse;
import com.taskManagement.Dtos.LoginDto;
import com.taskManagement.Dtos.UserDto;
import com.taskManagement.Entitys.User;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.UserServices;
import com.taskManagement.ServiceImpl.JwtUserDetails;
import com.taskManagement.jwtHelper.JwtHelper;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
//@Tag(name = "User-Register-login-API")
@Slf4j
public class UserController {

	private final UserServices services;
	private JwtUserDetails jwtUserDetails;
	private AuthenticationManager authentication1;
	private JwtHelper jwtHelper;
	private final ResponseWithObject responseWithObject;
	private final UserRepo userRepo;

	public UserController(UserServices services, JwtUserDetails jwtUserDetails, AuthenticationManager authentication1,
			JwtHelper jwtHelper, ResponseWithObject responseWithObject, UserRepo userRepo) {
		super();
		this.services = services;
		this.jwtUserDetails = jwtUserDetails;
		this.authentication1 = authentication1;
		this.jwtHelper = jwtHelper;
		this.responseWithObject = responseWithObject;
		this.userRepo = userRepo;
	}

	@PostMapping("/register")
	public ResponseEntity<Object> postMethodName(@RequestBody UserDto entity) {
		UserDto userDto = services.saveUserDetails(entity);
		if (userDto.getUserId() == null) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, "invalid input");
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, userDto);

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginDto entity) {
		Authentication authentication = authentication1
				.authenticate(new UsernamePasswordAuthenticationToken(entity.getEmail(), entity.getPassword()));
		if (authentication.isAuthenticated()) {
			log.info("valid");
		} else {
			log.error("Invalid");
		}
		String token = null;
		UserDetails userDetails = jwtUserDetails.loadUserByUsername(entity.getEmail());
		User user = userRepo.findByEmail(userDetails.getUsername());

		if (user == null) {
			return responseWithObject.generateResponse(AppConstants.INVALID_USERNAME, HttpStatus.UNAUTHORIZED,
					AppConstants.INVALID_INPUT_DATA);

		}

		if (!user.isActive()) {
			return responseWithObject.generateResponse(AppConstants.BAN_ACCOUNT, HttpStatus.BAD_REQUEST,
					"Account is banned or inactive");
		}

		token = this.jwtHelper.generateToken(userDetails);
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(token);
		jwtAuthResponse.setTokenType("Bearer");
		jwtAuthResponse.setRole(userDetails.getAuthorities().stream().findFirst().get().getAuthority());
		jwtAuthResponse.setEmail(userDetails.getUsername());

		jwtAuthResponse.setUserCode(user.getUserCode());
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, jwtAuthResponse);
	}

	@GetMapping("/log-out")
	public ResponseEntity<Object> logout(HttpServletRequest httpServletRequest) {
		String headerString = httpServletRequest.getHeader("Authorization");
		if (headerString == null || !headerString.startsWith("Bearer")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");

		}
		String token = headerString.substring(7);
		if (Boolean.TRUE.equals(jwtHelper.isTokenExpired(token))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is expired");
		}
		return ResponseEntity.ok().build();
	}

}

@CrossOrigin(origins = "*")
@RequestMapping("/getuser")
@RestController
@Tag(name = "Get-User-API")
class AuthController {
	private final UserServices services;
	private final ResponseWithObject responseWithObject;

	public AuthController(UserServices services, ResponseWithObject responseWithObject) {
		super();
		this.services = services;
		this.responseWithObject = responseWithObject;
	}

	@GetMapping("/DetailsByEmail")
	public ResponseEntity<Object> getUserDetails(@RequestParam String email) {
		UserDto dto = this.services.getUserDetailsByEmail(email);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.BAD_GATEWAY,
					AppConstants.INVALID_INPUT_ID);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, dto);
	}

	@PostMapping("/restYourPassword")
	public ResponseEntity<Object> restYourPassword(@RequestParam String userCode, @RequestParam String password) {
		String response = this.services.resetYourPassword(userCode, password);
		if (response.equals(AppConstants.ERROR)) {
			return responseWithObject.generateResponse(response, HttpStatus.BAD_REQUEST,
					AppConstants.INVALID_INPUT_DATA);
		}
		return responseWithObject.generateResponse("Password update succesfully!", HttpStatus.CREATED, response);

	}

}
