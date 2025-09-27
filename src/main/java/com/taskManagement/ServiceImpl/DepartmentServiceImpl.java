package com.taskManagement.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.DepartmentDTO;
import com.taskManagement.Entitys.Department;
import com.taskManagement.Entitys.Developer;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DepartmentRepository;
import com.taskManagement.Repository.DeveloperRepository;
import com.taskManagement.Repository.SalesDepartmentEmployeeRepo;
import com.taskManagement.Service.DepartmentService;
import com.taskManagement.Service.UserServices;
import com.taskManagement.exceptions.DataNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final DeveloperRepository developerRepository;
	private final SalesDepartmentEmployeeRepo salesEmployeeRepo;
	private final LeadManagmentMapper mapper;
	private final UserServices userServices;

	public DepartmentServiceImpl(DepartmentRepository departmentRepository,
			SalesDepartmentEmployeeRepo salesEmployeeRepo, DeveloperRepository developerRepository,
			UserServices userServices, LeadManagmentMapper mapper) {
		this.departmentRepository = departmentRepository;
		this.developerRepository = developerRepository;
		this.salesEmployeeRepo = salesEmployeeRepo;
		this.mapper = mapper;
		this.userServices = userServices;
	}

	@Override
	public DepartmentDTO createDepartment(DepartmentDTO dto) {
		if (departmentRepository.existsByName(dto.getName())) {
			throw new RuntimeException("Department already exists with name: " + dto.getName());
		}

		Department department = new Department();
		department.setName(dto.getName());
		department.setDescription(dto.getDescription());
		Department saved = departmentRepository.save(department);
		return new DepartmentDTO(saved.getDepId(), saved.getName(), saved.getDescription());
	}

	@Override
	public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found"));

		department.setName(dto.getName());
		department.setDescription(dto.getDescription());

		Department updated = departmentRepository.save(department);
		return new DepartmentDTO(updated.getDepId(), updated.getName(), updated.getDescription());
	}

	@Override
	@Transactional
	public void deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("department not found with this id"));
		if ("Information Technology".equalsIgnoreCase(department.getName())) {
			List<Developer> developers = developerRepository.findAllByDepartment(department);
			for (Developer developer : developers) {
				userServices.deleteUser(developer.getEmail());
			}
		} else if ("Sales Department".equalsIgnoreCase(department.getName())) {
			List<SalesDepartmentEmployee> salesDepartmentEmployees = salesEmployeeRepo.findAllByDepartment(department);
			for (SalesDepartmentEmployee salesDepartmentEmployee : salesDepartmentEmployees) {
				userServices.deleteUser(salesDepartmentEmployee.getEmail());
			}
		}
		departmentRepository.deleteById(id);
	}

	@Override
	public DepartmentDTO getDepartmentById(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found"));
		return new DepartmentDTO(department.getDepId(), department.getName(), department.getDescription());
	}

	@Override
	public List<DepartmentDTO> getAllDepartments() {
		return departmentRepository.findAll().stream()
				.map(d -> new DepartmentDTO(d.getDepId(), d.getName(), d.getDescription()))
				.collect(Collectors.toList());
	}
}
