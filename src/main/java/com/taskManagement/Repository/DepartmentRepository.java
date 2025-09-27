package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.SalesDepartmentEmployee;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByName(String name);

	List<Developer> findAllDevelopersByDepId(Long depId);

	List<SalesDepartmentEmployee> findAllSEmployeesByDepId(Long depId);

}
