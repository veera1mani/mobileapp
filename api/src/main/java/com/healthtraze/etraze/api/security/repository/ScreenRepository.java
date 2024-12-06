package com.healthtraze.etraze.api.security.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.Screen;


@Repository
public interface ScreenRepository extends  BaseRepository<Screen, String> {

		
}
