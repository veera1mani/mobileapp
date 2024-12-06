package com.healthtraze.etraze.api.masters.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.dto.BlockEmailDTO;
import com.healthtraze.etraze.api.masters.model.BlockEmail;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.repository.BlockEmailRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class BlockEmailService implements BaseService<BlockEmail,String> {
	
	Logger logger = LogManager.getLogger(BlockEmailService.class);
	
	private final BlockEmailRepository blockEmailRepository;
	
	private final TenantManufactureRepository tenantManufactureRepository;
	
	private final UserRepository userRepository;
	
	

	public BlockEmailService(BlockEmailRepository blockEmailRepository,
			TenantManufactureRepository tenantManufactureRepository, UserRepository userRepository) {
		this.blockEmailRepository = blockEmailRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<BlockEmail> findAll() {
		return Collections.emptyList();
	}
	
	public List<BlockEmailDTO> findAllByTenant() {
		List<BlockEmailDTO> list = new ArrayList<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				List<Object[]>ob = blockEmailRepository.findAllBlockEmailByManufacturer(u.getTenantId());
				
				for(Object[] b : ob) {
					BlockEmailDTO dto = new BlockEmailDTO();
					
					dto.setBlockType(String.valueOf(b[0]));
					dto.setBlockValue(String.valueOf(b[1]));
					dto.setManufacturerName(String.valueOf(b[2]));
					dto.setId(String.valueOf(b[3]));
					list.add(dto);
				}
				return list;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			}
		return Collections.emptyList();
	}

	@Override
	public BlockEmail findById(String id) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				Optional<BlockEmail> optional = blockEmailRepository.findById(id);
				if(optional.isPresent()) {
					return optional.get();
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Result<BlockEmail> create(BlockEmail t) {
		Result<BlockEmail> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				t.setId(System.currentTimeMillis()+"");
				t.setTenantId(u.getTenantId());
				 CommonUtil.setCreatedOn(t);
				 Optional<TenantManufacture> tm = tenantManufactureRepository.findByManufactureId(t.getManufacturerId(),u.getTenantId());
				if(tm.isPresent()) {
					 t.setTenantManufacturerId(tm.get().getId());
				}
				blockEmailRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESS_MESSAGE);
			}
			
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.MESSAGEREGERROR);
			logger.error(e.getMessage());
		}
		return result;
	}
	

	@Override
	public Result<BlockEmail> update(BlockEmail t) {
		return null;
	}

	@Override
	public Result<BlockEmail> delete(String id) {
		Result<BlockEmail> result = new Result<>();
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		if(us.isPresent()) {
			User u = us.get();		
			Optional<BlockEmail> blockEmail = blockEmailRepository.findByIdAndTenantId(id,u.getTenantId());
			if(blockEmail.isPresent()) {
				blockEmailRepository.delete(blockEmail.get());
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("Un Blocked");
			}
			else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Invalid Data");
			}
		}else {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("Invalid User");
		}
		
		return result;
	}

}
