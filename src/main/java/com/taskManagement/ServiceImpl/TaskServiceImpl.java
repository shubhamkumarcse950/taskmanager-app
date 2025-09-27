package com.taskManagement.ServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.NotificationDto;
import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Entitys.Task;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.ProjectRepository;
import com.taskManagement.Repository.TaskRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.TaskService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.outputdto.TaskCompletionStatsDto;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	private final LeadManagmentMapper mapper;
	private final DeveloperRepository developerRepository;
	private final ProjectRepository projectRepository;
	private final UserRepo userRepo;
	private final NotificationService notificationService;

	public TaskServiceImpl(TaskRepository taskRepository, LeadManagmentMapper mapper,
			ProjectRepository projectRepository, DeveloperRepository developerRepository, UserRepo userRepo,
			NotificationService notificationService) {
		this.taskRepository = taskRepository;
		this.mapper = mapper;
		this.developerRepository = developerRepository;
		this.projectRepository = projectRepository;
		this.userRepo = userRepo;
		this.notificationService = notificationService;
	}

	@Override
	public String sentNotification(Long developerId) throws RuntimeException {
		NotificationDto notification = NotificationDto.builder().message("New Task Assign")
				.title("This notification is created for testing purpose!..").type("Task")
				.timestamp(LocalDate.now().toString()).build();
		notificationService.sendToUser(getUserName(developerId), notification);
		return AppConstants.SUCCESS;
	}

	@Override
	@Transactional
	public TaskDto doTaskForDevelopers(TaskDto dto) {
		Task task = this.mapper.toTask(dto);
		this.projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new InvalidInputException(
				"you provide project id is not found in data base,please input correct id"));
		this.developerRepository.findById(dto.getDeveloperId()).orElseThrow(() -> new InvalidInputException(
				"you provide developer id is not found in data base ,please input correct id"));
		task.setTaskSubmitType(null);
		task = this.taskRepository.save(task);
		NotificationDto notification = NotificationDto.builder().message("New Task Assign").title(dto.getTitle())
				.type("Task").timestamp(LocalDate.now().toString()).build();

		notificationService.sendToUser(getUserName(dto.getDeveloperId()), notification);
		dto = this.mapper.toTaskDto(task);
		return dto;
	}

	private String getUserName(Long developerId) {
		Developer developer = developerRepository.findById(developerId).orElseThrow(
				() -> new InvalidInputException("Developer not found with this developer id: " + developerId));
		User user = userRepo.findByUserCode(developer.getUserCode()).orElseThrow(() -> new UsernameNotFoundException(
				"User name not found with this user code" + developer.getUserCode()));

		return user.getEmail();
	}

	@Override
	public List<TaskDto> getAllTask() {
		List<TaskDto> list = new ArrayList<>();
		try {
			List<Task> taskList = taskRepository.findAll();
			for (Task task : taskList) {
				TaskDto dto = mapper.toTaskDto(task);
				Optional<Project> projectOptional = this.projectRepository.findById(task.getProjectId());
				if (projectOptional.isEmpty()) {
					log.warn("The Project id {} is not present in the database!", task.getProjectId());
					continue;
				}
				dto.setProjectName(projectOptional.get().getProjectName());
				Optional<Developer> developerOptional = this.developerRepository.findById(task.getDeveloperId());
				if (developerOptional.isEmpty()) {
					log.warn("The developer id {} is not present in the database!", task.getDeveloperId());
					continue;
				}
				dto.setDeveloperName(developerOptional.get().getFullName());
				list.add(dto);
			}
			return list;
		} catch (DataNotFoundException e) {
			log.warn("Task list is empty!", e.getMessage());
			return java.util.Collections.emptyList();
		}
	}

	@Override
	public List<TaskDto> getAllTaskByDeveloperId(Long developerId) {
		List<TaskDto> list = new ArrayList<>();
		try {
			List<Task> tasks = this.taskRepository.findByDeveloperId(developerId);
			for (Task task : tasks) {
				TaskDto dto = this.mapper.toTaskDto(task);
				Developer developerOptional = this.developerRepository.findById(task.getDeveloperId())
						.orElseThrow(() -> new DataNotFoundException("Data not found with developer id"));
				Optional<Project> project = this.projectRepository.findById(task.getProjectId());
				if (project.isEmpty()) {
					log.warn("Project is not found in data base");
					continue;
				}
				dto.setDeveloperName(developerOptional.getFullName());
				dto.setProjectName(project.get().getProjectName());
				list.add(dto);
			}
			return list;
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public TaskDto updateTask(TaskDto dto) {
		this.projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new InvalidInputException(
				"you provide project id is not found in data base,please input correct id"));
		this.developerRepository.findById(dto.getDeveloperId()).orElseThrow(() -> new InvalidInputException(
				"you provide developer id is not found in data base ,please input correct id"));
		try {
			Optional<Task> optional = this.taskRepository.findById(dto.getTaskId());
			if (optional.isEmpty()) {
				throw new DataNotFoundException("Data not found with this task id");
			}
			Task existingTask = optional.get();
			Task updatedTask = this.mapper.toTask(dto);
			Long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(existingTask.getEndDate(), LocalDate.now());
			existingTask.setSubmitDay(daysDiff);
			existingTask.setTaskSubmitType(taskSubmitType(dto.getStatus(), existingTask.getEndDate(), LocalDate.now()));
			existingTask.setTitle(updatedTask.getTitle());
			existingTask.setDescription(updatedTask.getDescription());
			existingTask.setStatus(updatedTask.getStatus());
			existingTask.setPriority(updatedTask.getPriority());
			existingTask.setStartDate(updatedTask.getStartDate());
			existingTask.setEndDate(updatedTask.getEndDate());
			existingTask.setDueDate(updatedTask.getDueDate());
			existingTask.setDeveloperId(updatedTask.getDeveloperId());
			existingTask.setProjectId(updatedTask.getProjectId());
			existingTask.setUserCode(updatedTask.getUserCode());
			if (updatedTask.getTaskProgresses() != null) {
				existingTask.getTaskProgresses().clear();
				existingTask.getTaskProgresses().addAll(updatedTask.getTaskProgresses());
			}
			Task saved = this.taskRepository.save(existingTask);
			return this.mapper.toTaskDto(saved);
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public String deletById(Long taskId) {
		String response = AppConstants.DELETED_SUCCESFULLY;
		Task optional = this.taskRepository.findById(taskId)
				.orElseThrow(() -> new DataNotFoundException("Task data not found with this id"));
		this.taskRepository.deleteById(optional.getTaskId());
		return response;
	}

	@Override
	public List<TaskDto> getTaskOfDevelopers(String userCode) {
		try {
			List<Task> list = taskRepository.findByUserCode(userCode);
			return mapper.toTaskListDto(list);
		} catch (Exception e) {
			log.error("Error occurred in fetching data from database! {}", e.getMessage());
			return java.util.Collections.emptyList();
		}
	}

	@Override
	public List<TaskDto> getTasksByDeveloperAndDateRange(Long developerId, LocalDate startDate, LocalDate endDate) {
		try {
			Developer developer = developerRepository.findById(developerId)
					.orElseThrow(() -> new InvalidInputException("Developer not found with id: " + developerId));
			if (startDate == null || endDate == null) {
				throw new InvalidInputException("Start date and end date must not be null");
			}
			if (startDate.isAfter(endDate)) {
				throw new InvalidInputException("Start date must be before or equal to end date");
			}
			List<Task> tasks = taskRepository.findByDeveloperIdAndStartDateBetween(developerId, startDate, endDate);
			List<TaskDto> taskDtos = new ArrayList<>();
			for (Task task : tasks) {
				TaskDto dto = mapper.toTaskDto(task);
				Optional<Project> projectOptional = projectRepository.findById(task.getProjectId());
				if (projectOptional.isEmpty()) {
					log.warn("Project id {} not found for task id {}", task.getProjectId(), task.getTaskId());
					continue;
				}
				dto.setProjectName(projectOptional.get().getProjectName());
				dto.setDeveloperName(developer.getFullName());
				taskDtos.add(dto);
			}
			if (taskDtos.isEmpty()) {
				log.warn("No tasks found for developer id {} between {} and {}", developerId, startDate, endDate);
			}
			return taskDtos;
		} catch (InvalidInputException e) {
			log.error("Invalid input: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error fetching tasks for developer id {} between {} and {}: {}", developerId, startDate, endDate,
					e.getMessage());
			throw new DataNotFoundException("Failed to fetch tasks: " + e.getMessage());
		}
	}

	@Override
	public TaskCompletionStatsDto getTaskCompletionStats(Long developerId, LocalDate startDate, LocalDate endDate) {
		try {
			Developer developer = developerRepository.findById(developerId)
					.orElseThrow(() -> new InvalidInputException("Developer not found with id: " + developerId));
			if (startDate == null || endDate == null) {
				throw new InvalidInputException("Start date and end date must not be null");
			}
			if (startDate.isAfter(endDate)) {
				throw new InvalidInputException("Start date must be before or equal to end date");
			}
			List<Task> tasks = taskRepository.findByDeveloperIdAndStartDateBetween(developerId, startDate, endDate);
			if (tasks.isEmpty()) {
				log.warn("No tasks found for developer id {} between {} and {}", developerId, startDate, endDate);
				return new TaskCompletionStatsDto(developerId, developer.getFullName(), 0, 0, 0, 0, 0, 0.0, 0.0, 0.0,
						0.0);
			}
			long completedTasks = 0;
			long onTimeCompletedTasks = 0;
			long pendingTasks = 0;
			long inProgressTasks = 0;
			LocalDate currentDate = LocalDate.now();
			for (Task task : tasks) {
				String status = task.getStatus() != null ? task.getStatus() : "Pending";
				boolean isOverdue = task.getEndDate() != null && task.getEndDate().isBefore(currentDate);
				if ("Completed".equalsIgnoreCase(status)) {
					completedTasks++;
					if (task.getEndDate() != null && !task.getEndDate().isBefore(currentDate)) {
						onTimeCompletedTasks++;
					}
				} else if ("In Progress".equalsIgnoreCase(status) && !isOverdue) {
					inProgressTasks++;
				} else {
					pendingTasks++;
				}
			}
			long totalTasks = tasks.size();
			double completedPercentage = (double) completedTasks / totalTasks * 100;
			double onTimeCompletedPercentage = (double) onTimeCompletedTasks / totalTasks * 100;
			double pendingPercentage = (double) pendingTasks / totalTasks * 100;
			double inProgressPercentage = (double) inProgressTasks / totalTasks * 100;
			return new TaskCompletionStatsDto(developerId, developer.getFullName(), totalTasks, completedTasks,
					onTimeCompletedTasks, pendingTasks, inProgressTasks, completedPercentage, onTimeCompletedPercentage,
					pendingPercentage, inProgressPercentage);
		} catch (InvalidInputException e) {
			log.error("Invalid input: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error calculating task stats for developer id {} between {} and {}: {}", developerId, startDate,
					endDate, e.getMessage());
			throw new DataNotFoundException("Failed to calculate task stats: " + e.getMessage());
		}
	}

	@Override
	public TaskCompletionStatsDto getTaskPerformanceByUserCode(String userCode) {
		try {
			if (userCode == null || userCode.trim().isEmpty()) {
				throw new InvalidInputException("User code must not be null or empty");
			}
			Optional<Developer> developerOptional = developerRepository.findByUserCode(userCode);
			if (developerOptional.isEmpty()) {
				throw new InvalidInputException("Developer not found with user code: " + userCode);
			}
			Developer developer = developerOptional.get();
			Long developerId = developer.getDeveloperId();
			List<Task> tasks = taskRepository.findByDeveloperId(developerId);
			if (tasks.isEmpty()) {
				log.warn("No tasks found for developer with user code: {}", userCode);
				return new TaskCompletionStatsDto(developerId, developer.getFullName(), 0, 0, 0, 0, 0, 0.0, 0.0, 0.0,
						0.0);
			}
			long completedTasks = 0;
			long onTimeCompletedTasks = 0;
			long pendingTasks = 0;
			long inProgressTasks = 0;
			LocalDate currentDate = LocalDate.now();
			for (Task task : tasks) {
				String status = task.getStatus() != null ? task.getStatus() : "Pending";
				boolean isOverdue = task.getEndDate() != null && task.getEndDate().isBefore(currentDate);
				if ("Completed".equalsIgnoreCase(status)) {
					completedTasks++;
					if (task.getEndDate() != null && !task.getEndDate().isBefore(currentDate)) {
						onTimeCompletedTasks++;
					}
				} else if ("In Progress".equalsIgnoreCase(status) && !isOverdue) {
					inProgressTasks++;
				} else {
					pendingTasks++;
				}
			}
			long totalTasks = tasks.size();
			double completedPercentage = (double) completedTasks / totalTasks * 100;
			double onTimeCompletedPercentage = (double) onTimeCompletedTasks / totalTasks * 100;
			double pendingPercentage = (double) pendingTasks / totalTasks * 100;
			double inProgressPercentage = (double) inProgressTasks / totalTasks * 100;
			return new TaskCompletionStatsDto(developerId, developer.getFullName(), totalTasks, completedTasks,
					onTimeCompletedTasks, pendingTasks, inProgressTasks, completedPercentage, onTimeCompletedPercentage,
					pendingPercentage, inProgressPercentage);
		} catch (InvalidInputException e) {
			log.error("Invalid input: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error calculating task performance for user code {}: {}", userCode, e.getMessage());
			throw new DataNotFoundException("Failed to calculate task performance: " + e.getMessage());
		}
	}

	private String taskSubmitType(String status, LocalDate currentDate, LocalDate endDate) {
		if (!"Completed".equalsIgnoreCase(status)) {
			return null;
		}

		Long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(endDate, currentDate);

		if (daysDiff < 0) {
			return "Excellent";
		} else if (daysDiff == 0) {
			return "Good";
		} else if (daysDiff == 1) {
			return "Average";
		} else if (daysDiff == 2) {
			return "Bad";
		} else if (daysDiff >= 5) {
			return "Poor";
		} else {
			return "Delay";
		}
	}

	@Override
	public Map<String, Long> getTaskSubmitType(Long developerId) {
		List<Task> listOfTasks = this.taskRepository.findByDeveloperId(developerId);

		Map<String, Long> result = new HashMap<>();

		List<Task> completedTasks = listOfTasks.stream()
				.filter(task -> "Completed".equalsIgnoreCase(task.getStatus()) && task.getTaskSubmitType() != null)
				.toList();

		long total = completedTasks.size();
		if (total == 0) {
			return result;
		}

		Map<String, Long> countMap = completedTasks.stream()
				.collect(Collectors.groupingBy(Task::getTaskSubmitType, Collectors.counting()));

		for (Map.Entry<String, Long> entry : countMap.entrySet()) {
			long percentage = Math.round((entry.getValue() * 100.0) / total);
			result.put(entry.getKey(), percentage);
		}

		return result;
	}

	@Override
	public List<TaskDto> getAllTaskByDateWise(String userCode, LocalDate startDate, LocalDate endDate) {
		Developer developer = developerRepository.findByUserCode(userCode)
				.orElseThrow(() -> new DataNotFoundException("developr not found with this userCode"));

		List<Task> list = taskRepository.findByDeveloperIdAndStartDateBetween(developer.getDeveloperId(), startDate,
				endDate);
		return list.stream().map(task -> mapper.toTaskDto(task)).toList();
	}
}
