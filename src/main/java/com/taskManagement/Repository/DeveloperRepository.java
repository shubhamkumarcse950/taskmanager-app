package com.taskManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

	Optional<Developer> findByUserCode(String userCode);

	List<Developer> findByRoleIn(List<String> rolesToFetch);

}
