package com.taskManagement.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageReq {

	private Integer chatId;
	private String content;
	private Long userId;
}
