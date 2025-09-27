package com.taskManagement.Dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDTO {

	private UUID permissionId;

	@NotNull(message = "User ID cannot be null")
	private Long userId;
	@Null(message = "userName should be null!")
	private String userName;
	@Null(message = "Role should be null!")
	private String roleName;

	@NotNull(message = "Permission cannot be null")
	@Size(min = 1, message = "minium one permission should be here!")
	private List<String> permission;

	private boolean isActive = true;
}
