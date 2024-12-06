package com.healthtraze.etraze.api.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.security.model.UserSession;
import com.healthtraze.etraze.api.security.repository.UserSessionRepository;

@Component
public class UserSessionService implements BaseService<UserSession, String> {

	private Logger logger = LogManager.getLogger(UserSessionService.class);


	private final UserSessionRepository repository;
		
	@Autowired(required = true)
		public UserSessionService(UserSessionRepository repository) {
			this.repository = repository;
		}
	

	@Override
	public List<UserSession> findAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	public Long count() {
		try {
			return repository.count();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;

	}

	@Override
	public UserSession findById(String userId) {
		try {
			Optional<UserSession> user=repository.findById(userId);
			if(user.isPresent()) {
				return user.get();
			}
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public List<UserSession> getUserByUserId(String userId) {
		try {
			return repository.findByUserId(userId);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}



	@Override
	public Result<UserSession> create(UserSession t) {
	
		return null;
	}

	
	@Override
	public Result<UserSession> update(UserSession t) {
		return new Result<>();
	}

	@Override
	public Result<UserSession> delete(String userId) {
		Result<UserSession> result = new Result<>();
		try {
		      Optional<UserSession> tr=repository.findById(userId);
				if(tr.isPresent()) {
					repository.delete(tr.get());
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(userId + StringIteration.DELETEDSUCCESSFULLY);
					return result; 
				}
			} catch (Exception e) {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(e.getMessage());
				logger.error(StringIteration.SPACE, e);
			}
		return result;
	}


}
