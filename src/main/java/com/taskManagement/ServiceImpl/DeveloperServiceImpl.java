package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.RoleEnum;
import com.taskManagement.Dtos.DeveloperDto;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.DeveloperService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeveloperServiceImpl implements DeveloperService {

	private final DeveloperRepository developerRepository;
	private final LeadManagmentMapper mapper;
	private final UserRepo userRepo;
	private final PasswordEncoder encoder;

	public DeveloperServiceImpl(DeveloperRepository developerRepository, LeadManagmentMapper mapper, UserRepo userRepo,
			PasswordEncoder encoder) {
		this.developerRepository = developerRepository;
		this.mapper = mapper;
		this.userRepo = userRepo;
		this.encoder = encoder;
	}

	@Override
	@Transactional
	public DeveloperDto saveDeveloperInfo(DeveloperDto dto) {
		if (userRepo.findByEmail(dto.getEmail()) != null) {
			throw new InvalidInputException("This email is already exist, please choose a different email!");
		}

		try {
			Developer developer = mapper.toDeveloper(dto);
			developer.setUserCode(UUID.randomUUID().toString());
			developer = developerRepository.save(developer);
			List<String> role = new ArrayList<>();
			role.add(dto.getRole().toUpperCase());
			User user = new User();
			user.setName(dto.getFullName());
			user.setEmail(dto.getEmail());
			user.setContact(dto.getMobileNumber());
			user.setActive(true);
			user.setPassword(encoder.encode("123456"));
			user.setUserCode(developer.getUserCode());
			user.setRoles(role);

			userRepo.save(user);

			return mapper.toDeveloperDto(developer);

		} catch (InvalidInputException e) {
			throw e;
		} catch (Exception e) {
			log.error("Internal service error!!", e);
			throw new RuntimeException("Something went wrong while saving developer info");
		}
	}

	@Override
	public List<DeveloperDto> getAllDeveloper() {

		try {

			List<Developer> developers = this.developerRepository.findAll();
			return mapper.toListDeveloperDto(developers);

		} catch (DataNotFoundException e) {
			log.warn("Developer data not found in data base!!", e.getMessage());
			return Collections.emptyList();
		}

	}

	@Override
	public DeveloperDto getOneDeveloperById(Long developerId) {
		Developer developer = this.developerRepository.findById(developerId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID));
		return mapper.toDeveloperDto(developer);
	}

	@Override
	public DeveloperDto updataDeveloperInfo(DeveloperDto dto) {
		Developer developer = this.mapper.toDeveloper(dto);
		try {
			Optional<Developer> optional = this.developerRepository.findById(dto.getDeveloperId());
			if (optional.isEmpty()) {
				throw new InvalidInputException("Data not found with this developer id");
			}
			developer.setDeveloperId(optional.get().getDeveloperId());
			developer.setUserCode(optional.get().getUserCode());

			Optional<User> user = this.userRepo.findByUserCode(optional.get().getUserCode());
			if (user.isEmpty()) {
				throw new InvalidInputException("User code is invalid,not updating!");
			}
			List<String> role = new ArrayList<>();
			role.add(dto.getRole().toUpperCase());
			User user2 = user.get();
			user2.setEmail(developer.getEmail());
			user2.setContact(developer.getMobileNumber());
			user2.setName(developer.getFullName());
			user2.setRoles(role);
			this.userRepo.save(user2);
			developer = this.developerRepository.save(developer);

			return mapper.toDeveloperDto(developer);
		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public String deleteDeveloperInfo(Long developerId) {
		String message = AppConstants.DELETED_SUCCESFULLY;
		Developer developer = this.developerRepository.findById(developerId).orElseThrow(
				() -> new InvalidInputException("Data not deleted with this ,it is not found in data base!!"));
		User user = this.userRepo.findByUserCode(developer.getUserCode()).orElseThrow(
				() -> new InvalidInputException("Data not deleted with this ,it is not found in data base!!"));
		this.userRepo.deleteById(user.getUserId());
		this.developerRepository.deleteById(developer.getDeveloperId());
		return message;
	}

	@Override
	public List<DeveloperDto> getDeveloperByIds(List<Long> developerId) {
		List<DeveloperDto> list = new ArrayList<>();
		try {
			if (developerId.isEmpty()) {
				throw new InvalidInputException("You provide null ids!!");
			}
			for (Long long1 : developerId) {
				Optional<Developer> optional = this.developerRepository.findById(long1);
				if (optional.isEmpty()) {
					log.warn("developer not found with {} this id", long1);
					continue;
				}
				DeveloperDto dto = this.mapper.toDeveloperDto(optional.get());
				list.add(dto);
			}

		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		}
		return list;
	}

	@Override
	public List<DeveloperDto> getAllEmployee(String role, String userCode) {
		RoleEnum roleEnum;
		try {
			Optional<User> user = userRepo.findByUserCode(userCode);
			if (user.isEmpty()) {
				throw new InvalidInputException("User not found with this user code");
			}
			roleEnum = RoleEnum.valueOf(role.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Invalid role: " + role);
		}

		List<String> rolesToFetch;
		switch (roleEnum) {
		case ADMIN:
			rolesToFetch = List.of("HR", "MANAGER", "TL", "EMPLOYEE");
			break;
		case HR:
			rolesToFetch = List.of("MANAGER", "TL", "EMPLOYEE");
			break;
		case MANAGER:
			rolesToFetch = List.of("TL", "EMPLOYEE");
			break;
		case TL:
			rolesToFetch = List.of("EMPLOYEE");
			break;
		default:
			throw new IllegalArgumentException("Unexpected role: " + roleEnum);
		}

		List<Developer> developers = developerRepository.findByRoleIn(rolesToFetch);
		return developers.stream().map(mapper::toDeveloperDto).collect(Collectors.toList());
	}

	@Override
	public Long getDeveloperByUserCode(String userCode) {

		try {
			Developer developer = this.developerRepository.findByUserCode(userCode)
					.orElseThrow(() -> new InvalidInputException("Invalid user Code"));
			return developer.getDeveloperId();

		} catch (Exception e) {
			log.error("Error|Ouccred in getting user Code {}", e.getMessage());
			return (long) 0;
		}
	}

}
