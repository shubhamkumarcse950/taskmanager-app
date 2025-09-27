package com.taskManagement.outputdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCompletionStatsDto {
	private Long developerId;
	private String developerName;
	private long totalTasks;
	private long completedTasks;
	private long onTimeCompletedTasks;
	private long pendingTasks;
	private long inProgressTasks;
	private double completedPercentage;
	private double onTimeCompletedPercentage;
	private double pendingPercentage;
	private double inProgressPercentage;
}