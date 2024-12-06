package com.healthtraze.etraze.api.security.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.PasswordHistoryModel;

@Repository
public interface PasswordHistoryRepository extends BaseRepository<PasswordHistoryModel, String>{

	PasswordHistoryModel findByUserId(String userId);

}
