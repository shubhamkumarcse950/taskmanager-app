package com.taskManagement.Service;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.UserDto;

@Service
public interface UserServices {

	UserDto getUserDetailsByEmail(String email);

	UserDto saveUserDetails(UserDto dto);

	String resetYourPassword(String userCode, String password);

	String userAccountDecative(Long userId, String email, String userName);

}
