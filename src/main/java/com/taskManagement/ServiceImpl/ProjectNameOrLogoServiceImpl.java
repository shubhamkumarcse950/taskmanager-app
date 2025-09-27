package com.taskManagement.ServiceImpl;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Entitys.ProjectNameOrLogo;
import com.taskManagement.Repository.ProjectNameOrLogoRepo;
import com.taskManagement.Service.ProjectNameOrLogoService;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectNameOrLogoServiceImpl implements ProjectNameOrLogoService {

	private final ProjectNameOrLogoRepo repo;

	public ProjectNameOrLogoServiceImpl(ProjectNameOrLogoRepo repo) {
		super();
		this.repo = repo;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public String projectNameAndLogo(String projectName, MultipartFile file) throws java.io.IOException {
		List<ProjectNameOrLogo> list = repo.findAll();
		if (list.isEmpty()) {
			ProjectNameOrLogo data = new ProjectNameOrLogo();
			data.setId(1);
			data.setLogo(file != null ? file.getBytes() : null);
			data.setName(projectName);
			repo.save(data);
			log.info("Add new project Name and logo");
			return AppConstants.SUCCESS;
		} else {

			ProjectNameOrLogo data = new ProjectNameOrLogo();
			data.setId(list.get(0).getId());
			data.setName(projectName != null ? projectName : list.get(0).getName());
			data.setLogo(file != null ? file.getBytes() : list.get(0).getLogo());
			log.warn("Update old project Name and Logo");
			repo.save(data);
			return AppConstants.SUCCESS;

		}
	}

	@Override
	public String getLogo() {
		String logo = null;
		List<ProjectNameOrLogo> list = repo.findAll();
		for (ProjectNameOrLogo projectNameOrLogo : list) {
			if (projectNameOrLogo.getLogo() == null) {
				return null;
			}
			logo = Base64.getEncoder().encodeToString(projectNameOrLogo.getLogo());
		}
		return logo;
	}

	@Override
	public String getName() {
		String name = null;
		List<ProjectNameOrLogo> list = repo.findAll();
		for (ProjectNameOrLogo projectNameOrLogo : list) {
			name = projectNameOrLogo.getName();
		}
		return name;
	}
}
