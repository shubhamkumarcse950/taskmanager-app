package com.taskManagement.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.outputdto.TaskCompletionStatsDto;

public interface TaskService {

	TaskDto doTaskForDevelopers(TaskDto dto);

	List<TaskDto> getAllTask();

	List<TaskDto> getAllTaskByDeveloperId(Long developerId);

	TaskDto updateTask(TaskDto dto);

	String deletById(Long taskId);

	List<TaskDto> getTaskOfDevelopers(String userCode);

	List<TaskDto> getTasksByDeveloperAndDateRange(Long developerId, LocalDate startDate, LocalDate endDate);

	TaskCompletionStatsDto getTaskCompletionStats(Long developerId, LocalDate startDate, LocalDate endDate);

	TaskCompletionStatsDto getTaskPerformanceByUserCode(String userCode);

	Map<String, Long> getTaskSubmitType(Long developerId);

	List<TaskDto> getAllTaskByDateWise(String userCode, LocalDate startDate, LocalDate endDate);

	String sentNotification(Long developerId);

}
