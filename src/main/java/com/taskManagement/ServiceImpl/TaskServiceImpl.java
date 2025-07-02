package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Entitys.Task;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.ProjectRepository;
import com.taskManagement.Repository.TaskRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.TaskService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
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

	public TaskServiceImpl(TaskRepository taskRepository, LeadManagmentMapper mapper,
			ProjectRepository projectRepository, DeveloperRepository developerRepository, UserRepo userRepo) {
		super();
		this.taskRepository = taskRepository;
		this.mapper = mapper;
		this.developerRepository = developerRepository;
		this.projectRepository = projectRepository;
		this.userRepo = userRepo;
	}

	@Override
	@Transactional
	public TaskDto doTaskForDevelopers(TaskDto dto) {

		Task task = this.mapper.toTask(dto);
		this.projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new InvalidInputException(
				"you provide project id is not found in data base,please input correct id"));
		this.developerRepository.findById(dto.getDeveloperId()).orElseThrow(() -> new InvalidInputException(
				"you provide developer id is not found in data base ,please input correct id"));

		task = this.taskRepository.save(task);
		dto = this.mapper.toTaskDto(task);

		return dto;
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
						.orElseThrow(() -> new DataNotFoundException("DAta not found with developer id"));
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
		Task task = this.mapper.toTask(dto);
		this.projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new InvalidInputException(
				"you provide project id is not found in data base,please input correct id"));
		this.developerRepository.findById(dto.getDeveloperId()).orElseThrow(() -> new InvalidInputException(
				"you provide developer id is not found in data base ,please input correct id"));

		try {
			Optional<Task> optional = this.taskRepository.findById(dto.getTaskId());
			if (optional.isEmpty()) {
				throw new DataNotFoundException("Data not found with this task id");
			}
			task.setTaskId(optional.get().getTaskId());
			task = this.taskRepository.save(task);
			return this.mapper.toTaskDto(task);
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
	public List<Task> getTaskOfDevelopers(String userCode) {
		try {
			Optional<Developer> optional = this.developerRepository.findByUserCode(userCode);
			if (optional.isEmpty()) {
				throw new DataNotFoundException("Data not found with this user Code!");
			}
			List<Task> getList = this.taskRepository.findByDeveloperId(optional.get().getDeveloperId());
			if (getList.isEmpty()) {
				throw new DataNotFoundException("Task not found with this user Code");
			}
			return getList;
		} catch (DataNotFoundException | HibernateException e) {
			log.error("Error| Ouccred in fetching data from dataBase!{}", e.getMessage());
			return java.util.Collections.emptyList();
		}
	}

}
