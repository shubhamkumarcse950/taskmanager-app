package com.taskManagement.Entitys;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lead {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column
	private String phone;

	@Column
	private String email;

	@Column
	private String requirement;

	@Column
	private String location;

	@Column
	private String status;
	private String source;
	@Column
	private String company;

	@Column(name = "date_added")
	private LocalDate dateAdded;
	private boolean assignStatus;
	private String industry;
	private String userCode;

}