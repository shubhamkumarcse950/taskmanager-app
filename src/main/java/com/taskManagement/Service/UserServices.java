package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.UserDto;
import com.taskManagement.Entitys.User;
import com.taskManagement.exceptions.UserNotFoundException;

@Service
public interface UserServices {

	UserDto getUserDetailsByEmail(String email);

	UserDto saveUserDetails(UserDto dto);

	String resetYourPassword(String userCode, String password);

	String userAccountDecative(Long userId, String email, String userName);

	List<User> getAllUsers(String userName);

	String updateUserDetails(String usercode, String name, String email, String contectNo);

	List<User> getAllUsersList();

	User getCruentUser(String userCode) throws UserNotFoundException;

	User findUserByProfile(String jwt) throws UserNotFoundException;

	List<User> searchByUserName(String name);

	void deleteUser(String email);

}
