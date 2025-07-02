package com.taskManagement.Service;

import java.util.List;

import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Entitys.Task;

public interface TaskService {

	TaskDto doTaskForDevelopers(TaskDto dto);

	List<TaskDto> getAllTask();

	List<TaskDto> getAllTaskByDeveloperId(Long developerId);

	TaskDto updateTask(TaskDto dto);

	String deletById(Long taskId);

	List<Task> getTaskOfDevelopers(String userCode);

}
