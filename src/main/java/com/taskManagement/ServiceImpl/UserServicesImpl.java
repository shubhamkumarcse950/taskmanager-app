package com.taskManagement.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.UserDto;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.UserServices;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServicesImpl implements UserServices {

	private final UserRepo repo;
	private final LeadManagmentMapper mapper;
	private final PasswordEncoder encoder;
	@Value("${admin.secret}")
	private String adminName;

	public UserServicesImpl(UserRepo repo, LeadManagmentMapper mapper, PasswordEncoder encoder) {
		super();
		this.repo = repo;
		this.mapper = mapper;
		this.encoder = encoder;
	}

	@Override
	public UserDto saveUserDetails(UserDto dto) {
		if (repo.findByEmail(dto.getEmail()) != null) {
			throw new InvalidInputException("This email is already exist,please choose a deffrent email!");
		}
		try {
			User user = this.mapper.toUser(dto);
			user.setPassword(encoder.encode(dto.getPassword()));
			user.setUserCode(UUID.randomUUID().toString());
			user = repo.save(user);
			return mapper.toUserDto(user);
		} catch (Exception e) {
			log.error("Internal service error{}", e.getMessage());
			return new UserDto();
		}

	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserDto dto = new UserDto();
		try {
			User optional = this.repo.findByEmail(email);
			if (optional == null) {
				throw new InvalidInputException(AppConstants.INVALID_INPUT_ID + email);
			}
			dto.setContact(optional.getContact());
			dto.setEmail(optional.getEmail());
			dto.setName(optional.getName());
			dto.setPassword(null);
			dto.setRoles(optional.getRoles());
			dto.setUserId(null);
		} catch (Exception e) {
			log.error("Internal service data fetching in user service", e.getMessage());
			throw e;
		}
		return dto;
	}

	@Override
	public String resetYourPassword(String userCode, String password) {
		String response = AppConstants.ERROR;
		try {
			Optional<User> userOptional = this.repo.findByUserCode(userCode);
			if (userOptional.isEmpty()) {
				throw new DataNotFoundException("User Not found with this userCode !");
			}
			User user = userOptional.get();
			user.setPassword(encoder.encode(password));
			this.repo.save(user);
			return AppConstants.SUCCESS;
		} catch (DataNotFoundException | HibernateException e) {
			log.error(AppConstants.NO_DATA_FOUND, e.getMessage());
			return response;
		}
	}

	@Override
	public String userAccountDecative(Long userId, String email, String userName) {

		if (!adminName.equals(userName)) {
			return AppConstants.INVALID_USERNAME;
		}

		if (userId == null && (email == null || email.isBlank())) {
			throw new InvalidInputException("Either userId or email must be provided");
		}

		try {
			User user = repo.findByEmailOrUserId(email, userId).orElseThrow(
					() -> new DataNotFoundException("User account not found with the provided userId or email"));

			if (!user.isActive()) {
				user.setActive(true);
				user.setUpdatedAt(LocalDateTime.now());
				repo.save(user);
				return AppConstants.USER_ACCOUNT_ACTIVE;
			}

			user.setActive(false);
			user.setUpdatedAt(LocalDateTime.now());
			repo.save(user);

			return AppConstants.USER_ACCOUNT_DEACTIVE;

		} catch (DataNotFoundException | InvalidInputException e) {
			log.warn("Handled exception: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred while deactivating account: {}", e.getMessage(), e);
			return AppConstants.INTERNAL_SERVER_ERROR;
		}
	}

}
