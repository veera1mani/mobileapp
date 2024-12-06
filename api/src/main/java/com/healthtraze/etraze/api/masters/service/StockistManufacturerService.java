package com.healthtraze.etraze.api.masters.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.StockistManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class StockistManufacturerService implements BaseService<StockistManufacture, Long> {

	private Logger logger = LogManager.getLogger(StockistManufacturerService.class);

	private final StockistManufactureRepository stockistManufactureRepository;

	private final StockistRepository stockistRepository;

	private final UserRepository userRepository;

	private final ManufacturerRepository manufacturerRepository;

	@Autowired
	public StockistManufacturerService(StockistManufactureRepository stockistManufactureRepository,
			StockistRepository stockistRepository, UserRepository userRepository,
			ManufacturerRepository manufacturerRepository) {

		this.stockistManufactureRepository = stockistManufactureRepository;
		this.stockistRepository = stockistRepository;
		this.userRepository = userRepository;
		this.manufacturerRepository = manufacturerRepository;

	}

	@Override
	public List<StockistManufacture> findAll() {
		
		return new ArrayList<>();
	}

	@Override
	public StockistManufacture findById(Long id) {
		
		return null;
	}

	@Override
	public Result<StockistManufacture> create(StockistManufacture t) {

		return null;
	}

	@Override
	public Result<StockistManufacture> update(StockistManufacture t) {
		return null;
	}

	@Override
	public Result<StockistManufacture> delete(Long id) {
		Result<StockistManufacture> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User uu = user.get();
				stockistManufactureRepository.deletestkmapping(id, uu.getTenantId());
				result.setMessage("deleted");
				result.setCode("0000");
			}
	
			
		
			
		} catch (Exception e) {
			
		logger.error(e);
		}
		return result;
	}

	public List<Manufacturer> getAllManufacturerByStockist() {
		try {

			Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
			if(user.isPresent()) {
				User uu=user.get();
				Stockist stockist = stockistRepository.findByUserId(uu.getUserId(),uu.getTenantId());
				List<StockistManufacture> list = stockistManufactureRepository.findListByStockist(stockist.getStockistId(),uu.getTenantId());
				
				List<String> manfList=list.stream().map(li-> li.getManufacture()).collect(Collectors.toList());
				
				
				
				
				return manufacturerRepository.findManufacturersList(manfList);

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return Collections.emptyList();
	}

	public List<Manufacturer> getAllManufacturerByStockistId(String id) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();
				List<StockistManufacture> sm = stockistManufactureRepository.findListByStockist(id, us.getTenantId());
				List<String> manfList = sm.stream().map(li -> li.getManufacture()).collect(Collectors.toList());
				return manufacturerRepository.findManufacturersList(manfList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}
	public List<String> getAllManufacturer(String userId,List<String> list) {
		try {
				return stockistManufactureRepository.findAllStockistMan(userId, list);	
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}


}
