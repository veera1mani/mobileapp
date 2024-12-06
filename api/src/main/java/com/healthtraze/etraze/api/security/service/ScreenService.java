package com.healthtraze.etraze.api.security.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.masters.dto.UserRoleServiceDTO;
import com.healthtraze.etraze.api.masters.repository.UserRoleServiceRepository;
import com.healthtraze.etraze.api.security.model.Screen;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.ScreenRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;




@Component
public class ScreenService implements BaseService<Screen, String> {

	private Logger logger = LogManager.getLogger(ScreenService.class);

	private final  ScreenRepository screenRepository;
	private final UserRoleServiceRepository userRoleServiceRepository;
	private final UserRepository userRepository;
	
	@Autowired(required=true)
	public ScreenService(ScreenRepository screenRepository,UserRoleServiceRepository userRoleServiceRepository,UserRepository userRepository) {
		this.screenRepository=screenRepository;
		this.userRoleServiceRepository = userRoleServiceRepository;
		this.userRepository=userRepository;
	}
	
	
	@Override
	public List<Screen> findAll() {
		try {
			return  screenRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	
	
	@Override
	public Screen findById(String id) {
		try {
			Optional<Screen> option=screenRepository.findById(id);
			if(option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	
	
	@Override
	public Result<Screen> create(Screen t) {
		Result<Screen> result = new Result<>();
		try {
			t.setCreatedOn(new Date());
			t.setScreenId(System.currentTimeMillis()+"");
			Screen s = screenRepository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYCREATED);
			result.setData(s);
			return  result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}
	
	

	@Override
	public Result<Screen> update(Screen t) {
		Result<Screen> result = new Result<>();
		try {
			Screen s = screenRepository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			result.setData(s);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}
	

	@Override
	public Result<Screen> delete(String id) {
		Result<Screen> result=new Result<>();
		try {
		      Optional<Screen> tr=screenRepository.findById(id);
				if(tr.isPresent()) {
					screenRepository.delete(tr.get());
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(id +StringIteration.DELETEDSUCCESSFULLY);
					return result; 
				}
			} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}



	public Stream<Screen> findAllDetails() {

		try {
			List<Screen> list= screenRepository.findAll();
			
			return list.stream()
				     .sorted(Comparator.comparing(Screen::getCreatedOn, Comparator.reverseOrder()));
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		
		return null;
	}

	public List<UserRoleServiceDTO> findScreenForUser(String id) {
		
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				List<Object[]> ob=userRoleServiceRepository.findByUserId(id,us.get().getTenantId());
				List<UserRoleServiceDTO> urs = new ArrayList<>();
	            for(Object[] b : ob) {
	            	UserRoleServiceDTO u = new UserRoleServiceDTO();
	            	u.setId(String.valueOf(b[0]));
	            	u.setCreatedBy(String.valueOf(b[1]));
	            	u.setCreatedOn(String.valueOf(b[2]));
	            	u.setModifiedBy(String.valueOf(b[3]));
	            	u.setModifiedOn(String.valueOf(b[4]));
	            	u.setTenantId(String.valueOf(b[5]));
	            	u.setVersionNo(String.valueOf(b[6]));
	            	u.setRoleId(String.valueOf(b[7]));
	            	u.setScreenId(String.valueOf(b[8]));
	            	u.setScreenName(String.valueOf(b[9]));
	            	u.setUserId(String.valueOf(b[10]));
	            	u.setPath(String.valueOf(b[11]));
	            	u.setScreenOrder(String.valueOf(b[12]));
	            	u.setIcon(String.valueOf(b[13]));
	            	u.setType(String.valueOf(b[14]));
	            	urs.add(u);
	      	
	      	            }
	            return urs;
			}
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}



		
		
		
	
	
	
	
}
