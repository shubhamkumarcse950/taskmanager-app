package com.taskManagement.Dtos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskProgressDto {

	private Long taskProgressId;
	private Long taskId;
	@Column(length = 1000)
	private String todayTaskProgres;
	private LocalDate todayDate;
	private TaskDto taskDto;
}
