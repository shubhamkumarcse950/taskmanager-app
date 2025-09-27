package com.taskManagement.ServiceImpl;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.LeadDTO;
import com.taskManagement.Entitys.Lead;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.LeadAssignRepo;
import com.taskManagement.Repository.LeadRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.LeadService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

	private final LeadRepository leadRepository;
	private final FileParserUtil fileParserUtil;
	private final LeadAssignRepo leadAssignRepo;
	@Autowired
	private final LeadManagmentMapper mapper;
	@Autowired
	private UserRepo userRepo;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Map<String, Object> uploadLeads(MultipartFile file, String userCode) {
		try {
			User user = userRepo.findByUserCode(userCode).orElse(null);

			String finalUserCode = null;
			if (user != null && user.getRoles().contains("SALES")) {
				finalUserCode = userCode;
			}

			List<Lead> leads = fileParserUtil.parseFile(file);
			if (leads.isEmpty()) {
				return Map.of("type", "warning", "message",
						"No valid leads processed from " + file.getOriginalFilename());
			}

			// Batch assign karo
			for (Lead lead : leads) {
				lead.setSource("Upload");
				lead.setUserCode(finalUserCode);
			}

			List<Lead> savedLeads = leadRepository.saveAll(leads);

			return Map.of("type", "success", "message",
					"Successfully processed " + savedLeads.size() + " leads from " + file.getOriginalFilename(),
					"leads", savedLeads.stream().map(this::convertToDTO).collect(Collectors.toList()));
		} catch (Exception e) {
			return Map.of("type", "error", "message", "Failed to process file: " + e.getMessage());
		}
	}

	@Override
	public Page<LeadDTO> getAllLeads(Pageable pageable, String name, String location, String status,
			String requirement) {
		List<Lead> leads;

		if (name != null && !name.isEmpty()) {
			leads = leadRepository.findByNameContainingIgnoreCaseAndUserCodeIsNull(name);
		} else if (location != null && !location.isEmpty()) {
			leads = leadRepository.findByLocationContainingIgnoreCaseAndUserCodeIsNull(location);
		} else if (status != null && !status.isEmpty()) {
			leads = leadRepository.findByStatusAndUserCodeIsNull(status);
		} else if (requirement != null && !requirement.isEmpty()) {
			leads = leadRepository.findByRequirementContainingIgnoreCaseAndUserCodeIsNull(requirement);
		} else {
			leads = leadRepository.findByUserCodeIsNull();
		}

		List<LeadDTO> leadDTOs = leads.stream().map(this::convertToDTO).collect(Collectors.toList());

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), leadDTOs.size());
		List<LeadDTO> pagedDTOs = leadDTOs.subList(start, end);

		return new PageImpl<>(pagedDTOs, pageable, leadDTOs.size());
	}

	@Override
	public ByteArrayInputStream downloadTemplate() {
		List<LeadDTO> templateData = List.of(new LeadDTO() {
			{
				setName("John Doe");
				setPhone("+91-9876543210");
				setEmail("john@email.com");
				setRequirement("Website Development");
				setLocation("Mumbai, Maharashtra");
				setStatus("Cold");
				setCompany("ABC Corp");
				setDateAdded("2024-01-15");
			}
		}, new LeadDTO() {
			{
				setName("Jane Smith");
				setPhone("+91-8765432109");
				setEmail("jane@email.com");
				setRequirement("Digital Marketing");
				setLocation("Delhi, NCR");
				setStatus("Warm");
				setCompany("XYZ Ltd");
				setDateAdded("2024-01-14");
			}
		});
		return fileParserUtil.generateCsvTemplate(templateData);
	}

	private LeadDTO convertToDTO(Lead lead) {
		LeadDTO dto = new LeadDTO();
		dto.setId(lead.getId());
		dto.setName(lead.getName());
		dto.setPhone(lead.getPhone());
		dto.setEmail(lead.getEmail());
		dto.setRequirement(lead.getRequirement());
		dto.setLocation(lead.getLocation());
		dto.setStatus(lead.getStatus());
		dto.setCompany(lead.getCompany());
		dto.setDateAdded(lead.getDateAdded() != null ? lead.getDateAdded().format(DATE_FORMATTER) : null);
		return dto;
	}

	@Override
	public String updateLeadStatus(Long id, String status) {
		Lead lead = this.leadRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Error|Lead not found with this Id!"));
		lead.setStatus(status);
		leadRepository.save(lead);
		return AppConstants.SUCCESS;
	}

	@Override
	public boolean delete(Long id) {
		Lead customLead = leadRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("lead id not found!"));

		List<LeadAssign> list = leadAssignRepo.findAll();
		for (LeadAssign leadAssign : list) {
			Map<Long, String> map = leadAssign.getLeads();
			Iterator<Map.Entry<Long, String>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Long, String> entry = iterator.next();
				Long assignedId = entry.getKey();
				String source = entry.getValue();

				if (source.equalsIgnoreCase("Upload") && assignedId.equals(id)) {
					iterator.remove();
				}
			}
		}

		leadAssignRepo.saveAll(list);
		leadRepository.delete(customLead);
		return true;
	}

	@Override
	public List<LeadDTO> getAllLeads(String userCode) {
		List<Lead> leads = leadRepository.findByUserCode(userCode);
		return leads.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public String saveLead(LeadDTO dto) throws RuntimeException {
		User user = this.userRepo.findByUserCode(dto.getUserCode()).orElseThrow(
				() -> new UsernameNotFoundException("User not found with this user code" + dto.getUserCode()));
		Lead lead = mapper.toLeadDto(dto);
		lead.setUserCode(user.getUserCode());
		lead.setSource("Manuly");
		leadRepository.save(lead);
		return AppConstants.SUCCESS;
	}

}
