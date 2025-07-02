package com.taskManagement.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

	private String message;
	private LocalDateTime dateTime;
	private boolean status;

}
