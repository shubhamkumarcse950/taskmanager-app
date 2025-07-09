package com.taskManagement.Entitys;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskProgress {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long taskProgressId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id")
	@JsonBackReference
	private Task task;
	@Column(length = 1000)
	private String todayTaskProgres;
	private LocalDate todayDate;
}
