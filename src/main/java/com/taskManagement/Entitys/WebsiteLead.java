package com.taskManagement.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WebsiteLead {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long webLeadId;
	private String name;
	private String email;
	private String number;
	private String service;
	private String companyName;
	private String message;
	private String status;
	private String source;
	private boolean assignStatus;
}
