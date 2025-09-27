package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.EmployeeDto;
import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.Employee;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DepartmentRepository;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.EmployeeRepo;
import com.taskManagement.Repository.SalesDepartmentEmployeeRepo;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.DeveloperService;
import com.taskManagement.Service.EmployeeService;
import com.taskManagement.Service.SalesDepartmentEmployeeService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.outputdto.EmployeeResponse;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepo employeeRepository;
	private final LeadManagmentMapper mapper;
	private final UserRepo userRepo;
	private final PasswordEncoder encoder;
	private final DepartmentRepository departmentRepository;
	private final DeveloperService developerService;
	private final SalesDepartmentEmployeeService salesEmployeeService;
	private final DeveloperRepository developerRepository;
	private final SalesDepartmentEmployeeRepo salesEmployeeRepo;

	public EmployeeServiceImpl(EmployeeRepo employeeRepository, LeadManagmentMapper mapper, UserRepo userRepo,
			PasswordEncoder encoder, DepartmentRepository departmentRepository,
			SalesDepartmentEmployeeService salesEmployeeService, DeveloperService developerService,
			DeveloperRepository developerRepository, SalesDepartmentEmployeeRepo salesEmployeeRepo) {
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;
		this.userRepo = userRepo;
		this.encoder = encoder;
		this.departmentRepository = departmentRepository;
		this.developerService = developerService;
		this.salesEmployeeService = salesEmployeeService;
		this.developerRepository = developerRepository;
		this.salesEmployeeRepo = salesEmployeeRepo;
	}

	@Override
	@Transactional
	public EmployeeDto saveEmployeeInfo(EmployeeDto dto) {
		if (userRepo.findByEmail(dto.getEmail()) != null) {
			throw new InvalidInputException("This email is already exist, please choose a different email!");
		}

		try {
			Department department = departmentRepository.findById(dto.getDepartmentId())
					.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID + "for department"));
			String userCode = UUID.randomUUID().toString();
			if ("Information Technology".equalsIgnoreCase(department.getName())) {
				Developer developer = new Developer();
				developer.setCompany(null);
				developer.setCompnyEmail(null);
				developer.setDepartment(department);
				developer.setEmail(dto.getEmail());
				developer.setFullName(dto.getFullName());
				developer.setMobileNumber(dto.getMobileNumber());
				developer.setRole(dto.getRole());
				developer.setTechStack(dto.getDesignation());
				developer.setUserCode(userCode);
				developer.setAddress(dto.getAddress());
				developerService.saveDeveloperByEmployeeApi(developer, dto.getPassword());
				return dto;
			} else if ("Sales Department".equalsIgnoreCase(department.getName())) {
				SalesDepartmentEmployee salesEmployee = new SalesDepartmentEmployee();
				salesEmployee.setFullName(dto.getFullName());
				salesEmployee.setEmail(dto.getEmail());
				salesEmployee.setMobileNumber(dto.getMobileNumber());
				salesEmployee.setRole(dto.getRole());
				salesEmployee.setAddress(dto.getAddress());
				salesEmployee.setDesignation(dto.getDesignation());
				salesEmployee.setUserCode(userCode);
				salesEmployee.setDepartment(department);
				salesEmployee.setAddress(dto.getAddress());
				salesEmployeeService.addSalesEmployee(salesEmployee, dto.getPassword());
				return dto;
			}

			Employee employee = mapper.toEmployee(dto);
			employee.setDepartment(department);
			employee.setUserCode(userCode);
			employee = employeeRepository.save(employee);
			List<String> role = new ArrayList<>();
			role.add(dto.getRole().toUpperCase());
			User user = new User();
			user.setName(dto.getFullName());
			user.setEmail(dto.getEmail());
			user.setContact(dto.getMobileNumber());
			user.setActive(true);
			user.setPassword(encoder
					.encode(dto.getPassword() != null && !dto.getPassword().isEmpty() ? dto.getPassword() : "123456"));
			user.setUserCode(employee.getUserCode());
			user.setRoles(role);

			userRepo.save(user);

			return mapper.toEmployeeDto(employee);

		} catch (InvalidInputException e) {
			throw e;
		} catch (Exception e) {
			log.error("Internal service error!!", e);
			throw new RuntimeException("Something went wrong while saving employee info");
		}
	}

	@Override
	@Transactional
	public EmployeeDto updateEmployeeInfo(EmployeeDto dto) {
		try {
			Department department = departmentRepository.findById(dto.getDepartmentId())
					.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID + " for department"));

			String deptName = department.getName();

			if ("Information Technology".equalsIgnoreCase(deptName)) {
				Developer developer = developerRepository.findById(dto.getEmployeeId())
						.orElseThrow(() -> new InvalidInputException("Developer not found for update"));
				developer.setAddress(dto.getAddress());
				developer.setFullName(dto.getFullName());
				developer.setEmail(dto.getEmail());
				developer.setMobileNumber(dto.getMobileNumber());
				developer.setRole(dto.getRole().toUpperCase());
				developer.setTechStack(dto.getDesignation());
				developer.setDepartment(department);

				developerService.updataDeveloperByEmployeeApi(developer);
				return dto;
			}

			else if ("Sales Department".equalsIgnoreCase(deptName)) {
				SalesDepartmentEmployee salesEmployee = salesEmployeeRepo.findById(dto.getEmployeeId())
						.orElseThrow(() -> new InvalidInputException("Sales employee not found for update"));

				salesEmployee.setFullName(dto.getFullName());
				salesEmployee.setEmail(dto.getEmail());
				salesEmployee.setMobileNumber(dto.getMobileNumber());
				salesEmployee.setRole(dto.getRole().toUpperCase());
				salesEmployee.setAddress(dto.getAddress());
				salesEmployee.setDesignation(dto.getDesignation());
				salesEmployee.setDepartment(department);
				salesEmployee.setAddress(dto.getAddress());
				salesEmployeeService.updateSalesEmployeeRegistration(salesEmployee);
				return dto;
			}
			Employee existingEmployee = employeeRepository.findById(dto.getEmployeeId())
					.orElseThrow(() -> new InvalidInputException("Data not found with this employee id"));

			Employee employee = mapper.toEmployee(dto);
			employee.setEmployeeId(existingEmployee.getEmployeeId());
			employee.setUserCode(existingEmployee.getUserCode());
			employee.setDepartment(department);
			User user = userRepo.findByUserCode(dto.getUserCode())
					.orElseThrow(() -> new InvalidInputException("User code is invalid, not updating!"));

			List<String> role = new ArrayList<>();
			role.add(dto.getRole().toUpperCase());
			user.setEmail(employee.getEmail());
			user.setContact(employee.getMobileNumber());
			user.setName(employee.getFullName());
			user.setRoles(role);
			userRepo.save(user);
			employee = employeeRepository.save(employee);

			return mapper.toEmployeeDto(employee);

		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error while updating employee info", e);
			throw new RuntimeException("Something went wrong while updating employee info", e);
		}
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		try {
			List<Employee> employees = employeeRepository.findAll();
			return employees.stream().map(e -> mapper.toEmployeeDto(e)).toList();
		} catch (Exception e) {
			log.warn("Employee data not found in database!!", e.getMessage());
			return new ArrayList<>();
		}
	}

	@Override
	public EmployeeDto getEmployeeById(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID));
		return mapper.toEmployeeDto(employee);
	}

	@Override
	public EmployeeDto getEmployeeByUserCode(String userCode) {
		Employee employee = employeeRepository.findByUserCode(userCode)
				.orElseThrow(() -> new InvalidInputException("Invalid user code"));
		return mapper.toEmployeeDto(employee);
	}

	@Override
	public List<EmployeeDto> getEmployeesByDepartmentId(Long departmentId) {
		try {
			Department department = departmentRepository.findById(departmentId)
					.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID + " for department"));
			List<Employee> employees = employeeRepository.findByDepartment(department);
			return employees.stream().map(e -> mapper.toEmployeeDto(e)).toList();
		} catch (InvalidInputException e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	@Override
	@Transactional
	public String deleteEmployeeInfo(Long employeeId, Long depId) {
		Department department = departmentRepository.findById(depId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID + " for department"));

		if ("Sales Department".equalsIgnoreCase(department.getName())) {
			return salesEmployeeService.deletedSalesEmployeeByEmployeApi(employeeId);
		} else if ("Information Technology".equalsIgnoreCase(department.getName())) {
			return developerService.deleteDeveloperInfo(employeeId);
		}

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new InvalidInputException("Data not found with this employee id"));
		User user = userRepo.findByUserCode(employee.getUserCode())
				.orElseThrow(() -> new InvalidInputException("User not found with this employee id"));
		userRepo.delete(user);
		employeeRepository.delete(employee);
		return AppConstants.DELETED_SUCCESFULLY;
	}

	@Override
	public List<EmployeeResponse> getAllEmployeeResponsesWithIncludeSaleTableAndDeveloperTable()
			throws DataNotFoundException, RuntimeException {
		try {
			List<Developer> developers = developerRepository.findAll();
			List<SalesDepartmentEmployee> salesEmployees = salesEmployeeRepo.findAll();
			List<Employee> employees = employeeRepository.findAll();

			return Stream.of(developers.stream().map(dev -> {
				EmployeeResponse response = new EmployeeResponse();
				response.setEmployeeId(dev.getDeveloperId());
				response.setFullName(dev.getFullName());
				response.setEmail(dev.getEmail());
				response.setMobileNumber(dev.getMobileNumber());
				response.setRole(dev.getRole());
				response.setDesignation(dev.getTechStack());
				response.setUserCode(dev.getUserCode());
				response.setDepartmentDTO(mapper.toDepartmentDto(dev.getDepartment()));
				return response;
			}), salesEmployees.stream().map(sales -> {
				EmployeeResponse response = new EmployeeResponse();
				response.setEmployeeId(sales.getEmpId());
				response.setFullName(sales.getFullName());
				response.setEmail(sales.getEmail());
				response.setMobileNumber(sales.getMobileNumber());
				response.setRole(sales.getRole());
				response.setDesignation(sales.getDesignation());
				response.setUserCode(sales.getUserCode());
				response.setDepartmentDTO(mapper.toDepartmentDto(sales.getDepartment()));
				return response;
			}), employees.stream().map(emp -> {
				EmployeeResponse response = new EmployeeResponse();
				response.setEmployeeId(emp.getEmployeeId());
				response.setFullName(emp.getFullName());
				response.setEmail(emp.getEmail());
				response.setMobileNumber(emp.getMobileNumber());
				response.setRole(emp.getRole());
				response.setDesignation(emp.getDesignation());
				response.setUserCode(emp.getUserCode());
				response.setDepartmentDTO(mapper.toDepartmentDto(emp.getDepartment()));
				return response;
			})).flatMap(s -> s).collect(Collectors.toList());

		} catch (Exception e) {
			log.error("Error while fetching employee responses", e);
			throw new RuntimeException("Something went wrong while fetching employee responses", e);
		}
	}

}