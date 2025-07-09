package com.taskManagement.Dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

	private Long profileId;

	private String companyName;
	private String ownerName;
	private String gstNumber;

	private String email;
	private String mobileNumber;
	private String alternateMobile;

	private String addressLine1;
	private String city;
	private String state;
	private String country;
	private String pinCode;

	private String website;
	private String businessType;

	private MultipartFile logo;
	private String getLogo;
}
