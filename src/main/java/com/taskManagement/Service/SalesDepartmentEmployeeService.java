package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;
import com.taskManagement.Entitys.SalesDepartmentEmployee;

@Service
public interface SalesDepartmentEmployeeService {

	SalesDepartmentEmployeeDto addSaleseEmployee(SalesDepartmentEmployeeDto dto);

	boolean deletedSalesEmployee(Long employeeId);

	List<SalesDepartmentEmployeeDto> getAllSalesDepartmentEmployee();

	SalesDepartmentEmployeeDto updatEmployeeDto(SalesDepartmentEmployeeDto dto);

	SalesDepartmentEmployeeDto getEmployeeByUserCode(String userCode);

	void addSalesEmployee(SalesDepartmentEmployee salesDepartmentEmployee, String password);

	void updateSalesEmployeeRegistration(SalesDepartmentEmployee salesDepartmentEmployee);

	String deletedSalesEmployeeByEmployeApi(Long employeeId);

}
