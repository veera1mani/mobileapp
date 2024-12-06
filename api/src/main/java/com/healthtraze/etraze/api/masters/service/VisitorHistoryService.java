package com.healthtraze.etraze.api.masters.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.model.Visitor;
import com.healthtraze.etraze.api.masters.model.VisitorHistory;
import com.healthtraze.etraze.api.masters.repository.VisitorHistoryRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class VisitorHistoryService implements BaseService<VisitorHistory, String>{
	
	private Logger logger = LogManager.getLogger(VisitorHistoryService.class);
	
	private final UserRepository userRepository;
	
	private final VisitorHistoryRepository visitorHistoryRepository;
	
	

	public VisitorHistoryService(UserRepository userRepository, VisitorHistoryRepository visitorHistoryRepository) {
		super();
		this.userRepository = userRepository;
		this.visitorHistoryRepository = visitorHistoryRepository;
	}



	
	public Result<Object> findAll(String id, int mon, int page) {
		Result<Object> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				String sortBy = "in_time";
				String sortDir = StringIteration.DESC;
				Pageable pageable = PageRequest.of(page, 10,Sort.by(Sort.Direction.fromString(sortDir), sortBy));
				Page<VisitorHistory> page1 = visitorHistoryRepository.getAllById(id,mon,pageable);
				result.setData(page1);
				result.setCode(StringIteration.SUCCESS_CODE);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}



	@Override
	public VisitorHistory findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Result<VisitorHistory> create(VisitorHistory t) {
		Result<VisitorHistory> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				t.setTenantId(u.getTenantId());
				t.setId("VST_HIS_"+System.currentTimeMillis());
				t.setVisitorId(t.getId());
				t.setMeetTo(t.getMeetTo());
				t.setPurpose(t.getPurpose());
			    CommonUtil.setCreatedOn(t);
			    
				visitorHistoryRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(t);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return null;
	}



	@Override
	public Result<VisitorHistory> update(VisitorHistory t) {
		Result<VisitorHistory> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				Optional<VisitorHistory> optional = visitorHistoryRepository.findById(t.getId());
				if(optional.isPresent()) {
					VisitorHistory his = optional.get();
					CommonUtil.setModifiedOn(t);
					his.setOutTime(t.getOutTime());
					visitorHistoryRepository.save(his);
				}
			    
				visitorHistoryRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(t);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return null;
	}



	@Override
	public Result<VisitorHistory> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<VisitorHistory> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
