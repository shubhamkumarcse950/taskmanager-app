package com.taskManagement.exceptions;

import java.io.Serializable;

public class ParameterizedErrorDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String message;

	public ParameterizedErrorDTO(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
