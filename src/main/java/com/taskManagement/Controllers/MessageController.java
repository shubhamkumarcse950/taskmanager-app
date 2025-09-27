package com.taskManagement.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Entitys.Message;
import com.taskManagement.Entitys.User;
import com.taskManagement.Service.UserServices;
import com.taskManagement.exception.ChatException;
import com.taskManagement.exception.MessageException;
import com.taskManagement.exception.UserException;
import com.taskManagement.request.ApiResponse;
import com.taskManagement.request.SendMessageReq;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private com.taskManagement.Service.MessageService messageService;
	@Autowired
	private UserServices userService;

	@PostMapping("/create")
	public ResponseEntity<Message> sentMessageHandler(@RequestBody SendMessageReq req,
			@RequestHeader("Authorization") String jwt) throws UserException, ChatException {

		User user = userService.findUserByProfile(jwt);
		req.setUserId(user.getUserId());
		Message sentMessage = messageService.sentMessage(req);
		System.out.println("Message sent successfully from" + user.getUserId());

		return new ResponseEntity<Message>(sentMessage, HttpStatus.OK);

	}

	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<Message>> GetChatMessagesHandler(@PathVariable Integer chatId,
			@RequestHeader("Authorization") String jwt) throws UserException, ChatException {

		User user = userService.findUserByProfile(jwt);

		List<Message> messages = messageService.getChatsMessages(chatId, user);

		return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);

	}

	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse> deleteMessagesHandler(@PathVariable Integer messageId,
			@RequestHeader("Authorization") String jwt) throws UserException, MessageException {

		User user = userService.findUserByProfile(jwt);

		messageService.deleteMessage(messageId, user);

		ApiResponse apiResponse = new ApiResponse("Message deteted succesfuly -----", true);

		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);

	}

}
