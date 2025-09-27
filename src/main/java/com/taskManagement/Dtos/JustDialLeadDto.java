package com.taskManagement.Dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JustDialLeadDto {

	private String name;
	private String phone;
	private String requirement;
	private String location;
	private String source;
	private List<String> followUp;
}
