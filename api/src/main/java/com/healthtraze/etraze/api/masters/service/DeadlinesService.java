package com.healthtraze.etraze.api.masters.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.masters.model.Deadlines;
import com.healthtraze.etraze.api.masters.repository.DeadlinesRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class DeadlinesService implements BaseService<Deadlines,Long> {
	

	private DeadlinesRepository deadlinesRepository;
	@Autowired
	public DeadlinesService(DeadlinesRepository deadlinesRepository ) {
		this.deadlinesRepository=deadlinesRepository;
		
		
	}
	
	private Logger logger = LogManager.getLogger(DeadlinesService.class);

	@Override
	public List<Deadlines> findAll() {		
       try {
			return deadlinesRepository.findAll(SecurityUtil.getUserName());
			
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			
			
		}
	return Collections.emptyList();
		
	}
	
	
	public Result<Deadlines> findByTenantId(String tid,String uid) {		
		Result<Deadlines> result = new Result<>();
		try {
			
			Optional<Deadlines> d = deadlinesRepository.findByTenantId(tid,uid);
			if(d.isPresent()) {
				Deadlines dl = d.get();
				result.setData(dl);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESS_MESSAGE);
			}
			else {
				Deadlines dl = new Deadlines();
				result.setData(dl);
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("No Deadlines");
			}
			
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			
		}
		return result;
			
		}
	
	
	
	public List<Deadlines> findByAllTenantId(String tid,String uid) {		
		
		try {
			
			return deadlinesRepository.findByAllTenantId(tid,uid);	
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);

		}
		return Collections.emptyList();
			
		}

	


	public DeadlinesRepository getDeadlinesRepository() {
		return deadlinesRepository;
	}


	public void setDeadlinesRepository(DeadlinesRepository deadlinesRepository) {
		this.deadlinesRepository = deadlinesRepository;
	}


	@Override
	public Deadlines findById(Long id) {
	
		return null;
	}

	@Override
	public Result<Deadlines> create(Deadlines t) {
		Result<Deadlines> result = new Result<>();
		try {
			
			Deadlines d =deadlinesRepository.save(t);
			result.setData(d);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESS_MESSAGE);
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			
		}
		return result;
	}	

	@Override
	public Result<Deadlines> update(Deadlines t) {
		Result<Deadlines> result = new Result<>();
		try {
			
			Optional<Deadlines> dl = deadlinesRepository.findById(t.getDeadlineId());
			
			if(dl.isPresent()) {			
				Deadlines dls = dl.get();
				dls.setDays(t.getDays());
				Deadlines d =deadlinesRepository.save(dls);
				result.setData(d);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESS_MESSAGE);
			}
			
			
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			
		}
		return result;
	}

	@Override
	public Result<Deadlines> delete(Long id) {
		
		return null;
	}

}
