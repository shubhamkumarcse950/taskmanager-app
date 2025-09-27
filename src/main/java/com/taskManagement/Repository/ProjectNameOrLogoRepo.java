package com.taskManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.ProjectNameOrLogo;

@Repository
public interface ProjectNameOrLogoRepo extends JpaRepository<ProjectNameOrLogo, Integer> {

}
