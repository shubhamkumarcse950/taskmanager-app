package com.taskManagement.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.EmployeeDto;
import com.taskManagement.Service.EmployeeService;
import com.taskManagement.outputdto.EmployeeResponse;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping("/register")
	public ResponseEntity<EmployeeDto> registerEmployee(@RequestBody EmployeeDto dto) {
		EmployeeDto savedDto = employeeService.saveEmployeeInfo(dto);
		return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto dto) {
		EmployeeDto updatedDto = employeeService.updateEmployeeInfo(dto);
		return new ResponseEntity<>(updatedDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
		EmployeeDto employee = employeeService.getEmployeeById(id);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@GetMapping("/user-code/{userCode}")
	public ResponseEntity<EmployeeDto> getEmployeeByUserCode(@PathVariable String userCode) {
		EmployeeDto employee = employeeService.getEmployeeByUserCode(userCode);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@GetMapping("/department/{departmentId}")
	public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
		List<EmployeeDto> employees = employeeService.getEmployeesByDepartmentId(departmentId);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteEmployee(@RequestParam Long id, @RequestParam Long depId) {
		String message = employeeService.deleteEmployeeInfo(id, depId);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@GetMapping("/getAll-employee")
	public ResponseEntity<List<EmployeeResponse>> getEmployeeWithDeveloperTableAndSalesEmployeeTable() {
		List<EmployeeResponse> listOfEmployee = employeeService
				.getAllEmployeeResponsesWithIncludeSaleTableAndDeveloperTable();
		return new ResponseEntity<>(listOfEmployee, HttpStatus.OK);
	}

}