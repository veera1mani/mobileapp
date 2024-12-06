package com.healthtraze.etraze.api.masters.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class ManufacturerService implements BaseService<Manufacturer, String> {
	
	private Logger logger = LogManager.getLogger(ManufacturerService.class);

	private final ManufacturerRepository manufacturerRepository;

	private final UserRepository repository;
	private final UserService userService;
	
	private final TenantManufactureRepository tenantManufactureRepository;
	
	
	public ManufacturerService(ManufacturerRepository manufacturerRepository, UserRepository repository,
			 TenantManufactureRepository tenantManufactureRepository,UserService userService) {
		this.manufacturerRepository = manufacturerRepository;
		this.repository = repository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.userService=userService;
		
	}


	private static final String PREFIX = "MF";
    private static final int RANGE_LIMIT = 10000;
    private int counter = 1;
	
	@Override
	public List<Manufacturer> findAll() {
		
		return manufacturerRepository.findManufactureByTenant();
	}
	
	
	public List<Manufacturer> findManufactureByTenant() {
		try {
			
			Optional<User> us = repository.findById(SecurityUtil.getUserName());
			
			if(us.isPresent()) {
				return manufacturerRepository.findManufactureByTenant(us.get().getTenantId());
			}
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}
	
	
	@Transactional
	public Result<HashMap<String, Object>> findAllManufacture(int page, int limit, String value, String sortBy,
			String sortDir) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
						
						HashMap<String, Object> map = new HashMap<>();
						int size = 10;
						long totalCount=0;
			
			
			if (StringUtils.isNullOrEmpty(sortBy)) {
				sortBy = "manufacturer_name";
			}
			if (StringUtils.isNullOrEmpty(sortDir)) {
				sortDir = "ASC";
			}

			 Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
			List<Manufacturer> manufacturer= manufacturerRepository.findManufactureByCount(value,paging);
			
			
			if(value.isEmpty()) {
				 totalCount = manufacturerRepository.findManufactureByCount(value).size();
			}
			if(!value.isEmpty()) {
				 totalCount = manufacturerRepository.findManufactureByCount(value).size();
			}
						
			map.put("manufacturer", manufacturer);
            map.put("totalCount", totalCount);
			result.setCode("0000");
			result.setMessage("successfully");
			result.setData(map);
			return result;

		} catch (Exception ex) {

			logger.error(ex);
			result.setCode("1111");
			
		}
		return result;
	}

	
	
	
public Result<Manufacturer> activeAndDeActivateManuFacture(String t) {
	Result<Manufacturer> result = new Result<>();
	try {
		Optional<Manufacturer> list = manufacturerRepository.findById(t);
		if (list.isPresent()) {
		Manufacturer m = list.get();
			if(m.getStatus()!=null) {
				if(m.getStatus().equals(StringIteration.ACTIVE_STATUS)){
					m.setStatus("DEACTIVE");
				
				}
				else {
					m.setStatus("ACTIVE");
				}
			}
			else {
				m.setStatus("DEACTIVE");
			}
			CommonUtil.setModifiedOn(m);
			manufacturerRepository.save(m);
			result.setCode("0000");
			result.setMessage("Updated Successfully");
			
		}else {
			result.setCode("1111");
			result.setMessage("There is no modification done here");
		}
	} catch (Exception e) {
		result.setCode("1111");
		logger.error("", e);
	}
	return result;
	
	
}
	@Override
	public Manufacturer findById(String id) {
		try {
			Optional<Manufacturer> optional = manufacturerRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	@Transactional
	@Override
	public Result<Manufacturer> create(Manufacturer t) {
		Result<Manufacturer> result = new Result<>();
		try {
			Optional<User> op = repository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				List<Manufacturer> list = manufacturerRepository.findByManufacturerNameAndTenantId(t.getManufacturerName(),us.getTenantId());

				if (list.isEmpty()) {
//					t.setTenantId(user.getTenantId());
					
					Integer lastSequence = manufacturerRepository.getLastSequence();
					if (counter >= RANGE_LIMIT) {
			            throw new IllegalStateException("Sequence limit reached");
			        }
					if(lastSequence != null){
			        String sequenceNumber = String.format("%s%05d", PREFIX, lastSequence+1);
			        t.setManufacturerId(sequenceNumber);
					}
					else {
					String sequenceNumber = String.format("%s%05d", PREFIX, counter++);
					t.setManufacturerId(sequenceNumber);
					}
					
					User user = new User();
					user.setFirstName(t.getManufacturerName());
                     user.setUserId(t.getManufacturerId());
					user.setEmail(t.getEmail());
					user.setPhoneNo(t.getMobile());
					user.setIsUserOnboarded(true);
					user.setNewUserValidateWeb(true);
					user.setOtpVerified(true);
					user.setRoleId("7");
				     userService.signUp(user);

					t.setStatus("ACTIVE");
					CommonUtil.setCreatedOn(t);
					manufacturerRepository.save(t);
					
					result.setCode("0000");
					result.setMessage("Successful Created");
					result.setData(t);
				} else {
					result.setCode("1111");
					result.setMessage("Already exist");
				}
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	@Override
	public Result<Manufacturer> update(Manufacturer t) {
		Result<Manufacturer> result = new Result<>();
		try {
				Optional<Manufacturer> option=manufacturerRepository.findById(t.getManufacturerId());
				if(option.isPresent()) {
					Manufacturer  manufacturer=option.get();
					manufacturer.setManufacturerName(t.getManufacturerName());
					manufacturer.setShortName(t.getShortName());				
		              manufacturerRepository.save(manufacturer);
		              result.setCode("0000");
		              result.setData(manufacturer);
		              result.setMessage("successs");
				}
				    
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	@Override
	public Result<Manufacturer> delete(String id) {
		Result<Manufacturer> result = new Result<>();
		try {
			Optional<Manufacturer> optional = manufacturerRepository.findById(id);
			if (optional.isPresent()) {
				manufacturerRepository.deleteById(optional.get().getManufacturerId());
				result.setCode("0000");
				result.setMessage("Successful deleted");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}
	

	public Result<TenantManufacture> updateActions(TenantManufacture t){
		
		Result<TenantManufacture> result=new Result<>();
		Optional<TenantManufacture> man=tenantManufactureRepository.findById(t.getId());
		if(man.isPresent()) {
			man.get().setStatus(t.getStatus());
			tenantManufactureRepository.save(t);
		}
		return result;
		
	}
	

}
