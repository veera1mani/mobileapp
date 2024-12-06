package com.healthtraze.etraze.api.chat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.chat.model.Chat;
import com.healthtraze.etraze.api.chat.repository.ChatRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class ChatService {


	private final ChatRepository chatRepository;

	private final UserRepository userRepository;

	
	@Autowired
	public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
		this.chatRepository = chatRepository;
		this.userRepository = userRepository;
	}

	private Logger logger = LogManager.getLogger(ChatService.class);

	public Chat sendMessage(Chat chat) {

		chat.setChatId(System.currentTimeMillis());
		chat.setCreatedBy(getSenderName(chat.getSender()));
		chat.setSeen(false);
		logger.info(chat);
		return chatRepository.save(chat);

	}

	private String getSenderName(String id) {
		Optional<User> userOption = userRepository.findById(id);

		if (userOption.isPresent()) {
			User u = userOption.get();
			return u.getFirstName() + StringIteration.SPACE + u.getLastName();
		}

		return id;

	}


	public List<Chat> getChatHistory(Chat c) {

		List<String> intList = new ArrayList<>();

		intList.add(c.getSender());

		intList.add(c.getReceiver());


		return Collections.emptyList();

	}

	public List<Chat> getByReceiverId() {
	    Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
	    if (u.isPresent()) {
	        return chatRepository.findByReceiver(u.get().getUserId());
	    } else {
	        return Collections.emptyList();
	    }
	}


	public List<Chat> getAllChat() {
		return chatRepository.findAll();
	}

	public List<Chat> getBySenderId(Chat id) {

		return chatRepository.findBySender(id.getSender());

	}


	public List<Chat> getAllChats() {

		String loginUser = SecurityUtil.getUserName();

		userRepository.findByUserId(loginUser);
		
		
		return new ArrayList<>();
	}
}