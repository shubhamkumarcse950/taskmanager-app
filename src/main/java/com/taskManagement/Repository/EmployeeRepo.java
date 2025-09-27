package com.taskManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

	Optional<Employee> findByUserCode(String userCode);

	List<Employee> findByDepartment(Department department);

}
