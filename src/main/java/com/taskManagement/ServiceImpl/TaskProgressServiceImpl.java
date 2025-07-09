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
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.outputdto.OutputTask;

import jakarta.transaction.Transactional;
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
			log.info("data", dto.toString());
			TaskProgress taskProgress = this.mapper.toTaskProgress(dto);
			Task task = taskRepository.findById(dto.getTaskId())
					.orElseThrow(() -> new InvalidInputException("you provide invalid task Id whic is not present"));
			taskProgress.setTask(task);
			taskProgress.setTodayDate(LocalDate.now());
			taskProgress = this.progressRepo.saveAndFlush(taskProgress);
			response = this.mapper.toTaskProgressDto(taskProgress);
			response.setTaskId(taskProgress.getTask().getTaskId());
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

	@Override
	public TaskProgressDto updateTaskProgess(TaskProgressDto taskProgressDto) {
		TaskProgressDto dto = null;
		try {
			TaskProgress existing = this.progressRepo.findById(taskProgressDto.getTaskProgressId())
					.orElseThrow(() -> new InvalidInputException("Invalid task progress id"));

			Task task = this.taskRepository.findById(taskProgressDto.getTaskId())
					.orElseThrow(() -> new InvalidInputException("Invalid task id"));

			existing.setTodayTaskProgres(taskProgressDto.getTodayTaskProgres());
			existing.setTodayDate(taskProgressDto.getTodayDate());
			existing.setTask(task);

			progressRepo.saveAndFlush(existing);

			dto = mapper.toTaskProgressDto(existing);
			dto.setTaskId(task.getTaskId());

		} catch (Exception e) {
			log.error("Error occurred while updating the task progress data: {}", e.getMessage());
		}
		return dto;
	}

	@Override
	@Transactional
	public boolean deleteTaskProgres(Long taskProgresId) {
		try {
			TaskProgress taskProgress = this.progressRepo.findById(taskProgresId)
					.orElseThrow(() -> new InvalidInputException("Invalid input Id"));

			progressRepo.delete(taskProgress);
			return true;
		} catch (Exception e) {
			log.error("Error|Ouccreed in deleting task progress,{}", e.getMessage());
			return false;
		}
	}
}
