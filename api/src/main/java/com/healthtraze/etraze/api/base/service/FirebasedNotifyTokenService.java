package com.healthtraze.etraze.api.base.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.model.FirebasedNotifyToken;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.repository.FirebasedNotifyTokenRepository;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import java.util.Collections;

@Service
public class FirebasedNotifyTokenService implements BaseService<FirebasedNotifyToken, Long> {
	private Logger logger = LogManager.getLogger(FirebasedNotifyTokenService.class);
	
	
	private final FirebasedNotifyTokenRepository firebasedNotifyTokenRepository;
	
	

	public FirebasedNotifyTokenService(FirebasedNotifyTokenRepository firebasedNotifyTokenRepository) {
		this.firebasedNotifyTokenRepository = firebasedNotifyTokenRepository;
	}

	@Override
	public List<FirebasedNotifyToken> findAll() {

		return Collections.emptyList();
	}

	@Override
	public FirebasedNotifyToken findById(Long id) {

		return null;
	}

	@Override
	public Result<FirebasedNotifyToken> create(FirebasedNotifyToken t) {
		Result<FirebasedNotifyToken> result = new Result<>();
		try {
			t.setId(System.currentTimeMillis());
			CommonUtil.setCreatedOn(t);
        	firebasedNotifyTokenRepository.save(t);
			result.setCode("0000");
			result.setMessage("successfully created");
			result.setData(t);
			
		} catch (Exception e) {
			result.setCode("1111");
			logger.info("", e);
		}
		return result;
	}

	@Override
	public Result<FirebasedNotifyToken> update(FirebasedNotifyToken t) {

		return null;
	}

	@Override
	public Result<FirebasedNotifyToken> delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
