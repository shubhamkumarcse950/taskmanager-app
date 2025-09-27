package com.taskManagement.ServiceImpl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pusher.rest.Pusher;
import com.pusher.rest.data.Result;
import com.taskManagement.Dtos.NotificationDto;

@Service
public class NotificationService {

	@Autowired
	private Pusher pusher;

	/**
	 * Send notification to a specific user
	 * 
	 * @param username The user's username or userCode
	 * @param dto      The notification details (title, message, type)
	 */
	public void sendToUser(String username, NotificationDto dto) {
		if (username == null || dto == null) {
			throw new IllegalArgumentException("Username and DTO must not be null");
		}
		dto.setTimestamp(Instant.now().toString());
		Result result = pusher.trigger("private-user-" + username, "notification", dto);
		if (result.getStatus() != Result.Status.SUCCESS) {
			System.err.println("Failed to send user notification: " + result.getMessage());
		}
	}

	/**
	 * Send notification to a specific role
	 * 
	 * @param role The user role (e.g., EMPLOYEE, SALES_EMP)
	 * @param dto  The notification details
	 */
	public void sendToRole(String role, NotificationDto dto) {
		if (role == null || dto == null) {
			throw new IllegalArgumentException("Role and DTO must not be null");
		}
		dto.setTimestamp(Instant.now().toString());
		Result result = pusher.trigger("private-role-" + role, "notification", dto);
		if (result.getStatus() != Result.Status.SUCCESS) {
			System.err.println("Failed to send role notification: " + result.getMessage());
		}
	}

	/**
	 * Send general announcement
	 * 
	 * @param dto The notification details
	 */
	public void sendAnnouncement(NotificationDto dto) {
		if (dto == null) {
			throw new IllegalArgumentException("DTO must not be null");
		}
		dto.setTimestamp(Instant.now().toString());
		Result result = pusher.trigger("announcements", "notification", dto);
		if (result.getStatus() != Result.Status.SUCCESS) {
			System.err.println("Failed to send announcement: " + result.getMessage());
		}
	}

	/**
	 * Send multiple types of notifications based on target
	 * 
	 * @param target The target (username, role, or "announcement")
	 * @param dto    The notification DTO with type (TASK, DEADLINE, ACHIEVEMENT,
	 *               SYSTEM, etc.)
	 */
	public void sendNotification(String target, NotificationDto dto) {
		if (target == null || dto == null) {
			throw new IllegalArgumentException("Target and DTO must not be null");
		}

		if (target.equals("announcement")) {
			sendAnnouncement(dto);
		} else if (target.startsWith("ROLE_")) {
			sendToRole(target.replace("ROLE_", ""), dto);
		} else {
			sendToUser(target, dto);
		}
	}
}