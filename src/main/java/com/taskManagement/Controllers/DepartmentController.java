package com.taskManagement.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.DepartmentDTO;
import com.taskManagement.Service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@PostMapping
	public DepartmentDTO create(@RequestBody DepartmentDTO dto) {
		return departmentService.createDepartment(dto);
	}

	@PutMapping("/{id}")
	public DepartmentDTO update(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
		return departmentService.updateDepartment(id, dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
	}

	@GetMapping("/{id}")
	public DepartmentDTO getById(@PathVariable Long id) {
		return departmentService.getDepartmentById(id);
	}

	@GetMapping
	public List<DepartmentDTO> getAll() {
		return departmentService.getAllDepartments();
	}
}
