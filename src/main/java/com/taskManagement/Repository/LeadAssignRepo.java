package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.LeadAssign;

@Repository
public interface LeadAssignRepo extends JpaRepository<LeadAssign, Long> {

	List<LeadAssign> findBySalesDepartmentEmployee_EmpId(Long empId);

}
