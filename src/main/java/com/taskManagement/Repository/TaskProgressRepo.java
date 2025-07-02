package com.taskManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.TaskProgress;

@Repository
public interface TaskProgressRepo extends JpaRepository<TaskProgress, Long> {

//	@Query("SELECT new com.taskManagement.Dtos.TaskResponse(t,tp) FROM TaskProgress tp"
//			+ "LEFT JOIN Task t ON tp.taskId=t.taskId")
//	List<TaskResponse> getAllTaskWithProgressList();

}
