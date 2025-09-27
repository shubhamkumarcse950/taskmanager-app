
package com.taskManagement.Controllers;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.LeadDTO;
import com.taskManagement.Service.LeadService;
import com.taskManagement.responsemodel.AppConstants;
import com.taskManagement.responsemodel.ResponseWithObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeadController {

	private final LeadService leadService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> uploadLeads(@RequestParam("file") MultipartFile file,
			@RequestParam String userCode) {
		Map<String, Object> response = leadService.uploadLeads(file, userCode);
		return ResponseEntity.status(response.get("type").equals("success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
				.body(response);
	}

	@GetMapping
	public ResponseEntity<Page<LeadDTO>> getAllLeads(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String name,
			@RequestParam(required = false) String location, @RequestParam(required = false) String status,
			@RequestParam(required = false) String requirement) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LeadDTO> leads = leadService.getAllLeads(pageable, name, location, status, requirement);
		return ResponseEntity.ok(leads);
	}

	@GetMapping("/download/template")
	public ResponseEntity<Resource> downloadTemplate() {
		ByteArrayInputStream stream = leadService.downloadTemplate();
		InputStreamResource resource = new InputStreamResource(stream);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leads_template.csv");
		headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	@PutMapping("/update-lead-status")
	public ResponseEntity<Object> updateLeadStatus(@RequestParam Long id, @RequestParam String status) {
		String response = this.leadService.updateLeadStatus(id, status);
		if (response.contains(AppConstants.SUCCESS)) {
			return new ResponseWithObject().generateResponse(AppConstants.ACCEPT, HttpStatus.OK, response);
		}
		return new ResponseWithObject().generateResponse(AppConstants.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR, "Error|something went wrong");

	}

	@DeleteMapping("/delete")
	public ResponseEntity<Object> delete(@RequestParam Long id) {
		return new ResponseWithObject().generateResponse(AppConstants.DELETED_SUCCESFULLY, HttpStatus.OK,
				leadService.delete(id));
	}

	@GetMapping("/employee-lead")
	public ResponseEntity<List<LeadDTO>> getMethodName(@RequestParam String userCode) {
		List<LeadDTO> page = leadService.getAllLeads(userCode);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@PostMapping("/manuly")
	public ResponseEntity<String> saveLeadManuly(@RequestBody LeadDTO leadDTO) throws RuntimeException {
		return new ResponseEntity<>(leadService.saveLead(leadDTO), HttpStatus.OK);
	}
}