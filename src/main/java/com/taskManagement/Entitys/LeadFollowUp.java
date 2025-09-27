package com.taskManagement.Entitys;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class LeadFollowUp {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String followupText;
	private LocalDate date;
	private String time;
	private Long leadId;
	private String sourceType;
}
