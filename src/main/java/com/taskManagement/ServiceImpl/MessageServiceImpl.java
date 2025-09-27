package com.taskManagement.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskManagement.Entitys.Chat;
import com.taskManagement.Entitys.Message;
import com.taskManagement.Entitys.User;
import com.taskManagement.Repository.MessageRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.MessageService;
import com.taskManagement.exception.ChatException;
import com.taskManagement.exception.MessageException;
import com.taskManagement.exception.UserException;
import com.taskManagement.request.SendMessageReq;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private com.taskManagement.Service.ChatService chatService;
	@Autowired
	public UserRepo userRepo;

	@Override
	public Message sentMessage(SendMessageReq req) throws UserException, ChatException {

		User user = userRepo.findById(req.getUserId())
				.orElseThrow(() -> new UserException("User not found with this user Id" + req.getUserId()));
		Chat chat = chatService.findChatById(req.getChatId());

		Message message = new Message();
		message.setChat(chat);
		message.setUser(user);
		message.setContent(req.getContent());
		message.setTimestamp(LocalDateTime.now());
		Message message2 = messageRepository.save(message);
		return message2;
	}

	@Override
	public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
		Chat chat = chatService.findChatById(chatId);

		if (!chat.getUsers().contains(reqUser)) {
			throw new UserException("U Cant get this message, U are not related to this chat. :: " + chat.getId());
		}

		List<Message> messages = messageRepository.findByChatId(chat.getId());

		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		Optional<Message> opt = messageRepository.findById(messageId);

		if (opt.isPresent()) {
			return opt.get();
		}
		throw new MessageException(" Message not found with Id :: " + messageId);
	}

	@Override
	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
		Message message = findMessageById(messageId);

		if (message.getUser().getUserId().equals(reqUser.getUserId())) {
			messageRepository.deleteById(messageId);
		}
		throw new UserException("you cant delete another users message  :: " + reqUser.getName());
	}

}
