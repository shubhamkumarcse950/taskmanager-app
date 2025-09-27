package com.taskManagement.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.JustDialLeadDto;
import com.taskManagement.Entitys.JustDialLead;
import com.taskManagement.Entitys.LeadAssign;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.JustDialLeadRepo;
import com.taskManagement.Repository.LeadAssignRepo;
import com.taskManagement.Service.JustDialLeadService;
import com.taskManagement.exceptions.InternalServerException;
import com.taskManagement.exceptions.InvalidInputException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JustDialLeadServiceImpl implements JustDialLeadService {
	private final JustDialLeadRepo justsDialLeadRepo;
	private final LeadManagmentMapper mapper;
	private final PasswordEncoder encoder;
	private final LeadAssignRepo leadAssignRepo;

	public JustDialLeadServiceImpl(JustDialLeadRepo justsDialLeadRepo, LeadManagmentMapper mapper,
			PasswordEncoder encoder, LeadAssignRepo leadAssignRepo) {
		super();
		this.justsDialLeadRepo = justsDialLeadRepo;
		this.mapper = mapper;
		this.encoder = encoder;
		this.leadAssignRepo = leadAssignRepo;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public JustDialLeadDto justDialLead(JustDialLeadDto dto) {
		try {
			JustDialLead justDialLead = this.mapper.toJustDialLead(dto);
			justDialLead.setSource("Just-dial");
			justDialLead.setTimestamp(LocalDateTime.now());
			justDialLead = justsDialLeadRepo.saveAndFlush(justDialLead);
			encoder.encode(justDialLead.toString());
			JustDialLeadDto leadDto = mapper.toJustDialLeadDto(justDialLead);
			return leadDto;
		} catch (Exception e) {
			log.error("Error|Internal server error...,{}", e.getMessage());
			throw new InternalServerException("Internal server error...");
		}
	}

	@Override
	public List<JustDialLeadDto> getAllJustDialLead() {
		return justsDialLeadRepo.findAll().stream().map(mapper::toJustDialLeadDto).toList();
	}

	@Override
	public boolean delete(Long id) {
		JustDialLead justDialLead = justsDialLeadRepo.findById(id)
				.orElseThrow(() -> new InvalidInputException("Lead ID not found!"));

		List<LeadAssign> list = leadAssignRepo.findAll();
		for (LeadAssign leadAssign : list) {
			Map<Long, String> map = leadAssign.getLeads();
			Iterator<Map.Entry<Long, String>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Long, String> entry = iterator.next();
				Long assignedId = entry.getKey();
				String source = entry.getValue();

				if (source.equalsIgnoreCase("Just-Dial") && assignedId.equals(id)) {
					iterator.remove();
				}
			}
		}

		leadAssignRepo.saveAll(list);
		justsDialLeadRepo.delete(justDialLead);
		return true;
	}

}