package com.healthtraze.etraze.api.masters.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.documents.Country;
import com.healthtraze.etraze.api.masters.documents.State;
import com.healthtraze.etraze.api.masters.repository.CountryRepository;
import com.healthtraze.etraze.api.masters.repository.StateRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil; 

@Component
public class StateService implements BaseService<State, String> {

    private Logger logger = LogManager.getLogger(StateService.class);


  
    private final UserRepository repository;
    
    
    private final CountryRepository countryRepository;

    
    private final StateRepository stateRepository;
    
    @Autowired
    public StateService(UserRepository repository, CountryRepository countryRepository,
			StateRepository stateRepository) {
		
		this.repository = repository;
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
	}

	@Override
    public List<State> findAll() {
        try {
            return stateRepository.findAll();
        } catch (Exception e) {
            logger.error(StringIteration.SPACE, e);
        }
        return new ArrayList<>();
    }

    @Override
    public State findById(String id) {
        try {
            Optional<State> option = stateRepository.findById(id);
            if (option.isPresent()) {
                return option.get();
            }

        } catch (Exception e) {
            logger.error(StringIteration.SPACE, e);
        }
        return null;
    }
   
    @Override
    public Result<State> create(State t) {
        Result<State> result = new Result<>();
        try {
            Optional<User> user = repository.findByUserId(SecurityUtil.getUserName());
            if (user.isPresent()) {
            	Optional<Country> cu = countryRepository.findByCountryNameOrCode(t.getCountryCode().replace(" ","").toLowerCase());
            	Optional<State> state = stateRepository.findStateByName(t.getStateName().replace(" ","").toLowerCase(),t.getStateCode().replace(" ","").toLowerCase());
                if (cu.isPresent() && !state.isPresent()) {
                    CommonUtil.setCreatedOn(t);
                 
                    t.setId(System.currentTimeMillis() + "");
                    t.setStatus(StringIteration.ACTIVE);
                    State c = stateRepository.save(t);
                    result.setCode(StringIteration.SUCCESS_CODE);
                    result.setMessage(StringIteration.STATECREATEDSUCCESSFULLY);
                    result.setData(c);
                    return result;
                } else {
                    result.setCode(StringIteration.ERROR_CODE1);
                    result.setMessage(StringIteration.ALREADYEXIST);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error(StringIteration.SPACE, e);
        }
        return null;
    }

    @Override
    public Result<State> update(State t) {
        Result<State> result = new Result<>();
        try {

        

            List<State> state = stateRepository.findByStateNameAndStateCodeAndCountryCode(t.getStateName(),
                    t.getStateCode(), t.getCountryCode());
            if (state.isEmpty()) {
                Optional<State> sta = stateRepository.findById(t.getId());
                if (sta.isPresent()) {
                    t.setModifiedOn(new Date());
                    State st = stateRepository.save(t);
                    result.setCode(StringIteration.SUCCESS_CODE);
                    result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
                    result.setData(st);
                    return result;
                }
            } else {
                result.setCode(StringIteration.ERROR_CODE1);
                result.setMessage("Already Exist");
                return result;
            }

        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage(e.getMessage());
            logger.error(StringIteration.SPACE, e);
        }
        return null;
    }

    @Override
    public Result<State> delete(String id) {

        Result<State> result = new Result<>();
        try {
            Optional<State> tr = stateRepository.findById(id);

            if (tr.isPresent()) {
                stateRepository.delete(tr.get());
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

    public List<State> findByCountryCode(String countryCode) {
        try {
            return stateRepository.findAllByCountryCode(countryCode);
        } catch (Exception e) {
            logger.error(StringIteration.SPACE, e);
        }
        return Collections.emptyList();
    }

    public Iterable<Map<String, String>> domainListValue() {
        try {
            return stateRepository.findAll().stream().map(o -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("stateCode", o.getStateCode());
                map.put("stateName", o.getStateName());
                map.put("countryCode", o.getCountryCode());
                return map;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(StringIteration.SPACE, e);
        }
        return null;
    }
    public Result<HashMap<String, Object>> findAllState(int page, String value, String sortBy, String sortDir) {
		  Result<HashMap<String, Object>> result = new Result<>();
		    try {
		        HashMap<String, Object> map = new HashMap<>();
		        int size = 10;
		        long totalCount=0;

//		        if (value != null && !value.isEmpty()) {
//		            page = 0;
//		        }

		        if (StringUtils.isNullOrEmpty(sortBy)) {
		            sortBy = "state_name";
		        }

		        if (StringUtils.isNullOrEmpty(sortDir)) {
		            sortDir = "ASC";
		        }

		        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		      
		    	   
		            List<State> state = stateRepository.findAllState(value, paging);
		            
		            if(value.isEmpty()) {
		            	  totalCount = stateRepository.findAllCount().size();	 
		            	
		            }
		            if(!value.isEmpty()) {
		            	  totalCount = stateRepository.findAllState(value).size();	 
		            }
		           
		      System.err.println("stateController"); 
		        map.put("totalCount", totalCount);
		        map.put("state ",  state );
		        result.setData(map);
		        result.setCode("0000");
		        result.setMessage("success");
		        return result;

		    } catch (Exception ex) {
		        logger.error(ex);
		        result.setCode("1111");
		    }
		    return null;
		

	
	}

	public Result<State> activeAndDeActivateState(String id) {
		Result<State> result = new Result<>();
		try {
			Optional<State> list = stateRepository.findById(id);
			if (list.isPresent()) {
				State s = list.get();
				if(s.getStatus()!=null) {
					if(s.getStatus().equals(StringIteration.ACTIVE)){
						s.setStatus(StringIteration.DEACTIVE);
					}
					else {
						s.setStatus(StringIteration.ACTIVE);
					}
				}
				else {
					s.setStatus(StringIteration.DEACTIVE);
				}
				CommonUtil.setModifiedOn(s);
				stateRepository.save(s);
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
	
	public void updateKey(Map<String, BiConsumer<State, Cell>> columnPropertyMapping) {
        columnPropertyMapping.put("StateCode", (state, cell) -> state.setStateCode(cell.getStringCellValue()));
        columnPropertyMapping.put("StateName", (state, cell) -> state.setStateName(cell.getStringCellValue())); 
        columnPropertyMapping.put("CountryName", (state, cell) -> state.setCountryCode(cell.getStringCellValue()));
    }
	public List<State> fileReader(MultipartFile file, String sheetIndex) throws IOException {
	    List<State> states = new ArrayList<>();
	    
	    try (FileInputStream fileInputStream = new FileInputStream(convertMultiPartToFile(file))) {
	        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
	        XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
	        Map<String, BiConsumer<State, Cell>> columnPropertyMapping = new HashMap<>();
	        updateKey(columnPropertyMapping);
	        int rowIndex = 0;

	        for (Row row : sheet) {
	            if (rowIndex == 0) {
	                rowIndex++;
	                continue;
	            }
	            State state = new State();
	            state.setId(System.currentTimeMillis() + "");
	            state.setStatus(StringIteration.ACTIVE);
	            for (Cell cell : row) {
	                int columnIndex = cell.getColumnIndex();
	                String columnName = sheet.getRow(0).getCell(columnIndex).getStringCellValue();
	                if (columnPropertyMapping.containsKey(columnName.trim())) {
	                    columnPropertyMapping.get(columnName).accept(state, cell);
	                }
	            }
	            states.add(state);
	            rowIndex++;
	        }
	        workbook.close();
	    }
	    if (!states.isEmpty()) {
	        states.forEach(state -> {
	            Optional<Country> cu = countryRepository.findCountryName(state.getCountryCode().replace(" ", "").toLowerCase());
	            if (cu.isPresent()) {
	                state.setCountryCode(cu.get().getCountryCode());
	                create(state);
	            }
	        });
	    }
	    return states;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    
	    try (FileOutputStream fos = new FileOutputStream(convFile)) {
	        fos.write(file.getBytes());
	    }
	    return convFile;
	}
		
	}



