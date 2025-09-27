package com.taskManagement.ServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.LeadAssignDto;
import com.taskManagement.Entitys.CustomLead;
import com.taskManagement.Entitys.JustDialLead;
import com.taskManagement.Entitys.Lead;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Entitys.LeadFollowUp;
import com.taskManagement.Entitys.SalesDepartmentEmployee;
import com.taskManagement.Entitys.User;
import com.taskManagement.Entitys.WebsiteLead;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.CustomLeadRepository;
import com.taskManagement.Repository.JustDialLeadRepo;
import com.taskManagement.Repository.LeadAssignRepo;
import com.taskManagement.Repository.LeadFollowUpRepo;
import com.taskManagement.Repository.LeadRepository;
import com.taskManagement.Repository.SalesDepartmentEmployeeRepo;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Repository.WebsiteLeadRepo;
import com.taskManagement.Service.LeadAssignService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InternalServerException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.outputdto.LeadAssignOutPutDto;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeadAssignServiceImpl implements LeadAssignService {

	private final LeadAssignRepo leadAssignRepo;
	private final LeadManagmentMapper mapper;
	private final SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo;
	private final JustDialLeadRepo justDialLeadRepo;
	private final WebsiteLeadRepo websiteLeadRepo;
	private final LeadRepository bulkUploadRepo;
	private final CustomLeadRepository customLeadRepository;
	private final UserRepo userRepo;
	private final LeadFollowUpRepo followUpRepo;

	public LeadAssignServiceImpl(LeadAssignRepo leadAssignRepo, LeadManagmentMapper mapper,
			SalesDepartmentEmployeeRepo salesDepartmentEmployeeRepo, WebsiteLeadRepo websiteLeadRepo,
			JustDialLeadRepo justDialLeadRepo, CustomLeadRepository customLeadRepository, LeadRepository bulkUploadRepo,
			UserRepo userRepo, LeadFollowUpRepo followUpRepo) {
		super();
		this.leadAssignRepo = leadAssignRepo;
		this.mapper = mapper;
		this.salesDepartmentEmployeeRepo = salesDepartmentEmployeeRepo;
		this.justDialLeadRepo = justDialLeadRepo;
		this.websiteLeadRepo = websiteLeadRepo;
		this.bulkUploadRepo = bulkUploadRepo;
		this.customLeadRepository = customLeadRepository;
		this.userRepo = userRepo;
		this.followUpRepo = followUpRepo;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public LeadAssignDto leadAssignToEmployee(LeadAssignDto leadAssignDto) {
		if (leadAssignDto.getEmpId() == null) {
			throw new InvalidInputException("Employee ID is required.");
		}

		try {
			SalesDepartmentEmployee salesEmployee = salesDepartmentEmployeeRepo.findById(leadAssignDto.getEmpId())
					.orElseThrow(() -> new InvalidInputException("Employee ID not found in the database"));

			LeadAssign leadAssign = mapper.toLeadAssign(leadAssignDto);
			for (Map.Entry<Long, String> entry : leadAssign.getLeads().entrySet()) {
				Long id = entry.getKey();
				String source = entry.getValue();
				if (source.equalsIgnoreCase("Custom")) {
					CustomLead customLead = customLeadRepository.findById(id)
							.orElseThrow(() -> new DataNotFoundException("Custom Lead not found with ID: " + id));
					customLead.setAssignStatus(true);
					customLeadRepository.save(customLead);
				} else if (source.equalsIgnoreCase("Website")) {
					WebsiteLead websiteLead = websiteLeadRepo.findById(id)
							.orElseThrow(() -> new DataNotFoundException("Website Lead not found with ID: " + id));
					websiteLead.setAssignStatus(true);
					websiteLeadRepo.save(websiteLead);
				} else if (source.equalsIgnoreCase("Upload")) {
					Lead bulkupLead = bulkUploadRepo.findById(id)
							.orElseThrow(() -> new DataNotFoundException("Upload Lead not found with ID: " + id));
					bulkupLead.setAssignStatus(true);
					bulkUploadRepo.save(bulkupLead);
				} else if (source.equalsIgnoreCase("JustDial")) {
					JustDialLead justDialLead = justDialLeadRepo.findById(id)
							.orElseThrow(() -> new DataNotFoundException("JustDial Lead not found with ID: " + id));
					justDialLead.setAssignStatus(true);
					justDialLeadRepo.save(justDialLead);
				} else {
					throw new InvalidInputException("Unknown lead source: " + source);
				}

			}
			leadAssign.setSalesDepartmentEmployee(salesEmployee);
			leadAssign.setAssignDate(LocalDate.now());

			leadAssign = leadAssignRepo.save(leadAssign);

			leadAssignDto = mapper.toLeadAssignDto(leadAssign);
			leadAssignDto.setEmpId(salesEmployee.getEmpId());
			return leadAssignDto;
		} catch (RuntimeException e) {
			log.error("Error occurred while saving LeadAssign: ", e);
			throw new InternalServerException("Internal server error while assigning lead.");
		}
	}

	@Override
	public List<LeadAssignOutPutDto> getAllAssignLeadByUserCode(String userCode) {
		List<LeadAssignOutPutDto> resultList = new ArrayList<>();

		try {
			SalesDepartmentEmployee employee = salesDepartmentEmployeeRepo.findByUserCode(userCode)
					.orElseThrow(() -> new DataNotFoundException("Sales employee not found with this user code"));

			List<LeadAssign> leadAssigns = leadAssignRepo.findBySalesDepartmentEmployee_EmpId(employee.getEmpId());

			if (!leadAssigns.isEmpty()) {
				for (LeadAssign leadAssign : leadAssigns) {
					Map<Long, String> leadMap = leadAssign.getLeads();
					List<Object> leadList = new ArrayList<>();

					for (Map.Entry<Long, String> entry : leadMap.entrySet()) {
						Long id = entry.getKey();
						String source = entry.getValue();

						if (source.equalsIgnoreCase("Custom")) {
							Optional<CustomLead> customLead = customLeadRepository.findById(id);
							if (customLead.isEmpty()) {
								continue;
							}
							leadList.add(customLead.get());

						} else if (source.equalsIgnoreCase("Website")) {
							Optional<WebsiteLead> websiteLead = websiteLeadRepo.findById(id);
							if (websiteLead.isEmpty()) {
								continue;
							}
							leadList.add(websiteLead.get());

						} else if (source.equalsIgnoreCase("Upload")) {
							Optional<Lead> bulkupLead = bulkUploadRepo.findById(id);
							if (bulkupLead.isEmpty()) {
								continue;
							}
							leadList.add(bulkupLead.get());

						} else if (source.equalsIgnoreCase("Just-Dial")) {
							Optional<JustDialLead> justDialLead = justDialLeadRepo.findById(id);
							if (justDialLead.isEmpty()) {
								continue;
							}
							leadList.add(justDialLead.get());

						} else {
							continue;
						}
					}
					LeadAssignOutPutDto dto = mapper.toLeadAssignOutPutDto(leadAssign);
					dto.setLeads(leadList);
					resultList.add(dto);
				}
			}
			return resultList;
		} catch (Exception e) {
			log.error("Error occurred while fetching assigned leads: {}", e.getMessage());
			throw new InternalServerException("Internal server error while fetching lead assignments.");
		}
	}

//	@Transactional(rollbackOn = Exception.class)
//	@Override
//	public String autoAssignLeadsEqually(String userCode) {
//		// 0. Validate assigning user
//		User assigningUser = userRepo.findByUserCode(userCode)
//				.orElseThrow(() -> new DataNotFoundException("Assigning user not found with user code: " + userCode));
//
//		// 1. Get all sales employees
//		List<SalesDepartmentEmployee> employees = salesDepartmentEmployeeRepo.findAll();
//		if (employees.isEmpty()) {
//			return "No sales employees found for auto assignment.";
//		}
//
//		// 2. Get all unassigned leads from each source
//		List<CustomLead> unassignedCustomLeads = customLeadRepository.findByAssignStatus(false);
//		List<WebsiteLead> unassignedWebsiteLeads = websiteLeadRepo.findByAssignStatus(false);
//		List<Lead> unassignedUploadLeads = bulkUploadRepo.findByAssignStatus(false);
//		List<JustDialLead> unassignedJustDialLeads = justDialLeadRepo.findByAssignStatus(false);
//
//		// Combine all into a single list with source info
//		List<LeadInfo> allUnassignedLeads = new ArrayList<>();
//		unassignedCustomLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "Custom")));
//		unassignedWebsiteLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getWebLeadId(), "Website")));
//		unassignedUploadLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "Upload")));
//		unassignedJustDialLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "JustDial")));
//
//		int totalLeads = allUnassignedLeads.size();
//		int totalEmployees = employees.size();
//
//		// 3. Check if division is exact
//		if (totalLeads == 0) {
//			return "No unassigned leads available for distribution.";
//		}
//		if (totalLeads % totalEmployees != 0) {
//			return "Leads cannot be equally divided among employees. Assignment skipped.";
//		}
//
//		int leadsPerEmployee = totalLeads / totalEmployees;
//		int index = 0;
//
//		// 4. Assign equally
//		for (SalesDepartmentEmployee emp : employees) {
//			List<LeadInfo> leadsForThisEmp = allUnassignedLeads.subList(index, index + leadsPerEmployee);
//
//			LeadAssign leadAssign = new LeadAssign();
//			leadAssign.setSalesDepartmentEmployee(emp);
//			leadAssign.setAssignDate(LocalDate.now());
//			leadAssign.setUserCode(assigningUser.getUserCode());
//
//			// Convert LeadInfo to Map<Long,String>
//			Map<Long, String> leadMap = leadsForThisEmp.stream()
//					.collect(Collectors.toMap(LeadInfo::getId, LeadInfo::getSource));
//
//			leadAssign.setLeads(leadMap);
//			leadAssignRepo.save(leadAssign);
//
//			// Update assignStatus in respective tables
//			for (LeadInfo li : leadsForThisEmp) {
//				switch (li.getSource().toLowerCase()) {
//				case "custom" -> {
//					CustomLead cl = customLeadRepository.findById(li.getId()).get();
//					cl.setAssignStatus(true);
//					customLeadRepository.save(cl);
//				}
//				case "website" -> {
//					WebsiteLead wl = websiteLeadRepo.findById(li.getId()).get();
//					wl.setAssignStatus(true);
//					websiteLeadRepo.save(wl);
//				}
//				case "upload" -> {
//					Lead ul = bulkUploadRepo.findById(li.getId()).get();
//					ul.setAssignStatus(true);
//					bulkUploadRepo.save(ul);
//				}
//				case "justdial" -> {
//					JustDialLead jl = justDialLeadRepo.findById(li.getId()).get();
//					jl.setAssignStatus(true);
//					justDialLeadRepo.save(jl);
//				}
//				}
//			}
//
//			index += leadsPerEmployee;
//		}
//
//		return "Auto assignment completed successfully. " + leadsPerEmployee + " leads assigned to each of "
//				+ totalEmployees + " employees.";
//	}
//
//	private static class LeadInfo {
//		private final Long id;
//		private final String source;
//
//		public LeadInfo(Long id, String source) {
//			this.id = id;
//			this.source = source;
//		}
//
//		public Long getId() {
//			return id;
//		}
//
//		public String getSource() {
//			return source;
//		}
//	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public String autoAssignLeadsEqually(String userCode, List<Long> empIds) {
		User assigningUser = userRepo.findByUserCode(userCode)
				.orElseThrow(() -> new DataNotFoundException("Assigning user not found with user code: " + userCode));

		List<SalesDepartmentEmployee> employees = salesDepartmentEmployeeRepo.findAll();
		if (employees.isEmpty()) {
			return "No sales employees found for auto assignment.";
		}
		employees.removeIf(emp -> empIds.contains(emp.getEmpId()));
		log.info("Remaining employees after filtering: {}", employees.size());

		List<CustomLead> unassignedCustomLeads = customLeadRepository.findByAssignStatus(false);
		List<WebsiteLead> unassignedWebsiteLeads = websiteLeadRepo.findByAssignStatus(false);
		List<Lead> unassignedUploadLeads = bulkUploadRepo.findByAssignStatus(false);
		List<JustDialLead> unassignedJustDialLeads = justDialLeadRepo.findByAssignStatus(false);

		List<LeadInfo> allUnassignedLeads = new ArrayList<>();
		unassignedCustomLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "Custom")));
		unassignedWebsiteLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getWebLeadId(), "Website")));
		unassignedUploadLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "Upload")));
		unassignedJustDialLeads.forEach(l -> allUnassignedLeads.add(new LeadInfo(l.getId(), "JustDial")));

		int totalLeads = allUnassignedLeads.size();
		int totalEmployees = employees.size();

		if (totalLeads == 0) {
			return "No unassigned leads available for distribution.";
		}

		int leadsPerEmployee = totalLeads / totalEmployees;
		if (leadsPerEmployee == 0) {
			return "Not enough leads to assign at least one per employee.";
		}

		int index = 0;
		for (SalesDepartmentEmployee emp : employees) {
			if (index + leadsPerEmployee > totalLeads) {
				break;
			}

			List<LeadInfo> leadsForThisEmp = allUnassignedLeads.subList(index, index + leadsPerEmployee);

			LeadAssign leadAssign = new LeadAssign();
			leadAssign.setSalesDepartmentEmployee(emp);
			leadAssign.setAssignDate(LocalDate.now());
			leadAssign.setUserCode(assigningUser.getUserCode());

			Map<Long, String> leadMap = leadsForThisEmp.stream()
					.collect(Collectors.toMap(LeadInfo::getId, LeadInfo::getSource));

			leadAssign.setLeads(leadMap);
			leadAssignRepo.save(leadAssign);

			for (LeadInfo li : leadsForThisEmp) {
				switch (li.getSource().toLowerCase()) {
				case "custom" -> {
					CustomLead cl = customLeadRepository.findById(li.getId()).get();
					cl.setAssignStatus(true);
					customLeadRepository.save(cl);
				}
				case "website" -> {
					WebsiteLead wl = websiteLeadRepo.findById(li.getId()).get();
					wl.setAssignStatus(true);
					websiteLeadRepo.save(wl);
				}
				case "upload" -> {
					Lead ul = bulkUploadRepo.findById(li.getId()).get();
					ul.setAssignStatus(true);
					bulkUploadRepo.save(ul);
				}
				case "Just-dial" -> {
					JustDialLead jl = justDialLeadRepo.findById(li.getId()).get();
					jl.setAssignStatus(true);
					justDialLeadRepo.save(jl);
				}
				}
			}

			index += leadsPerEmployee;
		}

		int remainderLeads = totalLeads % totalEmployees;
		return "Auto assignment completed. " + leadsPerEmployee + " leads assigned to each of " + totalEmployees
				+ " employees. " + remainderLeads + " leads left unassigned for manual distribution.";
	}

	private static class LeadInfo {
		private final Long id;
		private final String source;

		public LeadInfo(Long id, String source) {
			this.id = id;
			this.source = source;
		}

		public Long getId() {
			return id;
		}

		public String getSource() {
			return source;
		}
	}

	@Override
	public String addLeadfollowUp(LeadFollowUp folloUp) {
		try {
			if (folloUp == null) {
				throw new InvalidInputException("Follow-up text is empty, please provide a valid follow-up note");
			}

			switch (folloUp.getSourceType().toLowerCase()) {
			case "custom" -> {
				customLeadRepository.findById(folloUp.getLeadId())
						.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
				followUpRepo.save(folloUp);

			}
			case "website" -> {
				websiteLeadRepo.findById(folloUp.getLeadId())
						.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
				followUpRepo.save(folloUp);
			}
			case "upload" -> {
				bulkUploadRepo.findById(folloUp.getLeadId())
						.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
				followUpRepo.save(folloUp);
			}
			case "Just-dial" -> {
				justDialLeadRepo.findById(folloUp.getLeadId())
						.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
				followUpRepo.save(folloUp);
			}
			default -> {
				return "SourceType is not found in this lead";
			}
			}

			return AppConstants.SUCCESS;

		} catch (InvalidInputException | HibernateException e) {
			log.error("Error occurred while saving follow-up list: {}", e.getMessage());
			return AppConstants.ERROR;
		}
	}

	@Override
	public List<LeadFollowUp> getAllFollowUps(Long leadId, String sourceType) {

		switch (sourceType) {
		case "Custom" -> {
			CustomLead cl = customLeadRepository.findById(leadId)
					.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
			return followUpRepo.findByLeadIdAndSourceType(cl.getId(), sourceType);
		}
		case "Website" -> {
			WebsiteLead wl = websiteLeadRepo.findById(leadId)
					.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
			return followUpRepo.findByLeadIdAndSourceType(wl.getWebLeadId(), sourceType);
		}
		case "Upload" -> {
			Lead ul = bulkUploadRepo.findById(leadId)
					.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
			return followUpRepo.findByLeadIdAndSourceType(ul.getId(), sourceType);
		}
		case "Just-dial" -> {
			JustDialLead jl = justDialLeadRepo.findById(leadId)
					.orElseThrow(() -> new DataNotFoundException("Lead not found with this id or source type"));
			return followUpRepo.findByLeadIdAndSourceType(jl.getId(), sourceType);
		}
		default -> throw new IllegalArgumentException("Unknown sourceType: " + sourceType);
		}
	}

}
