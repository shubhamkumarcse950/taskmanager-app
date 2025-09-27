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
public class WebsiteLeadDto {

	private Long webLeadId;
	private String name;
	private String email;
	private String number;
	private String service;
	private String companyName;
	private String message;
	private String source;
	private List<String> followUp;
}
