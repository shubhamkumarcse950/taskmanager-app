package com.taskManagement.responsemodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseWithObject {

	public ResponseEntity<?> genrateResponse(String message, HttpStatus status, String costumCode, Object object) {
		Map<String, Object> map = new HashMap<>();
		map.put(AppConstants.RSMG, message);
		map.put(AppConstants.RASTATUS, status.value());
		map.put(AppConstants.RCCODE, costumCode);
		map.put(AppConstants.RRES, object);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		return new ResponseEntity<>(map, httpHeaders, status.value());
	}

	public ResponseEntity<?> genrateResponse(String message, HttpStatus status, String costumCode, Object object,
			List<?> list) {
		Map<String, Object> map = new HashMap<>();
		map.put(AppConstants.RSMG, message);
		map.put(AppConstants.RASTATUS, status.value());
		map.put(AppConstants.RCCODE, costumCode);
		map.put(AppConstants.RRES, object);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		return new ResponseEntity<>(map, headers, status.value());

	}

	public ResponseEntity<Object> generateResponse(String message, HttpStatus status, String customcode,
			Map<String, String> hash) {
		Map<String, Object> map = new HashMap<>();
		map.put(AppConstants.RSMG, message);
		map.put(AppConstants.RASTATUS, status.value());
		map.put(AppConstants.RCCODE, customcode);
		map.put(AppConstants.RRES, hash);
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add(HttpHeaders.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		return new ResponseEntity<>(map, headers2, status.value());

	}

	public ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object object) {
		Map<String, Object> map = new HashMap<>();
		map.put(AppConstants.RSMG, message);
		map.put(AppConstants.RASTATUS, status.value());
		map.put(AppConstants.RRES, object);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, AppConstants.APPLICATION_JSON);
		return new ResponseEntity<>(map, headers, status.value());
	}

}
