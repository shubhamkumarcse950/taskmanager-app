package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.DepartmentDTO;

@Service
public interface DepartmentService {
	DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

	DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);

	void deleteDepartment(Long id);

	DepartmentDTO getDepartmentById(Long id);

	List<DepartmentDTO> getAllDepartments();
}
