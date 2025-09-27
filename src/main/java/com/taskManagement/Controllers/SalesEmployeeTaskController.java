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

import com.taskManagement.Dtos.SalesEmployeeTaskDto;
import com.taskManagement.Service.SalesEmployeeTaskService;
import com.taskManagement.outputdto.SalesEmployeeTaskOutputDto;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Sales-task-controller")
@RequestMapping("/sales-task")
public class SalesEmployeeTaskController {

	private final SalesEmployeeTaskService service;
	private final ResponseWithObject responseWithObject;

	public SalesEmployeeTaskController(SalesEmployeeTaskService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/add-new-task")
	public ResponseEntity<Object> addNewTask(@RequestBody SalesEmployeeTaskDto request) {
		request = this.service.addNewTask(request);
		if (Objects.isNull(request)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST,
					"something went wrong ,Error is comming!");
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.CREATED, request);

	}

	@GetMapping("/task-list")
	public ResponseEntity<Object> getTaskList() {
		List<SalesEmployeeTaskOutputDto> list = service.getAllEmployeeTaskOutput();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.OK, list.isEmpty());
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@GetMapping("/by-taskId")
	public ResponseEntity<Object> tMethodName(@RequestParam Long taskId) {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				service.getSalesEmployeeTaskByEmpId(taskId));
	}

	@PostMapping("/add-task-progress")
	public ResponseEntity<Object> addDailyTaskProgress(@RequestParam Long taskId,
			@RequestParam String dailyTaskReport) {
		String respose = this.service.addDailyTaskProgress(taskId, dailyTaskReport);
		if (respose.equals(AppConstants.SUCCESS)) {
			return responseWithObject.generateResponse("Task added succesfully!", HttpStatus.OK, respose);
		}
		return responseWithObject.generateResponse("some thing went worong !", HttpStatus.BAD_REQUEST, respose);
	}

	@GetMapping("/by-userCode")
	public ResponseEntity<Object> getMethodName(@RequestParam String userCode) {
		List<SalesEmployeeTaskOutputDto> salesEmployee = this.service.getAllTaskOutput(userCode);
		if (salesEmployee.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.OK, salesEmployee);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, salesEmployee);
	}

	@PutMapping("/task-id")
	public ResponseEntity<Object> updateTaskStatsu(@RequestParam Long taskId, @RequestParam String status) {
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
				service.updateStatus(taskId, status));

	}

	@DeleteMapping("/delete-task")
	public ResponseEntity<Object> deleteById(@RequestParam Long taskId) {
		return responseWithObject.generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK,
				service.deleteByTaskId(taskId));
	}

	@PutMapping("/update-status")
	public ResponseEntity<Object> putMethodName(@RequestParam Long taskId, @RequestParam String status) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.updateStatus(taskId, status));

	}
}
