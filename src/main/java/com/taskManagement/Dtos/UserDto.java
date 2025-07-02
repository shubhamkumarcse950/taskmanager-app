package com.taskManagement.Dtos;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {

	private Long userId;
	@NotNull(message = "Username should not be null")
	@Size(min = 5, max = 15, message = "you should provide min 5 characters in userName")

	@NotNull(message = "Name should not be null")
	private String name;
	@NotNull
	@Email(message = "Email should be valid")
	private String email;
	@NotNull
	@Size(min = 5, max = 10, message = "contact should not be grater then 10")
	private String contact;
	@NotNull(message = "message should be null")
	@Size(min = 5, max = 15, message = "password should be unique")
	private String password;
	@NotNull(message = "Roles should not be null,You can write roles like that USER,ADMIN etc.")
	private List<String> roles;

}
