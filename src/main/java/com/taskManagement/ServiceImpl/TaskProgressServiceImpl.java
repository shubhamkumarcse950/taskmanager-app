package com.taskManagement.ServiceImpl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.TaskProgressDto;
import com.taskManagement.Entitys.Task;
import com.taskManagement.Entitys.TaskProgress;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.TaskProgressRepo;
import com.taskManagement.Repository.TaskRepository;
import com.taskManagement.Service.TaskProgressService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.outputdto.OutputTask;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskProgressServiceImpl implements TaskProgressService {

	private final TaskProgressRepo progressRepo;
	private final LeadManagmentMapper mapper;
	private final TaskRepository taskRepository;

	public TaskProgressServiceImpl(TaskProgressRepo progressRepo, LeadManagmentMapper mapper,
			TaskRepository taskRepository) {
		super();
		this.progressRepo = progressRepo;
		this.mapper = mapper;
		this.taskRepository = taskRepository;
	}

	@Override
	public TaskProgressDto saveTaskProgress(TaskProgressDto dto) {
		TaskProgressDto response = null;
		try {
			TaskProgress taskProgress = this.mapper.toTaskProgress(dto);
			taskProgress.setTodayDate(LocalDate.now());
			taskProgress = this.progressRepo.saveAndFlush(taskProgress);
			response = this.mapper.toTaskProgressDto(taskProgress);
		} catch (Exception e) {
			log.error("Error|Ouccreed in saving the task progress data,{}", e.getMessage());
		}
		return response;
	}

	@Override
	public OutputTask getAllProgressTaskWithTask(Long taskId) {
		OutputTask outputTask = null;
		try {
			Task tasks = this.taskRepository.findById(taskId)
					.orElseThrow(() -> new DataNotFoundException("Task not found with this id"));
			outputTask = mapper.toOutputTask(tasks);

		} catch (Exception e) {
			log.error("Error|Ouccreed in getting the data ,{}", e.getMessage());

		}
		return outputTask;
	}
}
