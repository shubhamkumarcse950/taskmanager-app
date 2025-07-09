package com.taskManagement.outputdto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleBaseEmployee {
	private Long userId;
	private String userCode;
	private String name;
	private List<String> roles;
}
