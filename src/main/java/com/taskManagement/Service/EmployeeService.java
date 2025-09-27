package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.EmployeeDto;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.outputdto.EmployeeResponse;

@Service
public interface EmployeeService {

	EmployeeDto saveEmployeeInfo(EmployeeDto dto);

	EmployeeDto updateEmployeeInfo(EmployeeDto dto);

	List<EmployeeDto> getAllEmployees();

	EmployeeDto getEmployeeById(Long employeeId);

	EmployeeDto getEmployeeByUserCode(String userCode);

	List<EmployeeDto> getEmployeesByDepartmentId(Long departmentId);

	String deleteEmployeeInfo(Long employeeId, Long depId);

	List<EmployeeResponse> getAllEmployeeResponsesWithIncludeSaleTableAndDeveloperTable()
			throws DataNotFoundException, RuntimeException;
}