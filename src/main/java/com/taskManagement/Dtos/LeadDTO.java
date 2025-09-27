
package com.taskManagement.Dtos;

import java.util.List;

import lombok.Data;

@Data
public class LeadDTO {
	private Long id;
	private String name;
	private String phone;
	private String email;
	private String requirement;
	private String location;
	private String source;
	private String status;
	private String company;
	private String dateAdded;
	private List<String> followUp;
	private String industry;
	private String userCode;
}