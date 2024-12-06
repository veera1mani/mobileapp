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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.model.Visitor;
import com.healthtraze.etraze.api.masters.model.VisitorHistory;
import com.healthtraze.etraze.api.masters.repository.VisitorHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.VisitorRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class VisitorService implements BaseService<Visitor, String> {
	private Logger logger = LogManager.getLogger(VisitorService.class);

	private final VisitorRepository visitorRepository;

	private final UserRepository userRepository;

	private final VisitorHistoryRepository visitorHistoryRepository;
	

	public VisitorService(VisitorRepository visitorRepository, UserRepository userRepository,
			VisitorHistoryRepository visitorHistoryRepository) {
		this.visitorRepository = visitorRepository;
		this.userRepository = userRepository;
		this.visitorHistoryRepository = visitorHistoryRepository;
	}

	@Scheduled(cron = "${visitor.autoCheckOut.time}")
	public void updateAllOutTime() {
		try {
			
			List<Visitor> vs = visitorRepository.findAllNotOut();	
			vs.forEach(this::updateAutoOutTime);
						
			System.err.println("uuu");

		} catch (Exception e) {		
			logger.error(e);
		}
	}

	public Result<Visitor> updateAutoOutTime(Visitor v) {
		Result<Visitor> result = new Result<>();
		try {
			v.setModifiedBy(StringIteration.AUTO_CHECK_OUT);
			v.setModifiedOn(new Date());
			v.setOutTime(new Date());
			visitorRepository.save(v);
			Optional<VisitorHistory> optional = visitorHistoryRepository.getByPhoneAndIntime(v.getPhone(),
					v.getInTime());
			if (optional.isPresent()) {
				VisitorHistory his = optional.get();
				his.setOutTime(v.getOutTime());
				his.setModifiedBy(StringIteration.AUTO_CHECK_OUT);
				his.setModifiedOn(new Date());
				visitorHistoryRepository.save(his);
			}

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	@Override
	public List<Visitor> findAll() {
		try {
			return visitorRepository.findAll();

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	@Override
	public Visitor findById(String id) {
		try {
			Optional<Visitor> optional = visitorRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	public Visitor findByPhoneNumber(String phoneNumber) {
		try {
			Optional<Visitor> optional = visitorRepository.findByPhone(phoneNumber);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	public List<Visitor> findAllVisitors(){
		List<Visitor> list = new ArrayList<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				return visitorRepository.findByTenantAndToday(u.getTenantId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	public Result<Object> getVisitors(String search, int page, int mon) {
		Result<Object> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				String sortBy = "name";
				String sortDir = StringIteration.ASC;
				Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
				Page<Visitor> page1;
				if (mon == 0) {
					page1 = visitorRepository.findByTenantAndSearch(search, u.getTenantId(), pageable);
				} else {
					page1 = visitorRepository.findByTenantMonthSearch(search, u.getTenantId(), mon, pageable);
				}
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(page1);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	@Override
	public Result<Visitor> create(Visitor t) {
		Result<Visitor> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				t.setTenantId(u.getTenantId());
				t.setId("VST_" + System.currentTimeMillis());
				CommonUtil.setCreatedOn(t);
				Optional<Visitor> optional = visitorRepository.findByPhone(t.getPhone());
				if (optional.isPresent()) {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Mobile number already exists");
					return result;
				}
				t.setStatus(StringIteration.CHECK_IN);
				visitorRepository.save(t);
				saveHistory(t);
				result.setCode("0000");
				result.setMessage("Successful Created");
				result.setData(t);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}
	

	@Override
	public Result<Visitor> update(Visitor t) {
		Result<Visitor> result = new Result<>();
		try {
			Optional<Visitor> list = visitorRepository.findById(t.getId());
			if (list.isPresent()) {
				Visitor v = list.get();
				CommonUtil.setModifiedOn(v);
				v.setInTime(t.getInTime());
				if (t.getInTime() != null && t.getOutTime() != null && t.getInTime().after(t.getOutTime())) {
					v.setOutTime(null);
					v.setStatus(StringIteration.CHECK_IN);
					Optional<Visitor> visitor = visitorRepository.findByPhone(t.getPhone());
					if (visitor.isPresent() && !visitor.get().getId().equals(v.getId())) {
						result.setCode(StringIteration.ERROR_CODE1);
						result.setMessage("phone number already exists");
						return result;
					} else {
						v.setPhone(t.getPhone());
					}
				} else {
					v.setOutTime(t.getOutTime());
					v.setStatus(StringIteration.CHECK_OUT);
				}
				v.setMeetTo(t.getMeetTo());
				v.setPurpose(t.getPurpose());
				v.setName(t.getName());
				visitorRepository.save(v);
				if (v.getOutTime() != null) {
					Optional<VisitorHistory> optional = visitorHistoryRepository.getByPhoneAndIntime(t.getPhone(),
							t.getInTime());
					if (optional.isPresent()) {
						VisitorHistory his = optional.get();
						his.setOutTime(t.getOutTime());
						CommonUtil.setModifiedOn(his);
						his.setStatus(StringIteration.CHECK_OUT);
						visitorHistoryRepository.save(his);
					}
				} else {
					saveHistory(v);
				}

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
				result.setData(t);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}
	
	private Visitor saveHistory(Visitor v) {
		VisitorHistory his = new VisitorHistory();
		CommonUtil.setCreatedOn(his);
		his.setId(System.currentTimeMillis() + " ");
		his.setName(v.getName());
		his.setPhone(v.getPhone());
		his.setMeetTo(v.getMeetTo());
		his.setPurpose(v.getPurpose());
		his.setInTime(v.getInTime());
		his.setTenantId(v.getTenantId());
		his.setMonth(v.getInTime().getMonth() + 1);
		his.setVisitorId(v.getId());
		his.setCompany(v.getCompany());
		his.setStatus(StringIteration.CHECK_IN);
		visitorHistoryRepository.save(his);
		return v;
	}

	@Override
	public Result<Visitor> delete(String id) {
		Result<Visitor> result = new Result<>();
		try {
			Optional<Visitor> optional = visitorRepository.findById(id);
			if (optional.isPresent()) {
				visitorRepository.deleteById(optional.get().getId());
				result.setCode("0000");
				result.setMessage("Successful deleted");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

}
