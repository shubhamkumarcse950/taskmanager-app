package com.taskManagement.exceptions;

public class InvalidInputException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;

	public InvalidInputException(String message) {
		super(message);
		this.message = message;
	}

	public ParameterizedErrorDTO parameterizedErrorDTO() {
		return new ParameterizedErrorDTO(message);
	}

}
