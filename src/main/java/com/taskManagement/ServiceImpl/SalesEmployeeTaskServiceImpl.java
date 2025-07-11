package com.taskManagement.ServiceImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.SalesEmployeeTaskDto;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Entitys.SalesEmployeeTask;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.SalesDepartmentEmployeeRepo;
import com.taskManagement.Repository.SalesEmployeeTaskRepo;
import com.taskManagement.Service.SalesEmployeeTaskService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InternalServerException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.outputdto.SalesEmployeeTaskOutputDto;
import com.taskManagement.responsemodel.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalesEmployeeTaskServiceImpl implements SalesEmployeeTaskService {

	private final SalesEmployeeTaskRepo employeeTaskRepo;
	private final LeadManagmentMapper mapper;
	private final SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo;

	public SalesEmployeeTaskServiceImpl(SalesEmployeeTaskRepo employeeTaskRepo, LeadManagmentMapper mapper,
			SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo) {
		super();
		this.employeeTaskRepo = employeeTaskRepo;
		this.mapper = mapper;
		this.salesDepartmentEmployeeRepo = salesDepartmentEmployeeRepo;
	}

	@Override
	public SalesEmployeeTaskDto addNewTask(SalesEmployeeTaskDto dto) {
		try {
			SalesEmployeeTask task = mapper.toSalesEmployeeTask(dto);
			SalesDepartmentEmployee salesDepartmentEmployee = this.salesDepartmentEmployeeRepo.findById(dto.getEmpId())
					.orElseThrow(() -> new InvalidInputException("Sales Department emloyee id is not found in DB"));
			task.setSalesDepartmentEmployee(salesDepartmentEmployee);
			task = this.employeeTaskRepo.saveAndFlush(task);
			dto = mapper.toSalesEmployeeTaskDto(task);
			dto.setEmpId(task.getSalesDepartmentEmployee().getEmpId());
			return dto;
		} catch (InvalidInputException e) {
			log.error("Invalid input Error or Hibernate error,{}", e.getMessage());
			throw new InvalidInputException("sales employee id not found in DB");
		} catch (Exception e) {
			log.error("Erorr|something went wrong ,interanl server error,{}", e.getMessage());
			throw new InternalServerException("Internal server Error,something went wrong!");
		}
	}

	@Override
	public List<SalesEmployeeTaskOutputDto> getAllEmployeeTaskOutput() {
		List<SalesEmployeeTask> task = employeeTaskRepo.findAll();
		return task.stream().filter(t -> t.getSeTaskId() != null).map(e -> {
			SalesEmployeeTaskOutputDto dto = mapper.toSalesEmployeeTaskDtoOutput(e);
			return dto;
		}).toList();
	}

	@Override
	public SalesEmployeeTaskOutputDto getSalesEmployeeTaskByEmpId(Long taskId) {
		SalesEmployeeTask task = this.employeeTaskRepo.findById(taskId)
				.orElseThrow(() -> new InvalidInputException("task id not fund in DB"));
		return mapper.toSalesEmployeeTaskDtoOutput(task);
	}

	@Override
	public String addDailyTaskProgress(Long seTaskId, List<String> dailyProgress) {
		try {
			if (dailyProgress.isEmpty()) {
				throw new InvalidInputException("daily task progerss List is empty,plese provide a valid task list");
			}
			SalesEmployeeTask task = this.employeeTaskRepo.findById(seTaskId)
					.orElseThrow(() -> new DataNotFoundException("task not foud with this task Id"));
			task.setDailyTask(dailyProgress);
			employeeTaskRepo.saveAndFlush(task);
			return AppConstants.SUCCESS;
		} catch (InvalidInputException | HibernateException e) {
			log.error("Erorr|Ouccrred in saving task List,{}", e.getMessage());
			return AppConstants.ERROR;
		}
	}

	@Override
	public List<SalesEmployeeTaskOutputDto> getAllTaskOutput(String userCode) {
		SalesDepartmentEmployee employee = this.salesDepartmentEmployeeRepo.findByUserCode(userCode)
				.orElseThrow(() -> new InvalidInputException("User code is not found in DB"));

		List<SalesEmployeeTask> list = employeeTaskRepo.findBySalesDepartmentEmployee_EmpId(employee.getEmpId());
		return list.stream().map(m -> mapper.toSalesEmployeeTaskDtoOutput(m)).toList();
	}

	@Override
	public String updateStatus(Long taskId, String status) {
		SalesEmployeeTask task = this.employeeTaskRepo.findById(taskId)
				.orElseThrow(() -> new InvalidInputException("task not found with this task id"));
		task.setStatus(status);
		employeeTaskRepo.save(task);
		return AppConstants.SUCCESS;
	}
}
