package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
		try {
			if (userRepo.findByEmail(dto.getEmail()) != null) {
				throw new InvalidInputException("This email is already exist,please choose a deffrent email!");
			}
			Developer developer = this.mapper.toDeveloper(dto);
			developer.setUserCode(UUID.randomUUID().toString());
			developer = this.developerRepository.save(developer);

			User user = new User();
			user.setContact(dto.getMobileNumber());
			user.setEmail(dto.getEmail());
			user.setName(dto.getFullName());
			user.setPassword(encoder.encode("123456"));
			user.setUserCode(developer.getUserCode());
			List<String> role = new ArrayList<>();
			role.add("Employee");
			user.setRoles(role);
			this.userRepo.save(user);
			return this.mapper.toDeveloperDto(developer);
		} catch (InvalidInputException | HibernateException e) {
			log.error("Internal service error!!", e.getMessage());
			return new DeveloperDto();
		}

	}

	@Override
	@Cacheable(value = "Developer")
	public List<DeveloperDto> getAllDeveloper() {
		List<DeveloperDto> list = new ArrayList<>();
		try {
			log.warn("Product found succesfully!");
			List<Developer> developers = this.developerRepository.findAll();
			list = mapper.toListDeveloperDto(developers);
		} catch (DataNotFoundException e) {
			log.warn("Developer data not found in data base!!", e.getMessage());

		}
		return list;
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
			User user2 = user.get();
			user2.setEmail(developer.getEmail());
			user2.setContact(developer.getMobileNumber());
			user2.setName(developer.getFullName());
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

}
