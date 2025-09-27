package com.taskManagement.websoket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.NotificationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSoketNotificationService {

	private final SimpMessagingTemplate template;

	public void sendToUser(String username, NotificationDto dto) {
		template.convertAndSendToUser(username, "/queue/notifications", dto);
	}

	public void sendToRole(String role, NotificationDto dto) {
		template.convertAndSend("/group/role-" + role, dto);
	}
}
