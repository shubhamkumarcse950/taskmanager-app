package com.taskManagement.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

import com.taskManagement.Dtos.TaskDto;
import com.taskManagement.Service.TaskService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
@Tag(name = "Task-API")
@CrossOrigin(origins = "*")
public class TaskController {

	private final TaskService service;
	private final ResponseWithObject responseWithObject;

	public TaskController(TaskService service, ResponseWithObject responseWithObject) {
		super();
		this.service = service;
		this.responseWithObject = responseWithObject;
	}

	@PostMapping("/doTask")
	public ResponseEntity<Object> doTask(@Valid @RequestBody TaskDto entity) {
		TaskDto dto = this.service.doTaskForDevelopers(entity);
		if (dto == null) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST,
					AppConstants.INVALID_INPUT_DATA);
		}

		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, dto);
	}

	@GetMapping("/getAllTask")
	public ResponseEntity<Object> getAllTask() {
		List<TaskDto> list = this.service.getAllTask();
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NOT_FOUND,
					AppConstants.NA);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, list);
	}

	@GetMapping("/getTaskByDeveloperId")
	public ResponseEntity<Object> getAllTaskByDeveloperId(@RequestParam Long developerId) {
		List<TaskDto> list = this.service.getAllTaskByDeveloperId(developerId);
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NO_CONTENT,
					AppConstants.NA);
		}
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, list);

	}

	@PutMapping("/updateTask")
	public ResponseEntity<Object> updateTask(@RequestBody TaskDto entity) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, service.updateTask(entity));

	}

	@DeleteMapping("/deleteById")
	public ResponseEntity<Object> deletById(@RequestParam("taskId") Long taskId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK, service.deletById(taskId));
	}

	@GetMapping("/getAllGivenTaskByUserCode")
	public ResponseEntity<Object> getAllTaskOfDeveloper(@RequestParam String userCode) {
		List<TaskDto> list = this.service.getTaskOfDevelopers(userCode);
		if (list.isEmpty()) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NOT_FOUND, list);
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@GetMapping("/getTasksByDeveloperAndDateRange")
	public ResponseEntity<Object> getTasksByDeveloperAndDateRange(@RequestParam Long developerId,
			@RequestParam String startDate, @RequestParam String endDate) {
		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			List<TaskDto> list = service.getTasksByDeveloperAndDateRange(developerId, start, end);
			if (list.isEmpty()) {
				return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NO_CONTENT, list);
			}
			return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
		} catch (Exception e) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getTaskStatsByDeveloper")
	public ResponseEntity<Object> getTaskStatsByDeveloper(@RequestParam Long developerId,
			@RequestParam String startDate, @RequestParam String endDate) {
		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);
			return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
					service.getTaskCompletionStats(developerId, start, end));
		} catch (Exception e) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getTaskPerformanceByUserCode")
	public ResponseEntity<Object> getTaskPerformanceByUserCode(@RequestParam String userCode) {
		try {
			return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK,
					service.getTaskPerformanceByUserCode(userCode));
		} catch (Exception e) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getTaskSubmit-status-percntage")
	public ResponseEntity<Object> getTaskSubmitPercntage(@RequestParam Long developerId) {
		Map<String, Long> map = this.service.getTaskSubmitType(developerId);
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, map);
	}

	@GetMapping("/getTaskDateWise")
	public ResponseEntity<Object> getTaskDateWise(@RequestParam String userCode, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		List<TaskDto> list = service.getAllTaskByDateWise(userCode, startDate, endDate);
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, list);
	}

	@PostMapping("/sent-notification")
	public ResponseEntity<Object> sentNotification(@RequestParam Long developerId) {
		return responseWithObject.generateResponse(AppConstants.ACCEPT, HttpStatus.OK,
				service.sentNotification(developerId));
	}
}
