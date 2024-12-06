package com.healthtraze.etraze.api.masters.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.documents.Country;
import com.healthtraze.etraze.api.masters.repository.CountryRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil; 

@Component
public class CountryService implements BaseService<Country, String> {

	private Logger logger = LogManager.getLogger(CountryService.class);

	private final UserRepository repository;
	private final	CountryRepository countryRepository;
	@Autowired
	public CountryService(CountryRepository countryRepository, UserRepository repository) {
	this.countryRepository = countryRepository;
	this.repository = repository;
}
 

	@Override
	public List<Country> findAll() {
		try {
			Optional<User> user = repository.findByUserId(SecurityUtil.getUserName());
			if(user.isPresent()) {
				List<Country> countries = countryRepository.findByTenantId(user.get().getTenantId());
				if(!countries.isEmpty()) {
					return countries.stream().sorted(Comparator.comparing(Country::getCreatedOn,Comparator.reverseOrder())).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@Override
	public Country findById(String id) {
		try {
			Optional<Country> option = countryRepository.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	
	@Override

	public Result<Country> create(Country t) {
		Result<Country> result = new Result<>();
		try {
			Optional<User> user = repository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				
				t.setCreatedOn(new Date());
               
				t.setId(System.currentTimeMillis() + "");
				t.setStatus(StringIteration.ACTIVE);
				
			 Optional<Country> countryCode = countryRepository.findByCountryCode(t.getCountryCode());
			 Optional<Country> countryName=countryRepository.findByCountryName(t.getCountryName());
				if(countryCode.isPresent() ||countryName.isPresent()) {
				result.setCode("1111");
				result.setMessage("country already exist");
				return result;
				}
	           else {
				Country c = countryRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.COUNTRYCREATEDSUCCESSFULLY);
				result.setData(c);
				return result;
			}}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<Country> update(Country t) {
		Result<Country> result = new Result<>();
		try {
				Optional<Country> co = countryRepository.findById(t.getId());
				if (co.isPresent()) {
					t.setModifiedOn(new Date());
					t.setVersionNo(co.get().getVersionNo());
					Country ic = countryRepository.save(t);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(Constants.SUCCESSFULLYUPDATED);
					result.setData(ic);
					return result;
				
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
	public Result<Country> delete(String id) {
		Result<Country> result = new Result<>();
		try {

			Optional<Country> tr = countryRepository.findById(id);

			if (tr.isPresent()) {
				countryRepository.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id + StringIteration.TERRITORYDELETEDSUCCESSFULLY);

				return result;
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	public Iterable<Map<String, String>> domainListValue() {
		try {
			return countryRepository.findAll().stream().map(o -> {
				HashMap<String, String> map = new HashMap<>();
				map.put(Constants.COUNTRYCODE, o.getCountryCode());
				map.put(Constants.COUNTRYNAME, o.getCountryName());
				return map;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public List<Country> findAllDetails() {
		try {
			return countryRepository.findAllByCountry();

		
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	public Result<HashMap<String, Object>> findAllCountry(int page, String value, String sortBy, String sortDir) {
		  Result<HashMap<String, Object>> result = new Result<>();
		    try {
		        HashMap<String, Object> map = new HashMap<>();
		        int size = 10;

		        if (value != null && !value.isEmpty()) {
		            page = 0;
		        }

		        if (StringUtils.isNullOrEmpty(sortBy)) {
		            sortBy = "country_name";
		        }

		        if (StringUtils.isNullOrEmpty(sortDir)) {
		            sortDir = "ASC";
		        }

		        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		      
		    	   
		            List<Country> country = countryRepository.findAllCountry(value, paging);
		           
		    
		        int totalCount = countryRepository.findAllCount().size();	       
		        map.put("totalCount", totalCount);
		        map.put("country ", country );
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

	public Result<Country> activeAndDeActivateCountry(String id) {
		Result<Country> result = new Result<>();
		try {
			Optional<Country> list =countryRepository.findById(id);
			if (list.isPresent()) {
				Country c = list.get();
				if(c.getStatus()!=null) {
					if(c.getStatus().equals(StringIteration.ACTIVE)){
						c.setStatus("DEACTIVE");
					}
					else {
						c.setStatus("ACTIVE");
					}
				}
				else {
					c.setStatus("DEACTIVE");
				}
				CommonUtil.setModifiedOn(c);
				countryRepository.save(c);
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

	public void updateKey(Map<String, BiConsumer<Country, Cell>> columnPropertyMapping) {
        columnPropertyMapping.put("CountryCode", (country, cell) -> country.setCountryCode(cell.getStringCellValue()));
        columnPropertyMapping.put("CountryName", (country, cell) -> country.setCountryName(cell.getStringCellValue()));
    }
	
	public List<Country> fileReader(MultipartFile file, String sheetIndex) throws IOException {
	    try (FileInputStream fileInputStream = new FileInputStream(convertMultiPartToFile(file));
	         XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

	        List<Country> countries = new ArrayList<>();
	        XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
	        Map<String, BiConsumer<Country, Cell>> columnPropertyMapping = new HashMap<>();
	        updateKey(columnPropertyMapping);

	        int rowIndex = 0;
	        for (Row row : sheet) {
	            if (rowIndex == 0) {
	                rowIndex++;
	                continue;
	            }

	            Country country = new Country();
	            for (Cell cell : row) {
	                int columnIndex = cell.getColumnIndex();
	                String columnName = sheet.getRow(0).getCell(columnIndex).getStringCellValue();
	                if (columnPropertyMapping.containsKey(columnName.trim())) {
	                    columnPropertyMapping.get(columnName).accept(country, cell);
	                }
	            }
	            countries.add(country);
	            rowIndex++;
	        }

	        if (!countries.isEmpty()) {
	            countries.forEach(country -> {
	                String countryName = country.getCountryName().replace(" ", "").toLowerCase();
	                String countryCode = country.getCountryCode().replace(" ", "").toLowerCase();
	                Optional<Country> countryCodeOpt = countryRepository.findByCountryName(countryName, countryCode);
	                if (!countryCodeOpt.isPresent()) {
	                    country.setId(String.valueOf(System.currentTimeMillis()));
	                    country.setStatus(StringIteration.ACTIVE);
	                    CommonUtil.setCreatedOn(country);
	                    countryRepository.save(country);
	                }
	            });
	        }

	        return countries;
	    }
	}

	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    try (FileOutputStream fos = new FileOutputStream(convFile)) {
	        fos.write(file.getBytes());
	    }
	    return convFile;
	}
}
