package com.taskManagement.ServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.CustomLeadDto;
import com.taskManagement.Entitys.CustomLead;
import com.taskManagement.Entitys.JustDialLead;
import com.taskManagement.Entitys.Lead;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Entitys.WebsiteLead;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.CustomLeadRepository;
import com.taskManagement.Repository.JustDialLeadRepo;
import com.taskManagement.Repository.LeadAssignRepo;
import com.taskManagement.Repository.LeadRepository;
import com.taskManagement.Repository.WebsiteLeadRepo;
import com.taskManagement.Service.CustomLeadService;
import com.taskManagement.Service.JustDialLeadService;
import com.taskManagement.Service.LeadService;
import com.taskManagement.Service.websiteLeadService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InternalServerException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomLeadServiceImpl implements CustomLeadService {

	private final CustomLeadRepository repository;
	private final LeadManagmentMapper mapper;
	private final WebsiteLeadRepo websiteLeadRepo;
	private final JustDialLeadRepo justDialLeadRepo;
	private final LeadRepository leadRepository;
	private final LeadAssignRepo leadAssignRepo;

	public CustomLeadServiceImpl(CustomLeadRepository repository, LeadManagmentMapper mapper,
			WebsiteLeadRepo websiteLeadRepo, LeadRepository leadRepository, JustDialLeadRepo justDialLeadRepo,
			LeadAssignRepo leadAssignRepo, websiteLeadService websiteLeadService, LeadService leadService,
			JustDialLeadService justDialLeadService) {
		this.repository = repository;
		this.mapper = mapper;
		this.websiteLeadRepo = websiteLeadRepo;
		this.justDialLeadRepo = justDialLeadRepo;
		this.leadRepository = leadRepository;
		this.leadAssignRepo = leadAssignRepo;
	}

	@Override
	public CustomLeadDto saveCustomLead(CustomLeadDto dto) {
		CustomLead lead = mapper.toCustomLead(dto);
		lead.setSource("Custom");
		lead = repository.save(lead);
		return mapper.toCustomLeadDto(lead);
	}

	@Override
	public List<CustomLeadDto> getAllLeads() {
		return repository.findAll().stream().map(lead -> mapper.toCustomLeadDto(lead)).toList();
	}

	@Override
	public String updateLeadStatus(Long id, String status) {
		CustomLead customLead = this.repository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Error|data not found with this Id!"));
		customLead.setStatus(status);
		repository.save(customLead);
		return AppConstants.SUCCESS;
	}

	@Override
	public List<Object> getAllPlatformsLead() {
		List<Object> objects = new ArrayList<>();
		List<Lead> uploadLeads = leadRepository.findByAssignStatusFalseAndUserCodeIsNull();
		List<CustomLead> customLeads = repository.findByAssignStatus(false);
		List<WebsiteLead> websiteLeads = websiteLeadRepo.findByAssignStatus(false);
		List<JustDialLead> justDialLeads = justDialLeadRepo.findByAssignStatus(false);
		objects.add(justDialLeads);
		objects.add(uploadLeads);
		objects.add(websiteLeads);
		objects.add(customLeads);
		return objects;
	}

	@Override
	public boolean delete(Long id) {
		CustomLead customLead = repository.findById(id)
				.orElseThrow(() -> new InvalidInputException("lead id not found!"));
		List<LeadAssign> list = leadAssignRepo.findAll();
		for (LeadAssign leadAssign : list) {
			Map<Long, String> map = leadAssign.getLeads();
			Iterator<Map.Entry<Long, String>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Long, String> entry = iterator.next();
				Long asId = entry.getKey();
				String source = entry.getValue();
				if (source.equalsIgnoreCase("Custom") && asId.equals(id)) {
					iterator.remove();
				}
			}
		}
		leadAssignRepo.saveAll(list);
		repository.delete(customLead);
		return true;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public String updateLeadStatus(Long leadId, String source, String status) {
		try {
			switch (source) {
			case "Custom": {
				CustomLead lead = repository.findById(leadId)
						.orElseThrow(() -> new DataNotFoundException("Custom lead not found with ID: " + leadId));

				if ("Lost".equalsIgnoreCase(status)) {
					lead.setAssignStatus(false);
					delete_From_assignLead(leadId);
				}
				lead.setStatus(status);
				repository.save(lead);
				return "Custom lead status updated successfully.";
			}
			case "Website": {
				WebsiteLead lead = websiteLeadRepo.findById(leadId)
						.orElseThrow(() -> new DataNotFoundException("Website lead not found with ID: " + leadId));
				if ("Lost".equalsIgnoreCase(status)) {
					delete_From_assignLead(leadId);
					lead.setAssignStatus(false);
				}
				lead.setStatus(status);
				websiteLeadRepo.save(lead);
				return "Website lead status updated successfully.";
			}
			case "Upload": {
				Lead lead = leadRepository.findById(leadId)
						.orElseThrow(() -> new DataNotFoundException("Upload lead not found with ID: " + leadId));
				if ("Lost".equalsIgnoreCase(status)) {
					delete_From_assignLead(leadId);
					lead.setAssignStatus(false);
				}
				lead.setStatus(status);
				leadRepository.save(lead);
				return "Upload lead status updated successfully.";
			}
			case "Just-Dial": {
				JustDialLead lead = justDialLeadRepo.findById(leadId)
						.orElseThrow(() -> new DataNotFoundException("JustDial lead not found with ID: " + leadId));
				if ("Lost".equalsIgnoreCase(status)) {
					delete_From_assignLead(leadId);
					lead.setAssignStatus(false);
				}
				lead.setStatus(status);
				justDialLeadRepo.save(lead);
				return "JustDial lead status updated successfully.";
			}
			default:
				throw new IllegalArgumentException("Unknown lead source: " + source);
			}
		} catch (Exception e) {
			log.error("Error updating lead status: {}", e.getMessage());
			throw new InternalServerException("Failed to update lead status.");
		}
	}

	public void delete_From_assignLead(Long id) {
		List<LeadAssign> list = leadAssignRepo.findAll();
		List<LeadAssign> toDelete = new ArrayList<>();

		for (LeadAssign leadAssign : list) {
			Map<Long, String> map = leadAssign.getLeads();
			Iterator<Map.Entry<Long, String>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Long, String> entry = iterator.next();
				Long asId = entry.getKey();
				String source = entry.getValue();

				if (("Custom".equalsIgnoreCase(source) || "Upload".equalsIgnoreCase(source)
						|| "Website".equalsIgnoreCase(source) || "Just-Dial".equalsIgnoreCase(source))
						&& asId.equals(id)) {
					iterator.remove();
				}
			}
			if (map.isEmpty()) {
				toDelete.add(leadAssign);
			}
		}
		leadAssignRepo.saveAll(list.stream().filter(l -> !toDelete.contains(l)).toList());
		if (!toDelete.isEmpty()) {
			leadAssignRepo.deleteAll(toDelete);
		}
	}

}
