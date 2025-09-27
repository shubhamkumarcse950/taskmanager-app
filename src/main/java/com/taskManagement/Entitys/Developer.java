package com.taskManagement.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Developer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long developerId;
	private String fullName;
	@Column(name = "email", unique = true)
	private String email;
	private String compnyEmail;
	private String mobileNumber;
	private String company;
	private String userCode;
	private String techStack;
	private String role;
	private String address;
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;
}
