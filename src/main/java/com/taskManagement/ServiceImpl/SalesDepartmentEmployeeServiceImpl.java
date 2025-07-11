package com.taskManagement.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.SalesDepartmentEmployeeRepo;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.SalesDepartmentEmployeeService;
import com.taskManagement.exceptions.InvalidInputException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalesDepartmentEmployeeServiceImpl implements SalesDepartmentEmployeeService {

	private final SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo;
	private final LeadManagmentMapper mapper;
	private final UserRepo userRepo;
	private final PasswordEncoder encoder;

	public SalesDepartmentEmployeeServiceImpl(SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo,
			LeadManagmentMapper mapper, UserRepo userRepo, PasswordEncoder encoder) {
		super();
		this.salesDepartmentEmployeeRepo = salesDepartmentEmployeeRepo;
		this.mapper = mapper;
		this.userRepo = userRepo;
		this.encoder = encoder;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public SalesDepartmentEmployeeDto addSaleseEmployee(SalesDepartmentEmployeeDto dto) {
		final String password = "123456";
		if (userRepo.findByEmail(dto.getEmail()) != null) {
			throw new InvalidInputException("This email is already exist, please choose a different email!");
		}

		try {
			String usercode = UUID.randomUUID().toString();
			SalesDepartmentEmployee salesDepartmentEmployee = this.mapper.toSalesDepartmentEmployee(dto);
			salesDepartmentEmployee.setUserCode(usercode);

			User user = new User();
			user.setName(dto.getFullName());
			user.setEmail(dto.getEmail());
			user.setContact(dto.getMobileNumber());
			user.setActive(true);
			user.setPassword(encoder.encode(password));
			user.setUserCode(usercode);
			ArrayList<String> roles = new ArrayList<>();
			roles.add(dto.getRole().toUpperCase());
			user.setRoles(roles);
			userRepo.save(user);
			salesDepartmentEmployee.setRole(dto.getRole().toUpperCase());
			salesDepartmentEmployee = salesDepartmentEmployeeRepo.saveAndFlush(salesDepartmentEmployee);
			return mapper.toSalesDepartmentEmployeeDto(salesDepartmentEmployee);

		} catch (Exception e) {
			log.error("Error occurred in saving Sales employee data,{}", e.getMessage());
			throw new RuntimeException("Runtime error while saving Sales employee data. Please try again later.", e);
		}

	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deletedSalesEmployee(Long employeeId) {
		try {
			SalesDepartmentEmployee employee = salesDepartmentEmployeeRepo.findById(employeeId)
					.orElseThrow(() -> new InvalidInputException("employee not found with this empId"));
			userRepo.deleteByUserCode(employee.getUserCode());
			salesDepartmentEmployeeRepo.delete(employee);
			return true;
		} catch (Exception e) {
			log.error("Error occurred in deleting the sales employee data,{}", e.getMessage());
			return false;
		}
	}

	@Override
	public List<SalesDepartmentEmployeeDto> getAllSalesDepartmentEmployee() {
		return salesDepartmentEmployeeRepo.findAll().stream().map(e -> mapper.toSalesDepartmentEmployeeDto(e)).toList();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public SalesDepartmentEmployeeDto updatEmployeeDto(SalesDepartmentEmployeeDto dto) {
		if (dto == null || dto.getEmpId() == null) {
			throw new InvalidInputException("Employee ID and data cannot be null");
		}

		try {
			SalesDepartmentEmployee existingEmployee = salesDepartmentEmployeeRepo.findById(dto.getEmpId())
					.orElseThrow(() -> new InvalidInputException("Invalid empId for update"));

			User user = userRepo.findByUserCode(existingEmployee.getUserCode())
					.orElseThrow(() -> new InvalidInputException("Invalid user code"));

			if (!user.getEmail().equals(dto.getEmail())) {
				if (userRepo.findByEmail(dto.getEmail()) != null) {
					throw new InvalidInputException("This email already exists. Please choose a different email!");
				}
			}

			existingEmployee.setFullName(dto.getFullName());
			existingEmployee.setEmail(dto.getEmail());
			existingEmployee.setMobileNumber(dto.getMobileNumber());
			existingEmployee.setAddress(dto.getAddress());
			existingEmployee.setDesignation(dto.getDesignation());
			existingEmployee.setRole(dto.getRole().toUpperCase());

			user.setName(dto.getFullName());
			user.setEmail(dto.getEmail());
			user.setContact(dto.getMobileNumber());
			user.setUpdatedAt(LocalDateTime.now());
			user.setRoles(List.of(dto.getRole().toUpperCase()));

			userRepo.save(user);
			SalesDepartmentEmployee updatedEmployee = salesDepartmentEmployeeRepo.saveAndFlush(existingEmployee);

			return mapper.toSalesDepartmentEmployeeDto(updatedEmployee);

		} catch (InvalidInputException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("Error while updating Sales employee data", e);
			throw new RuntimeException(
					"Unexpected error occurred while updating Sales employee data. Please try again later.", e);
		}
	}

	@Override
	public SalesDepartmentEmployeeDto getEmployeeByUserCode(String userCode) {

		SalesDepartmentEmployee optional = this.salesDepartmentEmployeeRepo.findByUserCode(userCode)
				.orElseThrow(() -> new InvalidInputException("Invalid input user code for getting employee data"));
		return mapper.toSalesDepartmentEmployeeDto(optional);

	}
}
