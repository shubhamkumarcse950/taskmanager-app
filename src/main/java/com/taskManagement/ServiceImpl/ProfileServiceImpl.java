package com.taskManagement.ServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.ProfileDto;
import com.taskManagement.Entitys.Profile;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.ProfileRepo;
import com.taskManagement.Service.ProfileService;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepo profileRepo;
	private final LeadManagmentMapper mapper;

	public ProfileServiceImpl(ProfileRepo profileRepo, LeadManagmentMapper mapper) {
		super();
		this.profileRepo = profileRepo;
		this.mapper = mapper;
	}

	@Override
	public String createProfile(ProfileDto profileDto, MultipartFile file) {
		try {
			if (!profileRepo.findAll().isEmpty()) {
				throw new InvalidInputException("Company profile already exists. You can't create a second profile.");
			}

			Profile profile = mapper.toProfile(profileDto);
			profile.setCreatedAt(LocalDateTime.now());
			profile.setUpdateAt(null);

			if (file != null && !file.isEmpty()) {
				profile.setLogo(file.getBytes());
			}

			profileRepo.saveAndFlush(profile);
			return AppConstants.SUCCESS;

		} catch (InvalidInputException | HibernateException e) {
			log.error("Error: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred while saving profile data: {}", e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while saving profile data");
		}
	}

	@Override
	public List<ProfileDto> getUserProfile() {
		return profileRepo.findAll().stream().map(p -> {
			ProfileDto dto = mapper.toProfileDto(p);
			String image = Base64.getEncoder().encodeToString(p.getLogo());
			dto.setGetLogo(image);
			return dto;
		}).toList();
	}

	@Override
	public String updateProfile(ProfileDto profileDto, MultipartFile file) throws IOException {
		try {

			Optional<Profile> optional = this.profileRepo.findById(profileDto.getProfileId());
			if (optional.isEmpty()) {
				throw new InvalidInputException("You did provide Invalid input profileId");
			}
			Profile profile = this.mapper.toProfile(profileDto);
			profile.setUpdateAt(LocalDateTime.now());
			profile.setLogo(file != null ? file.getBytes() : optional.get().getLogo());
			this.profileRepo.saveAndFlush(profile);
			return AppConstants.SUCCESS;
		} catch (HibernateException | InvalidInputException e) {
			log.error("Error|Ouccrred in updating profile,{}", e.getMessage());
			return AppConstants.ERROR;
		}
	}
}
