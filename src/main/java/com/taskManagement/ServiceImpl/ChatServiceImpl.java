package com.taskManagement.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskManagement.Entitys.Chat;
import com.taskManagement.Entitys.User;
import com.taskManagement.Repository.ChatRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.ChatService;
import com.taskManagement.Service.UserServices;
import com.taskManagement.exception.ChatException;
import com.taskManagement.exception.UserException;
import com.taskManagement.request.GroupChatReq;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private UserServices userService;
	@Autowired
	private UserRepo userRepo;

	@Override
	public Chat createChat(User reqUser, Long userId2) throws UserException {

		User user2 = userRepo.findById(userId2)
				.orElseThrow(() -> new UserException("User not found with this userId" + userId2));

		Chat isChatExist = chatRepository.findSingleChatByUserIds(user2, reqUser);

		if (isChatExist != null) {
			return isChatExist;
		}

		Chat chat = new Chat();
		chat.setCreatedBy(reqUser);
		chat.getUsers().add(user2);
		chat.getUsers().add(reqUser);
		chat.setGroup(false);
		chatRepository.save(chat);

		return chat;

	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		Optional<Chat> chat = chatRepository.findById(chatId);

		if (chat.isPresent()) {
			return chat.get();
		}

		throw new ChatException("No User found with with id :: " + chatId);
	}

	@Override
	public List<Chat> findAllChatByUserId(Long userId) throws UserException {

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User not found with this userId" + userId));

		List<Chat> chats = chatRepository.findChatByUserId(user.getUserId());

		return chats;
	}

	@Override
	public Chat createGroup(GroupChatReq req, User reqUser) throws UserException {

		Chat group = new Chat();
		group.setGroup(true);
		group.setChatImg(req.getChatImage());
		group.setChatName(req.getChatName());
		group.setCreatedBy(reqUser);
		group.getAdmins().add(reqUser);
		// get member 1 by 1 and add it into grp
		for (Long userId : req.getUserIds()) {
			User usersToAddGroup = userRepo.findById(userId)
					.orElseThrow(() -> new UserException("User not found with this id" + userId));

			group.getUsers().add(usersToAddGroup);
		}

		System.out.println("Senidng data into database final checkout: " + group);

		Chat savedChats = chatRepository.save(group);

		System.out.println("Data Saved to DB " + savedChats);
		return savedChats;
	}

	@Override
	public Chat addUserToGroup(Long userId, Integer chatId, User reqUser) throws UserException, ChatException {
		Optional<Chat> group = chatRepository.findById(chatId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User not found with this id" + userId));

		if (group.isPresent()) {
			Chat chat = group.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().add(user);
				return chatRepository.save(chat);
			} else {
				throw new UserException("U dont have access to add members in group");
			}
		}

		throw new ChatException("chat not found with id :: " + chatId);
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
		Optional<Chat> group = chatRepository.findById(chatId);
		if (group.isPresent()) {
			Chat chat = group.get();
			if (chat.getUsers().contains(reqUser)) {
				chat.setChatName(groupName);
				chatRepository.save(chat);
			}
			throw new UserException("you are not member of this group");
		}

		throw new ChatException("Group not found with Group ID :: " + chatId);
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Long userId, User reqUser) throws UserException, ChatException {

		Optional<Chat> group = chatRepository.findById(chatId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new UserException("User not found With this user Id" + userId));

		if (group.isPresent()) {
			Chat chat = group.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(user);
				return chatRepository.save(chat);
			} else if (chat.getUsers().contains(reqUser)) {

				if (user.getUserId().equals(reqUser.getUserId())) {
					chat.getUsers().remove(user);
					return chatRepository.save(chat);
				}
			}

			throw new UserException("u Can't remove another user");

		}

		throw new ChatException("chat not found with id :: " + chatId);

	}

	@Override
	public void deleteChat(Integer chatId, Long userId) throws UserException, ChatException {
		Optional<Chat> chat = chatRepository.findById(chatId);

		if (chat.isEmpty()) {
			Chat chatHistory = chat.get();
			chatRepository.deleteById(chatHistory.getId());
		}
	}

}
