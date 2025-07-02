package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByDeveloperId(Long developerId);

}
