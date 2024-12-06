package com.healthtraze.etraze.api.masters.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.documents.DateFormat;
import com.healthtraze.etraze.api.masters.repository.DateFormatRepository;


@Service
public class DateFormatService implements BaseService<DateFormat, String>{

	
	private final DateFormatRepository repository;
	
	public DateFormatService(DateFormatRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<DateFormat> findAll() {
		return repository.findAll();
	}

	@Override
	public DateFormat findById(String id) {
		Optional<DateFormat> optional=repository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}

	@Override
	public Result<DateFormat> create(DateFormat t) {
		
		Result<DateFormat>  result=new Result<>();
		
		t.setDateFormatId(System.currentTimeMillis()+"");
		CommonUtil.setCreatedOn(t);
		repository.save(t);
		result.setCode("0000");
		result.setMessage("created");
		result.setData(t);
		
		return result;
	}

	@Override
	public Result<DateFormat> update(DateFormat t) {

		Result<DateFormat> result=new Result<>();
		Optional<DateFormat> optional=repository.findById(t.getDateFormatId());
		
		if(optional.isPresent()) {
			DateFormat df=optional.get();
			df.setDateFormat(t.getDateFormat());
			df.setDateOrder(t.getDateOrder());
			repository.save(df);
			result.setCode("0000");
			result.setMessage("updated");
			result.setData(df);
			return result;
		}
		
		return null;
	}

	@Override
	public Result<DateFormat> delete(String id) {

		
		return null;
	}

}
