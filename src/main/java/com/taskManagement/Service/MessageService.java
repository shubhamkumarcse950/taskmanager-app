package com.taskManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManagement.Entitys.Message;
import com.taskManagement.Entitys.User;
import com.taskManagement.exception.ChatException;
import com.taskManagement.exception.MessageException;
import com.taskManagement.exception.UserException;

@Service
public interface MessageService {

	public com.taskManagement.Entitys.Message sentMessage(com.taskManagement.request.SendMessageReq req)
			throws UserException, ChatException;

	public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;

	public Message findMessageById(Integer messageId) throws MessageException;

	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;

}
