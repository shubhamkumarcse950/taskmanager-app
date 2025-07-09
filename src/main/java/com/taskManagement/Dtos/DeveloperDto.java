package com.taskManagement.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDto {

	private Long developerId;
	@NotNull(message = "name should not be null!!")
	@Size(min = 1, message = "name should not be null!!")
	private String fullName;
	@NotNull(message = "Email should be unique or not null!!")
	@Size(min = 1, message = "Email should not be null!!")
	@Email(message = "Email should be valid!!")
	private String email;
	private String mobileNumber;
	private String company;
	private String address;
	private String techStack;
	private String role;
}
