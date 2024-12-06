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
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.documents.Country;
import com.healthtraze.etraze.api.masters.documents.State;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.CountryRepository;
import com.healthtraze.etraze.api.masters.repository.StateRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil; 

@Component
public class CityService implements BaseService<City, String> {

	private Logger logger = LogManager.getLogger(CityService.class);

	public static final String SEQUENCE_NAME = "City_sequence";

 
private final CityRepository cityRepository;

	
	private final UserRepository repository;
	
	private final StateRepository stateRepository;
	
	
	private final CountryRepository countryRepository;
	
	@Autowired
	public CityService(CityRepository cityRepository, UserRepository repository, StateRepository stateRepository,
			CountryRepository countryRepository) {
		super();
		this.cityRepository = cityRepository;
		this.repository = repository;
		this.stateRepository = stateRepository;
		this.countryRepository = countryRepository;
	}
	@Override
	public List<City> findAll() {
		try {
			return cityRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}
	
	public List<City> findAllcities() {
		try {
			return cityRepository.findAllCitis();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@Override
	public City findById(String id) {
		try {
			Optional<City> option = cityRepository.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<City> create(City t) {
		Result<City> result = new Result<>();
		try {
			Optional<User> user = repository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				
				
				String cleanedCityName = t.getCityName().replaceAll("[^a-zA-Z0-9]", "");
				String cityCodePrefix;

				String firstThreeChars = cleanedCityName.substring(0, Math.min(cleanedCityName.length(), 3)).toUpperCase();

				Optional<String> option1 = cityRepository.findByCityCode(firstThreeChars);
				if (option1.isPresent()) {
				    List<Character> charList = new ArrayList<>();
				    for (char c : firstThreeChars.toCharArray()) {
				        charList.add(c);
				    }
				    Collections.shuffle(charList);

				    StringBuilder shuffledPrefixBuilder = new StringBuilder();
				    for (char c : charList) {
				        shuffledPrefixBuilder.append(c);
				    }
				    cityCodePrefix = shuffledPrefixBuilder.toString().toUpperCase();
				    t.setCityCode(cityCodePrefix);
				} else {
				    t.setCityCode(firstThreeChars);
				    cityCodePrefix = firstThreeChars;
				}

				String lastSequence = cityRepository.count()+"";
				int nextSequence = Integer.parseInt(lastSequence) + 1;
				String sequenceNumber = String.format("%05d", nextSequence);
				String cityId = cityCodePrefix + sequenceNumber;
				t.setCityCode(cityId);
				

				List<City> cityresult = cityRepository.findByCityNameAndStateCode(t.getCityName().replace(" ", "").toLowerCase(),t.getStateCode());

				if (cityresult != null && cityresult.isEmpty()) {
					CommonUtil.setCreatedOn(t);
					t.setId(System.currentTimeMillis() + "");
					t.setStatus(StringIteration.ACTIVE);
					City c = cityRepository.save(t);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.CITYCREATEDSUCCESSFULLY);
					result.setData(c);
					return result;
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Already Exist");
					return result;
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<City> update(City t) {
		Result<City> result = new Result<>();
		try {

			List<City> state =  cityRepository.findByCountryCodeAndStateCodeAndCityCodeAndCityName(
			t.getCountryCode() , t.getStateCode() , t.getCityCode() , t.getCityName());

			if (state.isEmpty()) {

				Optional<City> ci = cityRepository.findById(t.getId());

				if (ci.isPresent()) {
					t.setModifiedOn(new Date());
					City c = cityRepository.save(t);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
					result.setData(c);
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
	public Result<City> delete(String id) {
		Result<City> result = new Result<>();
		try {
			Optional<City> tr = cityRepository.findById(id);

			if (tr.isPresent()) {
				cityRepository.delete(tr.get());
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

	public List<City> findByStateCode(String stateCode) {
		try {
			return cityRepository.findAllBystateCode(stateCode);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	public Iterable<Map<String, String>> domainListValue() {
		try {
			return cityRepository.findAll().stream().map(o -> {
				HashMap<String, String> map = new HashMap<>();
				map.put(Constants.CITYCODE, o.getCityCode());
				map.put(Constants.CITYNAME, o.getCityName());
				map.put(Constants.STATECODE, o.getStateCode());
				return map;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public List<City> findAllDetails() {
		try {
			Optional<User> user = repository.findByUserId(SecurityUtil.getUserName());
			if(user.isPresent()) {
				List<City> list = cityRepository.findByTenantId(user.get().getTenantId());
				if(!list.isEmpty()) {
					return list.stream().sorted(Comparator.comparing(City::getCreatedOn, Comparator.reverseOrder()))
							.collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}
	
	 public Result<HashMap<String, Object>> findAllCity(int page, String value, String sortBy, String sortDir) {
		  Result<HashMap<String, Object>> result = new Result<>();
		    try {
		        HashMap<String, Object> map = new HashMap<>();
		        int size = 10;
		        long totalCount=0;

//		        if (value != null && !value.isEmpty()) {
//		            page = 0;
//		        }

		        if (StringUtils.isNullOrEmpty(sortBy)) {
		            sortBy = "city_name";
		        }

		        if (StringUtils.isNullOrEmpty(sortDir)) {
		            sortDir = "ASC";
		        }

		        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		      
		    	   
		            List<City> city = cityRepository.findAllCity(value, paging);
		            for(City c : city) {
		            List<State> s=stateRepository.findAllByStateCode(c.getStateCode());
		          
		            	c.setStateName(s.get(0).getStateName());
		            
		            }
		            if(value!=null&&value.isEmpty()) {
			        	 totalCount = cityRepository.findAllCount().size();	
			        	
			        }
			        if(value!=null&&!value.isEmpty()) {
			        	 totalCount = cityRepository.findAllCity(value).size();	
			        	
			        }
		           
		        map.put("totalCount", totalCount);
		        map.put("city ",  city );
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

	public Result<City> activeAndDeActivateCity(String code) {
		
		
			Result<City> result = new Result<>();
			try {
				Optional<City> list = cityRepository.findById(code);
				if (list.isPresent()) {
					City c = list.get();
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
					cityRepository.save(c);
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

	 
	public void updateKey(Map<String, BiConsumer<City, Cell>> columnPropertyMapping) {
        columnPropertyMapping.put("CityName", (city, cell) ->   city.setCityName(cell.getStringCellValue())); 
        columnPropertyMapping.put("CountryName", (city, cell) ->   city.setCountryCode(cell.getStringCellValue()));
        columnPropertyMapping.put("StateName", (city, cell) -> city.setStateCode(cell.getStringCellValue()));
    }
	
	public List<City> fileReader(MultipartFile file, String sheetIndex) throws IOException {
	    try (FileInputStream fileInputStream = new FileInputStream(convertMultiPartToFile(file));
	         XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

	        List<City> cities = new ArrayList<>();
	        XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
	        Map<String, BiConsumer<City, Cell>> columnPropertyMapping = new HashMap<>();
	        updateKey(columnPropertyMapping);
	        int rowIndex = 0;
	        for (Row row : sheet) {
	            if (rowIndex == 0) {
	                rowIndex++;
	                continue;
	            }
	            City city = new City();
	            city.setId(System.currentTimeMillis() + "");
	            city.setStatus("ACTIVE");
	            for (Cell cell : row) {
	                int columnIndex = cell.getColumnIndex();
	                String columnName = sheet.getRow(0).getCell(columnIndex).getStringCellValue();
	                if (columnPropertyMapping.containsKey(columnName.trim())) {
	                    columnPropertyMapping.get(columnName).accept(city, cell);
	                }
	            }
	            cities.add(city);
	            rowIndex++;
	        }
	        if (!cities.isEmpty()) {
	            cities.forEach(city -> {
	                Optional<Country> countryOptional = countryRepository.findCountryName(city.getCountryCode().replace(" ", "").toLowerCase());
	                countryOptional.ifPresent(country -> {
	                    city.setCountryCode(country.getCountryCode());
	                    Optional<State> stateOptional = stateRepository.findStateName(city.getStateCode().replace(" ", "").toLowerCase());
	                    stateOptional.ifPresent(state -> {
	                        city.setStateCode(state.getStateCode());
	                        create(city);
	                    });
	                });
	            });
	        }
	        return cities;
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