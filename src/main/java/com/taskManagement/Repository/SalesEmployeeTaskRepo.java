package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.SalesEmployeeTask;

@Repository
public interface SalesEmployeeTaskRepo extends JpaRepository<SalesEmployeeTask, Long> {

	List<SalesEmployeeTask> findBySalesDepartmentEmployee_EmpId(Long empId);

}
