package com.healthtraze.etraze.api.masters.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.healthtraze.etraze.api.masters.model.Bank;
import com.healthtraze.etraze.api.masters.repository.BankRepository;

@Component
public class BankService implements BaseService<Bank, String> {
	private Logger logger = LogManager.getLogger(BankService.class);


	private BankRepository bankRepository;
	
	
	public BankService(BankRepository bankRepository) {
		this.bankRepository=bankRepository;
		
	}

	@Override
	public List<Bank> findAll() {
		try {
			return bankRepository.findAllBanks();

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	@Override
	public Bank findById(String id) {
		try {
			Optional<Bank> optional = bankRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public Result<Bank> create(Bank t) {
	    Result<Bank> result = new Result<>();
	    try {
	        t.setCode(System.currentTimeMillis() + "");
	        t.setCode("BK0000" + (bankRepository.count() + 1));
	        t.setStatus(StringIteration.ACTIVE);
	        CommonUtil.setCreatedOn(t);
	        

	        if (isDuplicateBankName(t.getName())) {
	            result.setCode("1111");
	            result.setMessage("Duplicate bank name found");
	            return result;
	        }
	        
	        bankRepository.save(t);
	        result.setCode("0000");
	        result.setMessage("Successful Created");
	        result.setData(t);
	    } catch (Exception e) {
	        result.setCode("1111");
	        logger.error("", e);
	    }
	    return result;
	}


		private boolean isDuplicateBankName(String bankName) {
		    if (bankName == null || bankName.trim().isEmpty()) {
		        return false;
		    }
		    
		    List<Bank> banks = bankRepository.findAll();

		    String normalizedBankName = bankName.trim().replaceAll("\\s+", " ").toLowerCase();

		    for (Bank bank : banks) {

		        String normalizedDBBankName = bank.getName().trim().replaceAll("\\s+", " ").toLowerCase();

		        if (normalizedDBBankName.equals(normalizedBankName)) {
		            return true;
		        }
		    }

		    return false;
		}

	@Override
	public Result<Bank> update(Bank t) {
		Result<Bank> result = new Result<>();
		try {
			Optional<Bank> list = bankRepository.findByName(t.getName());
			if (!list.isPresent()) {
				CommonUtil.setModifiedOn(t);
				bankRepository.save(t);
				result.setCode("0000");
				result.setMessage("Updated Successfully");
				result.setData(t);
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
	
	public Result<Bank> activeAndDeActivateBank(String t) {
		Result<Bank> result = new Result<>();
		try {
			Optional<Bank> list = bankRepository.findById(t);
			if (list.isPresent()) {
				Bank b = list.get();
				if(b.getStatus()!=null) {
					if(b.getStatus().equals(StringIteration.ACTIVE)){
						b.setStatus("DEACTIVE");
					}
					else {
						b.setStatus("ACTIVE");
					}
				}
				else {
					b.setStatus("DEACTIVE");
				}
				CommonUtil.setModifiedOn(b);
				bankRepository.save(b);
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
	public Result<Bank> delete(String id) {
		Result<Bank> result = new Result<>();
		try {
			Optional<Bank> optional = bankRepository.findById(id);
			if (optional.isPresent()) {
				bankRepository.deleteById(optional.get().getCode());
				result.setCode("0000");
				result.setMessage("Successful deleted");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	

		
		public Result<HashMap<String, Object>> findAllBank(int page, String value, String sortBy, String sortDir) {
		    Result<HashMap<String, Object>> result = new Result<>();
		    try {
		        HashMap<String, Object> map = new HashMap<>();
		        int size = 10;
		        long totalCount=0;

		        if (StringUtils.isNullOrEmpty(sortBy)) {
		            sortBy = "name";
		        }

	        if (StringUtils.isNullOrEmpty(sortDir)) {
	            sortDir = "ASC";
		        }

		        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		      
		    	   
	            List<Bank> banks = bankRepository.findAllBank(value, paging);
	            if(value.isEmpty()) {
			         totalCount = bankRepository.findAllCount().size();
		         }
				if(!value.isEmpty()) {
						totalCount = bankRepository.findAllBank(value).size();
						
					}
		      
		          
	        map.put("totalCount", totalCount);
		        map.put("bank", banks);
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

		public void updateKey(Map<String, BiConsumer<Bank, Cell>> columnPropertyMapping) {
        columnPropertyMapping.put("Bank", (bank, cell) -> bank.setName(cell.getStringCellValue()));
    }
	

		public Result<List<Bank>> fileReader(MultipartFile file , String sheetIndex) throws  IOException { 
			Result<List<Bank>> result = new Result<>();
	        try(FileInputStream fileInputStream = new FileInputStream(convertMultiPartToFile(file))){
	        	   XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
	        	   XSSFSheet  sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
	        	   List<Bank> banks = new ArrayList<>(); 
	   	        
	   	        Map<String, BiConsumer<Bank, Cell>> columnPropertyMapping = new HashMap<>();  
	   	        updateKey(columnPropertyMapping);
	   	        int rowIndex = 0;
	   	        int bankCount = (int) bankRepository.count();
	   	     for (Row row : sheet) { 
	   	            if (rowIndex == 0) {
	   	                rowIndex++;
	   	                continue;
	   	            } 
	   	            Bank bank = new Bank();          
	   	            bank.setCode("BK0000" + (bankCount+ 1));
	   	            bank.setStatus("ACTIVE");
	   	            for (Cell cell : row) {
	   	                int columnIndex = cell.getColumnIndex();
	   	                String columnName = sheet.getRow(0).getCell(columnIndex).getStringCellValue();    
	   	                if (columnPropertyMapping.containsKey(columnName.trim())) {  
	   	                    columnPropertyMapping.get(columnName).accept(bank, cell);
	   	                }
	   	            }
	   	            CommonUtil.setCreatedOn(bank);
	   	            rowIndex++;
	   	            if(duplicateBank(bankRepository.findAll(),bank.getName())) {
	   	             result.setCode("1111");
	                 return result; 
	   	            }
	   	            banks.add(bank);
	   	            bankCount++;
	   	             
	   	        } 
	   
	   	        workbook.close(); 
	   	        if(!banks.isEmpty()) {

	   	        		bankRepository.saveAll(banks);
	   	        		result.setCode(StringIteration.SUCCESS_CODE);
	   	        		result.setData(banks);
	   	        		result.setMessage("saved successfully");
	   	        } 
	   	        return result;

	        }catch(IOException e) {
	        	logger.error(e);
	        	result.setCode(StringIteration.ERROR_CODE1);	        	
	        }
	     return result;
	  }
		
		private boolean duplicateBank(List<Bank> banks, String bankName) {
		    return banks.stream().anyMatch(b ->
		            b.getName().toLowerCase().replaceAll("\\s", "")
		                    .equals(bankName.toLowerCase().replaceAll("\\s", ""))
		    );
		}

		
		
		
	    private File convertMultiPartToFile(MultipartFile file ) throws IOException {
	        File convFile = new File( file.getOriginalFilename() );
	    
	       try (FileOutputStream fos = new FileOutputStream( convFile )){
	        fos.write(file.getBytes());
	       
	        return convFile;
	    }catch (IOException e) {
	    	logger.error(e);
	    }
	       return  null;
	    }

	
	
	
}


