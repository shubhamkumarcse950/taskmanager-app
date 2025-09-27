package com.taskManagement.Entitys;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {

	private List<Long> userIds;
	private String chatName;
	private String chatImage;

}
