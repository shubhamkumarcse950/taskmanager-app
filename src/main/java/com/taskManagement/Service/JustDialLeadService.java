package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.JustDialLeadDto;

@Service
public interface JustDialLeadService {

	JustDialLeadDto justDialLead(JustDialLeadDto dto);

	List<JustDialLeadDto> getAllJustDialLead();

	boolean delete(Long id);

}
