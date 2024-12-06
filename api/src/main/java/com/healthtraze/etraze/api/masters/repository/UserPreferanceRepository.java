package com.healthtraze.etraze.api.masters.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.UserPreference;


@Repository
public interface UserPreferanceRepository extends BaseRepository<UserPreference, String>{

	UserPreference findByUserId(String userId);

}
