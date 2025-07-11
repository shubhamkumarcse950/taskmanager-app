package com.taskManagement.Controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;
import com.taskManagement.Service.SalesDepartmentEmployeeService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Sales Department")
@RequestMapping("/sales")
public class SalesDepartmentEmployeeController {

	private final SalesDepartmentEmployeeService service;
	private final ResponseWithObject responseWithObject;

	public SalesDepartmentEmployeeController(SalesDepartmentEmployeeService service,
			ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/add-new")
	public ResponseEntity<Object> addNewEmployee(@RequestBody SalesDepartmentEmployeeDto salesDepartmentEmployeeDto) {
		salesDepartmentEmployeeDto = this.service.addSaleseEmployee(salesDepartmentEmployeeDto);
		if (Objects.isNull(salesDepartmentEmployeeDto)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST,
					"Error in saving the sales employee data");
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.CREATED,
				salesDepartmentEmployeeDto);

	}

	@DeleteMapping("/delete-Employee")
	public ResponseEntity<Object> deleteEmployee(@RequestParam("salesEmpId") Long salesEmpId) {
		boolean response = this.service.deletedSalesEmployee(salesEmpId);
		if (response) {
			return responseWithObject.generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK, response);
		}
		return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, response);
	}

	@GetMapping("/emp-List")
	public ResponseEntity<Object> getEmpList() {
		List<SalesDepartmentEmployeeDto> list = service.getAllSalesDepartmentEmployee();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NO_CONTENT, list);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@PutMapping("update/emp-data")
	public ResponseEntity<Object> updateEmpData(@RequestBody SalesDepartmentEmployeeDto entity) {
		entity = this.service.updatEmployeeDto(entity);
		if (Objects.isNull(entity)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST,
					"Error in updating employee data");
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, entity);

	}

	@GetMapping("/byUserCode")
	public ResponseEntity<Object> getEmployeeData(@RequestParam String userCode) {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				service.getEmployeeByUserCode(userCode));
	}
}
