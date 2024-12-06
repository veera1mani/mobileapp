package com.healthtraze.etraze.api.security.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.UserAuth;



@Repository
public interface UserAuthRepository extends  BaseRepository<UserAuth, String> {
	
	
	public Optional<UserAuth> findByUserId(String userId);
		
	public void deleteByUserId(String userId);
	
}
