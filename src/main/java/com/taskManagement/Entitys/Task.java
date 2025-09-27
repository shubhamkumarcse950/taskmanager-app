package com.taskManagement.Entitys;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskId;
	private String title;
	@Column(length = 512)
	private String description;
	private String status;
	private String priority;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate dueDate;
	private Long developerId;
	private Long projectId;
	@JsonManagedReference
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<TaskProgress> taskProgresses;
	private String userCode;
	private String taskSubmitType;
	@Column(nullable = true)
	private Long submitDay;
}
