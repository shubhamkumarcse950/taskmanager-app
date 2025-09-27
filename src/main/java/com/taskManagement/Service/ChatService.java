package com.taskManagement.Service;

import java.util.List;

import com.taskManagement.Entitys.Chat;
import com.taskManagement.Entitys.User;
import com.taskManagement.exception.ChatException;
import com.taskManagement.exception.UserException;
import com.taskManagement.request.GroupChatReq;

public interface ChatService {

	public Chat createChat(User reqUser, Long userId2) throws UserException;

	public Chat findChatById(Integer chatId) throws ChatException;

	public List<Chat> findAllChatByUserId(Long userId) throws UserException;

	public Chat createGroup(GroupChatReq req, User reqUserId) throws UserException;

	public Chat addUserToGroup(Long userId, Integer chatId, User reqUser) throws UserException, ChatException;

	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException;

	public Chat removeFromGroup(Integer chatId, Long userId, User reqUser) throws UserException, ChatException;

	public void deleteChat(Integer chatId, Long userId) throws UserException, ChatException;
}
