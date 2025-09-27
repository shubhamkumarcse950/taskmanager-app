package com.taskManagement.Dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomLeadDto {
	private Long id;
	private String name;
	private String phone;
	private String requirement;
	private String location;
	private String status;
	private String source;
	private List<String> followUp;
}
