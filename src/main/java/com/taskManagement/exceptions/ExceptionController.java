package com.taskManagement.exceptions;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionController {

	private final ResponseWithObject responseWithObject;

	public ExceptionController(ResponseWithObject responseWithObject) {
		super();
		this.responseWithObject = responseWithObject;
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<Object> handleThrowable(HttpServletRequest request, Throwable ex) {
		log.error("Exception occurred: {}", ex.getMessage());

		return responseWithObject.generateResponse(AppConstants.INVALID_INPUT_DATA, HttpStatus.BAD_GATEWAY,
				ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		log.error("Input field Exception", ex.getMessage());
		return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, errors);

	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFoundException(HttpServletRequest request, Throwable exception) {
		log.error("wrong request ::" + exception.getMessage());
		return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.BAD_GATEWAY,
				exception.getMessage());
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<Object> internalServerException(HttpServletRequest request, Throwable exception) {
		log.error("wrong request::" + exception.getMessage());
		return responseWithObject.generateResponse(AppConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getMessage());
	}

	@ExceptionHandler(value = IOException.class)
	public ResponseEntity<Object> handleGlobalException(IOException e, WebRequest request) {
		log.error("wrong request::" + e.getMessage());
		return responseWithObject.generateResponse("wrong request", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	ResponseEntity<Object> handleControllerException(HttpServletRequest request, Throwable ex) {
		log.error("missing parameter ::" + ex.getMessage());
		return responseWithObject.generateResponse("missing parameter", HttpStatus.INTERNAL_SERVER_ERROR, "007");

	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	@ResponseBody
	ResponseEntity<Object> handleMissingServletRequestPartException(HttpServletRequest request, Throwable ex) {
		log.error("Should select atleast file for submit" + ex.getMessage());
		return responseWithObject.generateResponse("Should select atleast file for submit",
				HttpStatus.INTERNAL_SERVER_ERROR, "009");

	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	ResponseEntity<Object> handleIllegalArgumentException(HttpServletRequest request, Throwable ex) {
		log.error("Illegal arugement" + ex.getMessage());
		return responseWithObject.generateResponse(AppConstants.INVALID_INPUT_DATA, HttpStatus.BAD_REQUEST,
				ex.getMessage());

	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseBody
	ResponseEntity<Object> handleUsernameNotFoundException(HttpServletRequest request, Throwable ex) {
		log.error("INVALID_CREDENTIALS" + ex.getMessage());
		return responseWithObject.generateResponse("INVALID_CREDENTIALS ", HttpStatus.UNAUTHORIZED, "1001");

	}

	@ExceptionHandler(RequestRejectedException.class)
	@ResponseBody
	ResponseEntity<Object> handleRequestRejectedException(HttpServletRequest request, Throwable ex) {
		log.error("INVALID_CREDENTIALS " + ex.getMessage());
		return new ResponseWithObject().generateResponse("INVALID_CREDENTIALS ", HttpStatus.UNAUTHORIZED, "000");

	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpServletRequest request, Throwable ex) {
		log.error("Wrong method type " + ex.getMessage());
		return responseWithObject.generateResponse("Wrong method type ", HttpStatus.METHOD_NOT_ALLOWED, "000");

	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	ResponseEntity<Object> handleHttpMessageNotReadableException(HttpServletRequest request, Throwable ex) {
		log.error("Wrong method type " + ex.getMessage());
		return responseWithObject.generateResponse(" Required request body is missing", HttpStatus.BAD_REQUEST, "000");

	}

	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseBody
	ResponseEntity<Object> handleExpiredJwtException(HttpServletRequest request, Throwable ex) {
		log.error("authentication expired " + ex.getMessage());
		return new ResponseWithObject().generateResponse("authentication expired",
				HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "911");

	}

	@ExceptionHandler(value = { ServletException.class })
	public ResponseEntity<Object> servletException(HttpServletRequest request, Throwable ex) {
		String message = ex.getMessage();
		if ("JWT Token has expired".equals(message)) {
			message = "Authentication expired";
		}
		return responseWithObject.generateResponse(message, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "912");
	}

	@ExceptionHandler(SignatureException.class)
	@ResponseBody
	ResponseEntity<Object> handleSignatureException(HttpServletRequest request, Throwable ex) {
		log.error("authentication not provided " + ex.getMessage());
		return responseWithObject.generateResponse("authentication not provided",
				HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "913");

	}

//	@ExceptionHandler(PSQLException.class)
//	public ResponseEntity<Object> handlePSQLException(PSQLException ex) {
//		log.error("Email should be unique or username " + ex.getMessage());
//		if (ex.getMessage().contains("duplicate key value violates unique constraint")) {
//			return responseWithObject.generateResponse(
//					"Email or UserName already exists. Please use a different email.", HttpStatus.BAD_REQUEST,
//					ex.getMessage());
//		}
//		return responseWithObject.generateResponse("Database error occurred.", HttpStatus.INTERNAL_SERVER_ERROR,
//				ex.getMessage());
//
//	}

}
