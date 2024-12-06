package com.healthtraze.etraze.api.security.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.UserSession;




@Repository
public interface UserSessionRepository extends  BaseRepository<UserSession, String> {
	

	public List<UserSession> findByUserId(String userId);
	
	
	public List<UserSession> findByUserIdAndStatus(String userId, String status);


	public UserSession findBySessionId(String sessionId);

	
	
}
