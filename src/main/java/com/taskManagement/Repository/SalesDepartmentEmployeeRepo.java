package com.taskManagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.SalesDepartmentEmployee;

@Repository
public interface SalesDepartmentEmployeeRepo extends JpaRepository<SalesDepartmentEmployee, Long> {

	Optional<SalesDepartmentEmployee> findByUserCode(String userCode);

}
