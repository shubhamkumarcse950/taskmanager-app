package com.taskManagement.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

@Service
public interface ProjectNameOrLogoService {

	String projectNameAndLogo(String projectName, MultipartFile file) throws IOException, java.io.IOException;

	String getLogo();

	String getName();

}
