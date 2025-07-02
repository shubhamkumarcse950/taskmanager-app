package com.taskManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
