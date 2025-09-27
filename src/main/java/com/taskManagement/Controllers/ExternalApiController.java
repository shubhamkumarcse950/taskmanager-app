package com.taskManagement.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/external")
public class ExternalApiController {

	@GetMapping("/photos")
	public ResponseEntity<String> getFromLaravel() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://jsonplaceholder.typicode.com/photos";

		String response = restTemplate.getForObject(url, String.class);
		return ResponseEntity.ok(response);
	}
}
