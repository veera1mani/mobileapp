package com.healthtraze.etraze.api.masters.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import com.healthtraze.etraze.api.masters.model.ListValue;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;

@Component
public class ListValueService implements BaseService<ListValue, String> {

	private Logger logger = LogManager.getLogger(ListValueService.class);

    private final ListValueRepository repository;

	public ListValueService(ListValueRepository repository) {
		this.repository = repository;
	}

	
	@Override
	public List<ListValue> findAll() {
		try {
			return repository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@Override
	public ListValue findById(String id) {
		try {
			Optional<ListValue> v = repository.findById(id);
			if (v.isPresent()) {
				return v.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<ListValue> create(ListValue t) {
		Result<ListValue> result = new Result<>();
		try {

			ListValue h = findById(t.getKey());
			if (h == null) {
				CommonUtil.setCreatedOn(t);
				ListValue c = repository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.LISTVALUECREATEDSUCCESSFULLY);
				result.setData(c);
				return result;
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.KEYALREADYEXIST);
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<ListValue> update(ListValue t) {
		Result<ListValue> result = new Result<>();
		try {
			CommonUtil.setModifiedOn(t);
			ListValue c = repository.save(t);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
			result.setData(c);
			return result;
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<ListValue> delete(String id) {
		Result<ListValue> result = new Result<>();
		try {

			Optional<ListValue> tr = repository.findById(id);

			if (tr.isPresent()) {
				repository.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id + StringIteration.DELETEDSUCCESSFULLY);

				return result;
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}
	
	

	public Result<HashMap<String, Object>> findAllValue(int page, String value, String sortBy, String sortDir) {
		
			  Result<HashMap<String, Object>> result = new Result<>();
			    try {
			        HashMap<String, Object> map = new HashMap<>();
			        int size = 10;
			        long totalCount=0;
			        

			        if (StringUtils.isNullOrEmpty(sortBy)) {
		            sortBy = "description";
			        }
			        if (StringUtils.isNullOrEmpty(sortDir)) {
			            sortDir = "ASC";
			        }

			        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		           
			           List<ListValue> values = repository.findAllValue(value, paging);
			           
			           if(value.isEmpty()) {
			        	   totalCount = repository.findAllCount().size();
			        	   
			           }
			           if(!value.isEmpty()) {
			        	   totalCount = repository.findAllValue(value).size();

			        	   
			           }
			           
			        map.put("totalCount", totalCount);
			        map.put("value ", values);
			        result.setData(map);
			        result.setCode("0000");
			        result.setMessage("success");
			        return result;

			    } catch (Exception ex) {
			    	ex.printStackTrace();
		        logger.error(ex);
			        result.setCode("1111");
			    }
			    return null;	}

}
