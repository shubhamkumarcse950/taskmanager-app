package com.taskManagement.ServiceImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.WebsiteLeadDto;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Entitys.WebsiteLead;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.LeadAssignRepo;
import com.taskManagement.Repository.WebsiteLeadRepo;
import com.taskManagement.Service.websiteLeadService;
import com.taskManagement.exceptions.InvalidInputException;

@Service
public class websiteLeadServiceImpl implements websiteLeadService {

	private final LeadManagmentMapper mapper;
	private final WebsiteLeadRepo websiteLeadRepo;
	private final LeadAssignRepo leadAssignRepo;

	public websiteLeadServiceImpl(LeadManagmentMapper mapper, WebsiteLeadRepo websiteLeadRepo,
			LeadAssignRepo leadAssignRepo) {
		this.mapper = mapper;
		this.websiteLeadRepo = websiteLeadRepo;
		this.leadAssignRepo = leadAssignRepo;
	}

	@Override
	public WebsiteLeadDto saveLead(WebsiteLeadDto dto) {
		WebsiteLead entity = mapper.dtoToEntity(dto);
		entity.setSource("Website");
		WebsiteLead savedEntity = websiteLeadRepo.save(entity);
		return mapper.entityToDto(savedEntity);
	}

	@Override
	public List<WebsiteLeadDto> getAllLeads() {
		List<WebsiteLead> leads = websiteLeadRepo.findAll();
		return leads.stream().map(lead -> mapper.entityToDto(lead)).toList();
	}

	@Override
	public boolean delete(Long id) {
		WebsiteLead customLead = websiteLeadRepo.findById(id)
				.orElseThrow(() -> new InvalidInputException("lead id not found!"));

		List<LeadAssign> list = leadAssignRepo.findAll();
		for (LeadAssign leadAssign : list) {
			Map<Long, String> map = leadAssign.getLeads();
			Iterator<Map.Entry<Long, String>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Long, String> entry = iterator.next();
				Long assignedId = entry.getKey();
				String source = entry.getValue();

				if (source.equalsIgnoreCase("Website") && assignedId.equals(id)) {
					iterator.remove();
				}
			}
		}

		leadAssignRepo.saveAll(list);
		websiteLeadRepo.delete(customLead);
		return true;
	}

}
