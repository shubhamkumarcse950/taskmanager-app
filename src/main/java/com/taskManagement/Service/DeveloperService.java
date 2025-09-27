package com.taskManagement.Service;

import java.util.List;

import com.taskManagement.Dtos.DeveloperDto;
import com.taskManagement.Entitys.Developer;

public interface DeveloperService {

	DeveloperDto saveDeveloperInfo(DeveloperDto dto);

	List<DeveloperDto> getAllDeveloper();

	DeveloperDto getOneDeveloperById(Long developerId);

	DeveloperDto updataDeveloperInfo(DeveloperDto dto);

	String deleteDeveloperInfo(Long developerId);

	List<DeveloperDto> getDeveloperByIds(List<Long> developerId);

	List<DeveloperDto> getAllEmployee(String role, String userCode);

	Long getDeveloperByUserCode(String userCode);

	void updataDeveloperByEmployeeApi(Developer developer);

	void saveDeveloperByEmployeeApi(Developer developer, String password);

}
