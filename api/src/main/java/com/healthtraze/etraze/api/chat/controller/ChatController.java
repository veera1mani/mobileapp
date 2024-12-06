package com.healthtraze.etraze.api.chat.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.chat.model.Chat;
import com.healthtraze.etraze.api.chat.service.ChatService;


@CrossOrigin
@RestController
public class ChatController  {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private final ChatService chatService;

	private final SimpMessagingTemplate template;


    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate template) {
		this.chatService = chatService;
		this.template = template;
	}



	@MessageMapping("/send/message")
	public Chat sendMessage(String c) {
		ObjectMapper objectMapper = new ObjectMapper();
		Chat chat;
		try {

			chat = objectMapper.readValue(c, Chat.class);
			Chat cc = chatService.sendMessage(chat);
			this.template.convertAndSend("/message/success/"+chat.getSender(),  objectMapper.writeValueAsString(cc));
	        this.template.convertAndSend("/message/"+chat.getReceiver(),  objectMapper.writeValueAsString(cc));
	        this.template.convertAndSend("/message/"+chat.getGroupId(),  objectMapper.writeValueAsString(cc));

	        return cc;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}


	@PostMapping(value = "/rest/api/v1/chat/history") 
	public List<Chat> chatHistory(@RequestBody Chat c) {
		return chatService.getChatHistory(c);
	}



	@GetMapping("/getReceiver")
	public List<Chat> getByReceiverId() {
		return chatService.getByReceiverId();
	}

	@GetMapping("/chat-history")
	public List<Chat> getChatHistory() {
		return chatService.getAllChat();
	}	

	@PostMapping("/getSender")
	public List<Chat> getBySenderId(@RequestBody Chat id) {
		return chatService.getBySenderId(id);

	}

	@GetMapping("/getAllChat")
	public List<Chat> getAllChat() {
		return chatService.getAllChats();
	}
}