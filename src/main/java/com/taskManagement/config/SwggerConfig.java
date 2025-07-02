package com.taskManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwggerConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes("Bearer Authentication", createAPTScheme()))
				.info(new Info().title("Master Schema API").description("This is for global use"));
//				.addServersItem(new Server().url("https://app.ventureconsultancyservices.com/taskManagement"));
	}

	public SecurityScheme createAPTScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT");
	}
//	@Override
//	public DeveloperDto updataDeveloperInfo(DeveloperDto dto) {
//		try {
//			Developer existingDeveloper = this.developerRepository.findById(dto.getDeveloperId())
//					.orElseThrow(() -> new InvalidInputException("Data not found with this developer id"));
//
//			User user = this.userRepo.findByUserCode(existingDeveloper.getUserCode())
//					.orElseThrow(() -> new InvalidInputException("User code is invalid, not updating!"));
//
//			user.setEmail(dto.getEmail());
//			user.setContact(dto.getMobileNumber());
//			user.setName(dto.getFullName());
//			this.userRepo.save(user);
//
//			existingDeveloper.setFullName(dto.getFullName());
//			existingDeveloper.setEmail(dto.getEmail());
//			existingDeveloper.setMobileNumber(dto.getMobileNumber());
//
//			Developer updatedDev = this.developerRepository.save(existingDeveloper);
//			return mapper.toDeveloperDto(updatedDev);
//
//		} catch (InvalidInputException e) {
//			log.error(e.getMessage());
//			throw e;
//		}
//	}
//

}
