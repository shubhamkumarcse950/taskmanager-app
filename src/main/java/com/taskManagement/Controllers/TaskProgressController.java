package com.taskManagement.Controllers;

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

import com.taskManagement.Dtos.TaskProgressDto;
import com.taskManagement.Service.TaskProgressService;
import com.taskManagement.outputdto.OutputTask;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "task-progress-controller")
@RequestMapping("/taskProgress")
public class TaskProgressController {

	private final ResponseWithObject responseWithObject;
	private final TaskProgressService taskProgressService;

	public TaskProgressController(ResponseWithObject responseWithObject, TaskProgressService taskProgressService) {
		super();
		this.responseWithObject = responseWithObject;
		this.taskProgressService = taskProgressService;
	}

	@PostMapping("/create")
	public ResponseEntity<Object> postTaskProgress(@RequestBody TaskProgressDto dto) {
		dto = this.taskProgressService.saveTaskProgress(dto);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.CREATED, dto);
		}

		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, dto);
	}

	@GetMapping("/getAllTaskProgressByTaskId")
	public ResponseEntity<Object> getAllProgressWtihTask(@RequestParam Long taskId) {
		OutputTask task = this.taskProgressService.getAllProgressTaskWithTask(taskId);
		if (Objects.isNull(task)) {
			return responseWithObject.generateResponse(AppConstants.NO_DATA_FOUND, HttpStatus.NO_CONTENT,
					"no task found with this Id");
		}
		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, task);
	}

	@PutMapping("/update")
	public ResponseEntity<Object> updateTaskProgress(@RequestBody TaskProgressDto dto) {
		dto = this.taskProgressService.updateTaskProgess(dto);
		if (Objects.isNull(dto)) {
			return responseWithObject.generateResponse(AppConstants.ERROR, HttpStatus.CREATED, dto);
		}

		return responseWithObject.generateResponse(AppConstants.SUCCESS, HttpStatus.OK, dto);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> deleteTaskProgress(@RequestParam Long taskProgressId) {
		boolean response = this.taskProgressService.deleteTaskProgres(taskProgressId);
		if (response) {
			return responseWithObject.generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK, response);
		}
		return responseWithObject.generateResponse("Invalid input Id", HttpStatus.BAD_REQUEST, response);
	}

}
