package com.taskManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.SalesDepartmentEmployee;

@Repository
public interface SalesDepartmentEmployeeRepo extends JpaRepository<SalesDepartmentEmployee, Long> {

	Optional<SalesDepartmentEmployee> findByUserCode(String userCode);

	List<SalesDepartmentEmployee> findAllByDepartment(Department department);
}
