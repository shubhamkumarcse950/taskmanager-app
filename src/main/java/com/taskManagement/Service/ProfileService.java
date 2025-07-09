package com.taskManagement.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.ProfileDto;

@Service
public interface ProfileService {

	String createProfile(ProfileDto profileDto, MultipartFile file);

	List<ProfileDto> getUserProfile();

	String updateProfile(ProfileDto profileDto, MultipartFile file) throws IOException;

}
