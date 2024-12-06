package com.healthtraze.etraze.api.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.chat.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

	List<Chat> findAll();


	List<Chat> findBySender(String sender);

	
	public List<Chat> findByReceiver(String receiver);
	
}
