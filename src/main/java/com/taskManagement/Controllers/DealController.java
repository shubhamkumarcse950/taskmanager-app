package com.taskManagement.Controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Dtos.DealDTO;
import com.taskManagement.Service.DealService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/deals")
@CrossOrigin(origins = "*")
@Tag(name = "Client-deal API")
public class DealController {

	private final DealService dealService;

	public DealController(DealService dealService) {
		super();
		this.dealService = dealService;
	}

	@PostMapping(value = "/create-deal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public DealDTO createDeal(@ModelAttribute DealDTO dealDTO) throws IOException {
		return dealService.createDeal(dealDTO);
	}

	@PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public DealDTO updateDeal(@ModelAttribute DealDTO dealDTO) throws IOException {
		return dealService.createOrUpdateDeal(dealDTO);
	}

	@GetMapping("/{id}")
	public DealDTO getDeal(@PathVariable Long id) {
		return dealService.getDealById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteDeal(@PathVariable Long id) {
		dealService.deleteDeal(id);
	}

	@GetMapping
	public List<DealDTO> getAllDeals() {
		return dealService.getAllDeals();
	}

	@GetMapping("/prposal")
	public ResponseEntity<?> getPrposal(@RequestParam Long id) {
		String response = dealService.getPrposial(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/byUserCode")
	public List<DealDTO> getByUserCode(@RequestParam String userCode) {
		return dealService.getAllDealByUserCode(userCode);
	}

	@PutMapping("/isApproved")
	public ResponseEntity<?> putMethodName(@RequestParam Long dealId, @RequestParam String isApproved) {

		String response = dealService.isApproved(dealId, isApproved);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/client-deal")
	public List<DealDTO> getAllDealByClientCode(@RequestParam String userCode) {
		return dealService.getAllClientDeal(userCode);
	}

}
