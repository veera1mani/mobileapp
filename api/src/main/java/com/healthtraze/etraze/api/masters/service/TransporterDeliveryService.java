package com.healthtraze.etraze.api.masters.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.TransporterAmountChartDTO;
import com.healthtraze.etraze.api.masters.dto.TransporterChartDTO;
import com.healthtraze.etraze.api.masters.dto.TransporterImport;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.model.TransporterDelivery;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.masters.repository.TransporterDeliveryRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class TransporterDeliveryService implements BaseService<TransporterDelivery, String>{
	
	private Logger logger = LogManager.getLogger(TransporterDeliveryService.class);
	
	private final  UserRepository userRepository;
	
	private final FileStorageService fileStorageService ;
	
	private final TenantManufactureRepository tenantManufactureRepository;
	
	private final ManufacturerRepository manufacturerRepository ;
	
	private final TransporterDeliveryRepository transporterDeliveryRepository;

	private final TransportRepository transportRepository;
	

	@Override
	public List<TransporterDelivery> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransporterDelivery findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	public TransporterDeliveryService(UserRepository userRepository, FileStorageService fileStorageService,
			TenantManufactureRepository tenantManufactureRepository, ManufacturerRepository manufacturerRepository,
			TransporterDeliveryRepository transporterDeliveryRepository, TransportRepository transportRepository) {
		super();
		this.userRepository = userRepository;
		this.fileStorageService = fileStorageService;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.transporterDeliveryRepository = transporterDeliveryRepository;
		this.transportRepository = transportRepository;
	}


	@Override
	public Result<TransporterDelivery> create(TransporterDelivery t) {
		return null;
	}


	public Result<TransporterDelivery> create(TransporterDelivery t, long key) {
		Result<TransporterDelivery> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				
				CommonUtil.setCreatedOn(t);
				t.setDeliveryId(String.valueOf(key));
				t.setTenantId(u.getTenantId());
				transporterDeliveryRepository.save(t);
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
			}		
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	@Override
	public Result<TransporterDelivery> update(TransporterDelivery t) {
		Result<TransporterDelivery> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				
				Optional<TransporterDelivery> optional = transporterDeliveryRepository.findByMonthAndYear(t.getMonth() , t.getYear(), u.getTenantId() , t.getManufacturerId(), t.getTransporterId());
				
				if(optional.isPresent()) {
					TransporterDelivery td = optional.get();
					td.setTransporterExpenses(t.getTransporterExpenses());
					transporterDeliveryRepository.save(td);
					
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				}
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	@Override
	public Result<TransporterDelivery> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Result<Object> getTransporterExpenses(String id){
		Result<Object> result = new Result<>();
		
		return result;
	}
	
	public Result<Object> fileReader(MultipartFile file, String sheetIndex) throws IOException {
		
		Result<Object> result = new Result<>();
		List<TransporterDelivery> valid = new ArrayList<>();
		List<TransporterDelivery> invalid = new ArrayList<>();
		
		try(FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {
			
			XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			
			if (us.isPresent()) {
				User u = us.get();			
	
				if (sheet.getLastRowNum() < 1) {
					result.setCode(StringIteration.ERROR_CODE3);
					result.setMessage("No data found in the Excel sheet");
					return result;
				}
				
				int rowIndex = 0;
				for (Row row : sheet) {
					if (rowIndex == 0) {
						rowIndex++;
						continue;
					}
					TransporterDelivery td = new TransporterDelivery();										
					TransporterImport transportImport = new TransporterImport(true,row, td);	
					
					prepareRows(transportImport,row,td);         
					validation(transportImport, td , u );
		            
		            if (transportImport.isFlag()) {	            
		            	 valid.add(td);					
					} else {	
						td.setRemarks(transportImport.getMessage());
						invalid.add(td);				
					}		            
		            rowIndex++;
				}
				
				saveExcel(valid, result,u);
				invalidExcel(invalid , result);			
			}
				
			return result;	
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result.setCode(StringIteration.ERROR_CODE4);
			result.setMessage("inside catch");
		}
		return result;
	}
	
	void prepareRows(TransporterImport transportImport , Row row ,TransporterDelivery td ) {
		try {
			prepareManufacturer(transportImport , row , td);
			prepareTransporter(transportImport , row , td);
			prepareMonth(transportImport , row , td);
			prepareYear(transportImport , row , td);
			prepareAmount(transportImport , row , td);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void prepareManufacturer(TransporterImport tn,Row row, TransporterDelivery td) {
		try {			
			Cell manf = row.getCell(0);
			if (manf == null || manf.getCellType() == CellType.BLANK ) {
				tn.setFlag(false);
				tn.setMessage("Manufacturer can not be empty");
			} else {
				if (manf.getCellType() == CellType.NUMERIC) {
    				double numericValue = manf.getNumericCellValue();
    				long longValue = (long) numericValue;
    				td.setManufacturerId(String.valueOf(longValue).trim());
    			} else if (manf.getCellType() == CellType.STRING) {
    				td.setManufacturerId(manf.getStringCellValue().trim());
    			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void prepareTransporter(TransporterImport tn,Row row, TransporterDelivery td) {
		try {			
			Cell manf = row.getCell(1);
			if (manf == null || manf.getCellType() == CellType.BLANK ) {
				tn.setFlag(false);
				tn.setMessage("Transporter can not be empty");
			} else {
				if (manf.getCellType() == CellType.NUMERIC) {
    				double numericValue = manf.getNumericCellValue();
    				long longValue = (long) numericValue;
    				td.setTransporterId(String.valueOf(longValue).trim());
    			} else if (manf.getCellType() == CellType.STRING) {
    				td.setTransporterId(manf.getStringCellValue().trim());
    			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	void prepareMonth(TransporterImport tn,Row row, TransporterDelivery td) {
		try {			
			Cell manf = row.getCell(2);
			if (manf == null || manf.getCellType() == CellType.BLANK ) {
				tn.setFlag(false);
				tn.setMessage("Month can not be empty");
			} else {
				if (manf.getCellType() == CellType.NUMERIC) {
    				double numericValue = manf.getNumericCellValue();
    				long longValue = (long) numericValue;
    				if(String.valueOf(longValue).trim().length() < 2) {
    					td.setMonth("0"+String.valueOf(longValue).trim());
    				}else {
    					td.setMonth(String.valueOf(longValue).trim());
    				}	
    			} else if (manf.getCellType() == CellType.STRING) {
    				if(manf.getStringCellValue().trim().length() < 2) {
    					td.setMonth("0"+manf.getStringCellValue().trim());
    				}else {
    					td.setMonth(manf.getStringCellValue().trim());
    				}
    			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	void prepareYear(TransporterImport tn,Row row, TransporterDelivery td) {
		try {			
			Cell manf = row.getCell(3);
			if (manf == null || manf.getCellType() == CellType.BLANK ) {
				tn.setFlag(false);
				tn.setMessage("Year can not be empty");
			} else {
				if (manf.getCellType() == CellType.NUMERIC) {
    				double numericValue = manf.getNumericCellValue();
    				long longValue = (long) numericValue;
    				td.setYear(String.valueOf(longValue).trim());
    			} else if (manf.getCellType() == CellType.STRING) {
    				td.setYear(manf.getStringCellValue().trim());
    			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	void prepareAmount(TransporterImport tn,Row row, TransporterDelivery td) {
		try {			
			Cell manf = row.getCell(4);
			if (manf == null || manf.getCellType() == CellType.BLANK ) {
				tn.setFlag(false);
				tn.setMessage("Amount can not be empty");
			} else {
				if (manf.getCellType() == CellType.NUMERIC) {
    				double numericValue = manf.getNumericCellValue();    				
    				td.setTransporterExpenses(numericValue);
    			}else {
    				String amount = manf.getStringCellValue();
    				double deliveryAmount = Double.parseDouble(amount);
    				td.setTransporterExpenses(deliveryAmount);
    			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void validation(TransporterImport tn, TransporterDelivery td, User u) {
		try {
			if(td.getManufacturerId() != null) {
				Optional<Manufacturer> manf = manufacturerRepository.findByManufacturerNameAndStatus(td.getManufacturerId().replace(" ","").toLowerCase());
				if(manf.isPresent()) {
					Optional<TenantManufacture> tm = tenantManufactureRepository.findByManufactureIdAndTenantAndStatus(manf.get().getManufacturerId(),u.getTenantId());
					if(tm.isPresent()) {
						Optional<Transport> t = transportRepository.findByTransporterNameAndStatus(td.getTransporterId().toLowerCase().replaceAll(" ",""), u.getTenantId());
						if(t.isPresent()) {							
							td.setManufacturerId(tm.get().getManufacturerId());
							td.setTransporterId(t.get().getTransportId());
							Date date = new Date();
							String date1 = String.valueOf(date.getDate())+"-"+td.getMonth()+"-"+td.getYear();
							Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
				            td.setDeliveryDate(date2);
						}else {
							tn.setFlag(false);
							tn.setMessage("transporter not found");
						}
					}else {
						tn.setFlag(false);
						tn.setMessage("manufacturer may be not mapped for this tenant Or manufacturer may be inactive");
					}
				}else {
					tn.setFlag(false);
					tn.setMessage("manufacturer not found");
				}
			} else {
				tn.setFlag(false);
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	void saveExcel(List<TransporterDelivery> valid , Result<Object> result, User u) {
		if(!valid.isEmpty()) {
			
			long key = System.currentTimeMillis();
			for(TransporterDelivery td : valid) {
				
				Optional<TransporterDelivery> transporter = transporterDeliveryRepository.findByMonthAndYear(td.getMonth(),td.getYear() , u.getTenantId() ,td.getManufacturerId(), td.getTransporterId());
				
				if(transporter.isPresent()) {
					TransporterDelivery trans = transporter.get();
					trans.setTransporterExpenses(td.getTransporterExpenses());
					transporterDeliveryRepository.save(trans);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				}else {				
					
					Result<TransporterDelivery> res =  create(td,key++);
					result.setCode(res.getCode());
					result.setMessage(res.getMessage());
				}	
			}
		}
	}
	
	void invalidExcel(List<TransporterDelivery> invalid , Result<Object> result) {
		try (XSSFWorkbook workbook1 = new XSSFWorkbook()){
			if(!invalid.isEmpty()) {				
				String[]headerList = {"Manufacturer name","Transporter name","Month","Year","Amount" ,"Remarks"};	         
	            XSSFSheet spreadsheet = workbook1.createSheet(" Invalid TransporterExpense Excel ");
	            
	            int rowId = 0;	           
	            XSSFRow row1;
	            row1 = spreadsheet.createRow(rowId++);
	            int r=0;
	            for (String s : headerList) {
	                row1.createCell(r).setCellValue(s);
	                r++;
	            }
	            
	            	for(TransporterDelivery td : invalid) {
		            	 row1 = spreadsheet.createRow(rowId++);
		            	 row1.createCell(0).setCellValue(td.getManufacturerId());
		            	 row1.createCell(1).setCellValue(td.getTransporterId());	            	 
						 row1.createCell(2).setCellValue(td.getMonth());
						 row1.createCell(3).setCellValue(td.getYear());	
						 row1.createCell(4).setCellValue(td.getTransporterExpenses());	
						 row1.createCell(5).setCellValue(td.getRemarks());
					}            
	            
	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            workbook1.write(byteArrayOutputStream);
	            InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

	            String fileId = UUID.randomUUID().toString();

	            com.healthtraze.etraze.api.file.model.File invalidCheques = fileStorageService.uploadFileToAWS(fileId,
	                    fileId, byteArrayInputStream, "invalidTransporterExpense.xlsx", "document", 0);

	            result.setCode(StringIteration.ERROR_CODE1);
	            result.setData(invalidCheques);
	            result.setMessage("invalid data found");

	            byteArrayOutputStream.flush();
	            byteArrayOutputStream.close();	            
			} 
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public HashMap<String , Object> getChart(String month, String year , String manfId){
		HashMap<String , Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				 User u = us.get();
				List<TransporterAmountChartDTO> tm = tenantManufactureRepository.getTenantManufacturesByIdBy(u.getTenantId());
				List<String> transporterNames = transportRepository.findByTenantIdForName(u.getTenantId());
				List<Transport> transport = transportRepository.findByTenantId(u.getTenantId());
				List<TransporterChartDTO> series = new ArrayList<>();
				Long total = 0l;
				for(TransporterAmountChartDTO m :tm) {
					if(manfId.equals(m.getManufacturerId())) {
						TransporterChartDTO dt = new TransporterChartDTO();
						dt.setName(m.getManufacturerName());
						List<Long> dou = new ArrayList<>();
						for(Transport t : transport) {
							Long expense = transporterDeliveryRepository.findTransporterExpenses(m.getManufacturerId(),u.getTenantId(),t.getTransportId(),year, month);
							dou.add(expense); 
							total += expense ;
						}
						dt.setData(dou);
						series.add(dt);
					}	
				}
				
				map.put("categories",transporterNames);
				map.put("series",series);
				map.put("total",total);	
				return map;

			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		
		return map;
	}

}
