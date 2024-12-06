package com.healthtraze.etraze.api.masters.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.masters.model.ManufacturerReportMapping;
import com.healthtraze.etraze.api.masters.projections.ManufactureReport;
import com.healthtraze.etraze.api.masters.repository.ManufacturerReportMappingRepository;

@Service
public class ManufacturerReportMappingService implements BaseService<ManufacturerReportMapping, String> {

	Logger logger = LogManager.getLogger(ManufacturerReportMapping.class);

	private final ManufacturerReportMappingRepository manufacturerReportMappingRepository;

	@Autowired
	public ManufacturerReportMappingService(ManufacturerReportMappingRepository manufacturerReportMappingRepository) {
		super();
		this.manufacturerReportMappingRepository = manufacturerReportMappingRepository;
	}

	
	public List<List<ManufacturerReportMapping>> findAll(String manufacturetId) {
		try {
		
			List<List<ManufacturerReportMapping>> oi = new ArrayList<>();
			
			List<ManufacturerReportMapping>  ob=manufacturerReportMappingRepository.findAllmammm(manufacturetId);
			
			
			Map<String, List<ManufacturerReportMapping>> list = ob.stream().collect(Collectors
					.groupingBy(ManufacturerReportMapping::getValue, LinkedHashMap::new, Collectors.toList()));	
			 oi.addAll(list.values());
			 return oi; 
		}catch(Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}

	
	public List<ManufacturerReportMapping> findAll() {
		try {
			return manufacturerReportMappingRepository.findAll();
		}catch(Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public ManufacturerReportMapping findById(String id) {
		try {
		Optional<ManufacturerReportMapping> map=manufacturerReportMappingRepository.findById(id);
		ManufacturerReportMapping manufacturerReportMapping=map.get();
		return manufacturerReportMapping;
		}catch(Exception e) {
			logger.error(e);
			
		}
		return null;
	}

	@Override
	public Result<ManufacturerReportMapping> create(ManufacturerReportMapping t) {
		Result<ManufacturerReportMapping> result = new Result<>();
		try {
			
			t.setManufacturerReportMappingId(System.currentTimeMillis()+"");
			t.setKey("398");			
			ManufacturerReportMapping mapping = manufacturerReportMappingRepository.save(t);
			result.setData(mapping);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESS_MESSAGE);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_CREATE);
		}
		return result;
	}

	@Override
	public Result<ManufacturerReportMapping> update(ManufacturerReportMapping t) {
		Result<ManufacturerReportMapping> result = new Result<>();
		try {
			Optional<ManufacturerReportMapping> ts=manufacturerReportMappingRepository.findById(t.getManufacturerReportMappingId());
			if(ts.isPresent()) {
				ManufacturerReportMapping mapping =ts.get();
				mapping.setReportName(t.getReportName());
				mapping.setCode(t.getCode());
				mapping.setValue(t.getValue());
				manufacturerReportMappingRepository.save(mapping);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESS_MESSAGE);	
			}
		} catch (Exception e) {
			logger.error(e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_UPDATE);
		}
		return result;
	}

	@Override
	public Result<ManufacturerReportMapping> delete(String id) {
		Result<ManufacturerReportMapping> result=new Result<>();
		manufacturerReportMappingRepository.deleteById(id);
		result.setCode("0000");
		result.setMessage(StringIteration.DELETEDSUCCESSFULLY);
		
		return result;
	}


	public Result<ManufacturerReportMapping> delete(ManufacturerReportMapping t) {
		
	   try {	
		   Result<ManufacturerReportMapping> map=new Result<>();
		   Optional<ManufacturerReportMapping> optionalMapping = manufacturerReportMappingRepository.findById(t.getManufacturerReportMappingId());

		   if (optionalMapping.isPresent()) {
		       ManufacturerReportMapping mapping = optionalMapping.get();
		       manufacturerReportMappingRepository.deleteById(mapping.getManufacturerReportMappingId());
		   }
		   map.setCode("0000");
		   map.setMessage(StringIteration.DELETEDSUCCESSFULLY);
		   return map;
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
		
		return null;
	}


}
