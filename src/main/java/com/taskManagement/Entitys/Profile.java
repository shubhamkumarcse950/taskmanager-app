package com.taskManagement.Entitys;

import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;

	private String companyName;
	private String ownerName;
	private String gstNumber;

	private String email;
	private String mobileNumber;
	private String alternateMobile;
	@Column(length = 1000)
	private String addressLine1;
	private String city;
	private String state;
	private String country;
	private String pinCode;

	private String website;
	private String businessType;

	private byte[] logo;
	@CurrentTimestamp
	private LocalDateTime createdAt;

	private LocalDateTime updateAt;
}
