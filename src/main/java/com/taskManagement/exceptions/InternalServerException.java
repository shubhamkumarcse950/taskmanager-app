package com.taskManagement.exceptions;

public class InternalServerException extends Exception {

	private final static long serialVersionUID = 1L;
	private String message;

	public InternalServerException(String message) {
		super(message);
		this.message = message;
	}

	public ParameterizedErrorDTO parameterizedErrorDTO() {
		return new ParameterizedErrorDTO(message);
	}
}
