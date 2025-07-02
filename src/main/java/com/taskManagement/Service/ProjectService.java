package com.taskManagement.Service;

import java.util.List;

import com.taskManagement.Dtos.ProjectDto;
import com.taskManagement.Entitys.Project;

public interface ProjectService {

	List<ProjectDto> getAllProjectDetails();

	ProjectDto getOneProject(Long projectId);

	ProjectDto updataProjectDetails(ProjectDto dto);

	String deletProject(Long projectId);

	ProjectDto saveProjectDetails(ProjectDto dto);

	ProjectDto addDeveloperInProject(ProjectDto dto);

	List<Object> getAllAssignDeveloperInProject(Long projectId);

	List<Project> getAllAssignProject(String userCode);

}
