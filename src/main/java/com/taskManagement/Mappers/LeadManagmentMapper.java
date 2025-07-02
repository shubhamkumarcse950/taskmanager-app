package com.taskManagement.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.taskManagement.Dtos.DeveloperDto;
import com.taskManagement.Dtos.ProjectDto;
import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Dtos.TaskProgressDto;
import com.taskManagement.Dtos.UserDto;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Entitys.Task;
import com.taskManagement.Entitys.TaskProgress;
import com.taskManagement.Entitys.User;
import com.taskManagement.outputdto.OutputTask;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LeadManagmentMapper {
	User toUser(UserDto dto);

	UserDto toUserDto(User user);

	Project toProject(ProjectDto dto);

	ProjectDto toProjectDto(Project project);

	List<ProjectDto> toListProjectDto(List<Project> porProjects);

	Developer toDeveloper(DeveloperDto dto);

	DeveloperDto toDeveloperDto(Developer developer);

	List<DeveloperDto> toListDeveloperDto(List<Developer> developers);

	Task toTask(TaskDto dto);

	TaskDto toTaskDto(Task task);

	TaskProgress toTaskProgress(TaskProgressDto dto);

	TaskProgressDto toTaskProgressDto(TaskProgress taskProgress);

	OutputTask toOutputTask(Task tasks);

}
