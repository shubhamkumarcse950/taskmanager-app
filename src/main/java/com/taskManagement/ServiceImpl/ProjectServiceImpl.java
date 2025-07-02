package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.ProjectDto;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.ProjectRepository;
import com.taskManagement.Service.ProjectService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final LeadManagmentMapper mapper;
	private final DeveloperRepository developerRepository;

	public ProjectServiceImpl(ProjectRepository projectRepository, LeadManagmentMapper mapper,
			DeveloperRepository developerRepository) {

		this.projectRepository = projectRepository;
		this.mapper = mapper;
		this.developerRepository = developerRepository;
	}

	@Override
	@Transactional
	public ProjectDto saveProjectDetails(ProjectDto dto) {

		try {
			Project project = this.mapper.toProject(dto);
			project = this.projectRepository.save(project);
			dto = this.mapper.toProjectDto(project);
		} catch (InvalidInputException e) {
			log.error("Internal service error!!", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public List<ProjectDto> getAllProjectDetails() {
		List<ProjectDto> list = new ArrayList<>();
		try {
			List<Project> porProjects = this.projectRepository.findAll();
			list = this.mapper.toListProjectDto(porProjects);
		} catch (DataNotFoundException e) {
			log.error("Data not found in dataBase!!", e.getMessage());
			throw e;
		}
		return list;
	}

	@Override
	public ProjectDto getOneProject(Long projectId) {

		Project optional = this.projectRepository.findById(projectId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID));
		return mapper.toProjectDto(optional);

	}

	@Override
	public ProjectDto updataProjectDetails(ProjectDto dto) {
		Project project = this.mapper.toProject(dto);
		try {
			Optional<Project> optional = this.projectRepository.findById(dto.getProjectId());
			if (optional.isEmpty()) {
				throw new InvalidInputException("You provided invalid project id,this id is not found in dataBaase!");
			}
			project.setProjectId(optional.get().getProjectId());
			project = this.projectRepository.save(project);
			return mapper.toProjectDto(project);
		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Internal service error!!", e.getMessage());
			throw e;
		}
	}

	@Override
	public String deletProject(Long projectId) {
		String responnse = AppConstants.DELETED_SUCCESFULLY;
		Project poProject = this.projectRepository.findById(projectId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID));
		this.projectRepository.deleteById(poProject.getProjectId());
		return responnse;
	}

	@Override
	public ProjectDto addDeveloperInProject(ProjectDto dto) {
		Project project = this.mapper.toProject(dto);
		List<Long> developerIdList = dto.getDeveloperId();
		Iterator<Long> iterator = developerIdList.iterator();
		while (iterator.hasNext()) {
			Long developerId = iterator.next();
			Optional<Developer> optional = this.developerRepository.findById(developerId);
			if (optional.isEmpty()) {
				log.warn("You provided an invalid developer ID: {}. It is not present in the database!", developerId);
				iterator.remove();
			}
			if (developerIdList.isEmpty()) {
				throw new InvalidInputException("you provide all developer id wrong .it is not present in data base!!");
			}
		}
		try {
			Optional<Project> optional = this.projectRepository.findById(dto.getProjectId());
			if (optional.isEmpty()) {
				throw new DataNotFoundException("Project not found in data base with you provide which id!!");
			}
			project.setProjectId(optional.get().getProjectId());
			project.setPlannedEndDate(optional.get().getPlannedEndDate());
			project.setPlannedStartDate(optional.get().getPlannedStartDate());
			project.setProjectName(optional.get().getProjectName());
			project.setDeveloperId(developerIdList);
			project = this.projectRepository.save(project);
			dto = this.mapper.toProjectDto(project);
		} catch (DataNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public List<Object> getAllAssignDeveloperInProject(Long projectId) {
		List<Object> objects = new CopyOnWriteArrayList<>();
		try {
			Optional<Project> projecOptional = this.projectRepository.findById(projectId);
			if (projecOptional.isEmpty()) {
				throw new InvalidInputException("Project not found!!");
			}
			List<Long> developerIds = projecOptional.get().getDeveloperId();
			List<Developer> developers = new ArrayList<>();
			for (Long long1 : developerIds) {
				Optional<Developer> optional = this.developerRepository.findById(long1);
				if (optional.isEmpty()) {
					log.warn("Developer not found with this id{}", projectId);
					continue;
				}

				developers.add(optional.get());

			}
			objects.add(projecOptional.get());
			objects.add(developers);
		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		}
		return objects;
	}

	@Override
	public List<Project> getAllAssignProject(String userCode) {
		try {
			Optional<Developer> developerOptional = this.developerRepository.findByUserCode(userCode);
			if (developerOptional.isEmpty()) {
				throw new DataNotFoundException("Assign project not found with this userCode");
			}

			Long developerIdToCheck = developerOptional.get().getDeveloperId();
			List<Project> allProjects = this.projectRepository.findAll();

			List<Project> assignedProjects = new ArrayList<>();

			for (Project project : allProjects) {
				List<Long> developerIds = project.getDeveloperId();
				if (developerIds != null && developerIds.contains(developerIdToCheck)) {
					assignedProjects.add(project);
				}
			}

			return assignedProjects;
		} catch (DataNotFoundException | HibernateException e) {
			log.error(AppConstants.NO_DATA_FOUND, e.getMessage());
			return Collections.emptyList();
		}
	}

}
