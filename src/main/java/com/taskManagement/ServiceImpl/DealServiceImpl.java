package com.taskManagement.ServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.DealDTO;
import com.taskManagement.Entitys.DealEntity;
import com.taskManagement.Entitys.User;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.DealRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.DealService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.responsemodel.AppConstants;

@Service
public class DealServiceImpl implements DealService {

	private final DealRepository dealRepository;
	private final LeadManagmentMapper mapper;
	private final UserRepo userRepo;
	private final PasswordEncoder encoder;
	private final EmailService emailService;

	public DealServiceImpl(DealRepository dealRepository, LeadManagmentMapper mapper, UserRepo userRepo,
			PasswordEncoder encoder, EmailService emailService) {
		this.dealRepository = dealRepository;
		this.mapper = mapper;
		this.userRepo = userRepo;
		this.encoder = encoder;
		this.emailService = emailService;
	}

	@Override
	public DealDTO createDeal(DealDTO dealDTO) throws IOException {
		DealEntity entity = new DealEntity();
		BeanUtils.copyProperties(dealDTO, entity);
		System.out.println(dealDTO.getProposalUpload() + ".....................................");
		entity.setProposalUpload(dealDTO.getProposalUpload() != null ? dealDTO.getProposalUpload().getBytes() : null);
		entity.setDealDate(LocalDate.now());

		String clientUserCode = resolveClientUserCode(dealDTO);

		entity.setClientUserCode(clientUserCode);
		DealEntity saved = dealRepository.save(entity);

		BeanUtils.copyProperties(saved, dealDTO);
		dealDTO.setProposalUpload(null);
		return dealDTO;
	}

	@Override
	public DealDTO createOrUpdateDeal(DealDTO dealDTO) throws IOException {
		DealEntity entity;

		if (dealDTO.getId() != null) {
			entity = dealRepository.findById(Long.valueOf(dealDTO.getId()))
					.orElseThrow(() -> new DataNotFoundException("Deal not found with id: " + dealDTO.getId()));
		} else {
			entity = new DealEntity();
			entity.setDealDate(LocalDate.now());
		}

		BeanUtils.copyProperties(dealDTO, entity);

		if (dealDTO.getProposalUpload() != null) {
			entity.setProposalUpload(dealDTO.getProposalUpload().getBytes());
		}

		String clientUserCode = resolveClientUserCode(dealDTO);
		entity.setClientUserCode(clientUserCode);

		DealEntity saved = dealRepository.save(entity);

		BeanUtils.copyProperties(saved, dealDTO);
		dealDTO.setProposalUpload(null);
		return dealDTO;
	}

	@Override
	public DealDTO getDealById(Long id) {
		DealEntity entity = dealRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Deal not found with id: " + id));

		DealDTO dto = new DealDTO();
		BeanUtils.copyProperties(entity, dto);
		dto.setProposalUpload(null);
		return dto;
	}

	@Override
	public void deleteDeal(Long id) {
		if (!dealRepository.existsById(id)) {
			throw new DataNotFoundException("Deal not found with id: " + id);
		}
		dealRepository.deleteById(id);
	}

	@Override
	public List<DealDTO> getAllDeals() {
		return dealRepository.findAll().stream().map(entity -> {
			DealDTO dto = new DealDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setProposalUpload(null);
			return dto;
		}).toList();
	}

	@Override
	public String getPrposial(Long id) {
		DealEntity dealEntity = this.dealRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Proposal not found with this Id"));

		if (dealEntity.getProposalUpload() == null) {
			throw new DataNotFoundException("Proposal not found  jkgujgkjjljk");
		}

		return Base64.getEncoder().encodeToString(dealEntity.getProposalUpload());
	}

	@Override
	public List<DealDTO> getAllDealByUserCode(String userCode) {
		List<DealEntity> list = dealRepository.findByUserCode(userCode);

		return list.stream().map(d -> {
			DealDTO dto = mapper.toDealDto(d);
			dto.setProposalUpload(null);
			return dto;
		}).toList();
	}

	@Override
	public String isApproved(Long dealId, String isApproved) {
		DealEntity dealEntity = dealRepository.findById(dealId)
				.orElseThrow(() -> new InvalidInputException(AppConstants.INVALID_INPUT_ID));

		dealEntity.setIsApprovedByAdmin(isApproved);
		dealRepository.save(dealEntity);

		return AppConstants.SUCCESS;
	}

	private String resolveClientUserCode(DealDTO dealDTO) {
		User existingUser = userRepo.findByEmail(dealDTO.getEmail());

		if (dealDTO.getAdvanceAmount() != null) {
			if (existingUser == null) {
				String clientUserCode = UUID.randomUUID().toString();
				String rawPassword = "123456";

				User user = createNewClientUser(dealDTO, clientUserCode, rawPassword);
				try {
					emailService.sendClientOnboardingEmail(user.getEmail(), user.getName(), user.getEmail(),
							rawPassword, dealDTO.getName(), dealDTO.getValue(), dealDTO.getSalesPersonName());
				} catch (Exception e) {
					System.err.println("⚠️ Failed to send onboarding email: " + e.getMessage());
				}

				return clientUserCode;
			} else {
				try {
					emailService.sendAdditionalDealEmail(existingUser.getEmail(), existingUser.getName(),
							dealDTO.getName(), dealDTO.getValue(), dealDTO.getSalesPersonName());
				} catch (Exception e) {
					System.err.println("⚠️ Failed to send additional deal email: " + e.getMessage());
				}

				return existingUser.getUserCode();
			}
		}

		return UUID.randomUUID().toString();
	}

	private User createNewClientUser(DealDTO dealDTO, String clientUserCode, String rawPassword) {
		User user = new User();
		user.setUserCode(clientUserCode);
		user.setName(dealDTO.getClientName());
		user.setEmail(dealDTO.getEmail());
		user.setContact(dealDTO.getPhone());
		user.setActive(true);
		user.setPassword(encoder.encode(rawPassword));
		user.setRoles(List.of("CLIENT"));
		user.setCreatedAt(LocalDateTime.now());

		return userRepo.save(user);
	}

	@Override
	public List<DealDTO> getAllClientDeal(String userCode) {
		List<DealEntity> getList = dealRepository.findByClientUserCode(userCode);
		return getList.stream().map(item -> mapper.toDealDto(item)).toList();
	}
}
