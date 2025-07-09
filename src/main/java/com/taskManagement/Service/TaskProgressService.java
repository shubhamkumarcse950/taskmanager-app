package com.taskManagement.Service;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.TaskProgressDto;
import com.taskManagement.outputdto.OutputTask;

@Service
public interface TaskProgressService {

	TaskProgressDto saveTaskProgress(TaskProgressDto dto);

	OutputTask getAllProgressTaskWithTask(Long taskId);

	TaskProgressDto updateTaskProgess(TaskProgressDto taskProgressDto);

	boolean deleteTaskProgres(Long taskProgresId);

}
