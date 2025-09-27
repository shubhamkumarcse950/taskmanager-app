package com.taskManagement.Service;

import java.io.IOException;
import java.util.List;

import com.taskManagement.Dtos.DealDTO;

public interface DealService {
	DealDTO createDeal(DealDTO dealDTO) throws IOException;

	DealDTO getDealById(Long id);

	void deleteDeal(Long id);

	List<DealDTO> getAllDeals();

	String getPrposial(Long id);

	DealDTO createOrUpdateDeal(DealDTO dealDTO) throws IOException;

	List<DealDTO> getAllDealByUserCode(String userCode);

	String isApproved(Long dealId, String isApproved);

	List<DealDTO> getAllClientDeal(String userCode);

}
