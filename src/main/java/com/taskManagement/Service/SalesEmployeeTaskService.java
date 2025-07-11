package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.SalesEmployeeTaskDto;
import com.taskManagement.outputdto.SalesEmployeeTaskOutputDto;

@Service
public interface SalesEmployeeTaskService {

	SalesEmployeeTaskDto addNewTask(SalesEmployeeTaskDto dto);

	List<SalesEmployeeTaskOutputDto> getAllEmployeeTaskOutput();

	SalesEmployeeTaskOutputDto getSalesEmployeeTaskByEmpId(Long empId);

	String addDailyTaskProgress(Long seTaskId, List<String> dailyProgress);

	List<SalesEmployeeTaskOutputDto> getAllTaskOutput(String userCode);

	String updateStatus(Long taskId, String status);

}
