package com.taskManagement.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pusher.rest.Pusher;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pusher")
public class PusherAuthController {

	@Autowired
	private Pusher pusher;

	@PostMapping("/auth")
	public ResponseEntity<?> auth(@RequestParam("channel_name") String channelName,
			@RequestParam("socket_id") String socketId, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		String username = authentication.getName();
		String userRole = authentication.getAuthorities().stream().findFirst()
				.map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")).orElse("DEVELOPER");

		boolean authorized = false;
		if (channelName.startsWith("private-user-") && channelName.equals("private-user-" + username)) {
			authorized = true;
		} else if (channelName.startsWith("private-role-") && channelName.equals("private-role-" + userRole)) {
			authorized = true;
		} else if (channelName.equals("announcements")) {
			authorized = true;
		}

		if (!authorized) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
		}
		String auth = pusher.authenticate(socketId, channelName);

		Map<String, String> response = new HashMap<>();
		response.put("auth", auth);

		return ResponseEntity.ok(response);
	}
}