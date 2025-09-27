package com.taskManagement.Mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.taskManagement.Dtos.CustomLeadDto;
import com.taskManagement.Dtos.DealDTO;
import com.taskManagement.Dtos.DepartmentDTO;
import com.taskManagement.Dtos.DeveloperDto;
import com.taskManagement.Dtos.EmployeeDto;
import com.taskManagement.Dtos.InvoiceDTO;
import com.taskManagement.Dtos.JustDialLeadDto;
import com.taskManagement.Dtos.LeadAssignDto;
import com.taskManagement.Dtos.LeadDTO;
import com.taskManagement.Dtos.ProfileDto;
import com.taskManagement.Dtos.ProjectDto;
import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;
import com.taskManagement.Dtos.SalesEmployeeTaskDto;
import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Dtos.TaskProgressDto;
import com.taskManagement.Dtos.UserDto;
import com.taskManagement.Dtos.WebsiteLeadDto;
import com.taskManagement.Entitys.CustomLead;
import com.taskManagement.Entitys.DealEntity;
import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Employee;
import com.taskManagement.Entitys.Invoice;
import com.taskManagement.Entitys.JustDialLead;
import com.taskManagement.Entitys.Lead;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Entitys.Profile;
import com.taskManagement.Entitys.Project;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Entitys.SalesEmployeeTask;
import com.taskManagement.Entitys.Task;
import com.taskManagement.Entitys.TaskProgress;
import com.taskManagement.Entitys.User;
import com.taskManagement.Entitys.WebsiteLead;
import com.taskManagement.outputdto.LeadAssignOutPutDto;
import com.taskManagement.outputdto.OutputTask;
import com.taskManagement.outputdto.SalesEmployeeTaskOutputDto;

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

	@Mapping(target = "logo", ignore = true)
	Profile toProfile(ProfileDto profileDto);

	@Mapping(target = "logo", ignore = true)
	ProfileDto toProfileDto(Profile p);

	List<TaskDto> toTaskListDto(List<Task> list);

	SalesDepartmentEmployee toSalesDepartmentEmployee(SalesDepartmentEmployeeDto dto);

	SalesDepartmentEmployeeDto toSalesDepartmentEmployeeDto(SalesDepartmentEmployee salesDepartmentEmployee);

	SalesEmployeeTask toSalesEmployeeTask(SalesEmployeeTaskDto dto);

	SalesEmployeeTaskDto toSalesEmployeeTaskDto(SalesEmployeeTask task);

	SalesEmployeeTaskOutputDto toSalesEmployeeTaskDtoOutput(SalesEmployeeTask t);

	JustDialLead toJustDialLead(JustDialLeadDto dto);

	JustDialLeadDto toJustDialLeadDto(JustDialLead justDialLead);

	WebsiteLead dtoToEntity(WebsiteLeadDto dto);

	WebsiteLeadDto entityToDto(WebsiteLead savedEntity);

	CustomLead toCustomLead(CustomLeadDto dto);

	CustomLeadDto toCustomLeadDto(CustomLead lead);

	LeadAssign toLeadAssign(LeadAssignDto leadAssignDto);

	LeadAssignDto toLeadAssignDto(LeadAssign leadAssign);

	@Mapping(target = "leads", ignore = true)
	LeadAssignOutPutDto toLeadAssignOutPutDto(LeadAssign leadAssign);

	@Mapping(target = "proposalUpload", ignore = true)
	DealDTO toDealDto(DealEntity entity);

	Employee toEmployee(EmployeeDto dto);

	EmployeeDto toEmployeeDto(Employee employee);

	InvoiceDTO toInvoiceDTO(Invoice invoice);

	DepartmentDTO toDepartmentDto(Department department);

	Lead toLeadDto(LeadDTO dto);

}
