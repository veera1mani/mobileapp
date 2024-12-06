package com.healthtraze.etraze.api.masters.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.file.model.FileResponse;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.model.ManufacturerReport;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.projections.ManuafctureTenantReport;
import com.healthtraze.etraze.api.masters.projections.ManufactureReport;
import com.healthtraze.etraze.api.masters.repository.ManufacturerReportRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class ManufacturerReportService implements BaseService<ManufacturerReport, String> {
	
	Logger logger = LogManager.getLogger(ManufacturerReportService.class);

	private final ManufacturerReportRepository manufacturerReportRepository;
	
	private final FileStorageService fileStorageService;
	
	private final UserRepository userRepository;
   private final TenantManufactureRepository tenantManufactureRepository;
	@Autowired
	public ManufacturerReportService(ManufacturerReportRepository manufacturerReportRepository
			,FileStorageService fileStorageService,UserRepository userRepository,TenantManufactureRepository tenantManufactureRepository) {
		this.manufacturerReportRepository = manufacturerReportRepository;
		this.fileStorageService = fileStorageService;
		this.userRepository = userRepository;
		this.tenantManufactureRepository=tenantManufactureRepository;
	}

	@Override
	public List<ManufacturerReport> findAll() {

		return null;
	}

	public List<List<ManufactureReport>>  findAll(String manufacturerId,Integer month) {
		try {
			List<List<ManufactureReport>> oi = new ArrayList<>();
			
			int year=LocalDateTime.now().getYear();
			User us = getUser();
			if(us !=null) {
				
			
			List<ManufactureReport> o=	manufacturerReportRepository.getAllManufacturerReport(manufacturerId,us.getTenantId(),month,year);
			Map<String, List<ManufactureReport>> list = o.stream().collect(Collectors
					.groupingBy(ManufactureReport::getValue, LinkedHashMap::new, Collectors.toList()));	
			 oi.addAll(list.values());
			 return oi;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}
	
		public List<List<ManufactureReport>> getAllReportToManufacturer(String tenantId,Integer month) {
		List<List<ManufactureReport>> oi = new ArrayList<>();
		
		try {
			List<ManufactureReport> li  =new ArrayList<>();
			int year=LocalDateTime.now().getYear();
			
			User us = getUser();
			if(us !=null && us.getRoleName().equals(StringIteration.MANUFACTURERS)) {
				if (!tenantId.isEmpty()) {
					li = manufacturerReportRepository.getAllReportToManufacturerURL(us.getUserId(),tenantId,month,year);
				}else {
					li = manufacturerReportRepository.getAllReportToManufacturer(us.getUserId(),tenantId,month,year);
				}
				
				Map<String, List<ManufactureReport>> list = li.stream().collect(Collectors
						.groupingBy(ManufactureReport::getValue, LinkedHashMap::new, Collectors.toList()));	
				oi.addAll(list.values());
				return oi;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		return Collections.emptyList();
	}
	
	public static int getMonthFromDate(Date date) {
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.getMonthValue();
	}
	public List<ManuafctureTenantReport> getAllTenantReport(List<String> tenantId,String code) {
		try {
			User us = getUser();
			
	
			
			if(us !=null && us.getRoleName().equals(StringIteration.MANUFACTURERS) && !StringUtils.isNullOrEmpty(code)) {
				return manufacturerReportRepository.getAllTenantReport(us.getUserId(),tenantId,code);
			}else  if(us !=null && us.getRoleName().equals(StringIteration.MANUFACTURERS)&&StringUtils.isNullOrEmpty(code)) {
				System.err.println("code+"+code);
				return manufacturerReportRepository.getAllTenantReport(tenantId);
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}
	
	public List<ManuafctureTenantReport> findTenantByManufact() {
		try {
			User us = getUser();
			
	
			
			return manufacturerReportRepository.findTenantByManufacture(us.getUserId());
			
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyList();
	}
	
	
	
	private User getUser(){
		try {
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if(user.isPresent()) {
				return user.get();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	@Override
	public ManufacturerReport findById(String id) {

		return null;
	}

	@Override
	public Result<ManufacturerReport> create(ManufacturerReport t) {

		return null;
	}

	@Override
	public Result<ManufacturerReport> update(ManufacturerReport t) {

		return null;
	}

	@Override
	public Result<ManufacturerReport> delete(String id) {

		return null;
	}
	
	
	public Result<ManufacturerReport> create(MultipartFile file,String id,String manufacturerId,String fileName,Integer month) {
		Result<ManufacturerReport> result = new Result<>();
		try {
			User us = getUser();
			if(us !=null) {
				Result<FileResponse> s3 = fileStorageService.storeManufactureReportAws(file,month);
				
				if(s3 != null) {
					ManufacturerReport r = new ManufacturerReport();
					r.setId(CommonUtil.generateUniqueId());
					r.setManufacturerReportMappingId(id);
				      r.setReportFileName(fileName);
					r.setReportUrl(s3.getData().getUrl());
					CommonUtil.setCreatedOn(r);
					r.setMonth(month);
					r.setYear(LocalDateTime.now().getYear());
					r.setTenantId(us.getTenantId());
					r.setManufacturerId(manufacturerId);
					manufacturerReportRepository.save(r);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.SUCCESSFULLY_UPLOADED);
				}
			}

		} catch (Exception e) {
			logger.error(e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_UPLOADED);
		}
		return result;
	}

	public List<Object[]> listvalueManufacturer() {
	try {
		
		
		User us=getUser();
		if(us!=null&&us.getRoleName().equals(StringIteration.MANAGER)) {
			
		return	tenantManufactureRepository.listvalueManufacturer(us.getTenantId(),us.getUserId());
			
		}else if(us!=null&&us.getRoleName().equals(StringIteration.SUPERADMIN)) {
			return tenantManufactureRepository.listvalueManufacturerSuper(us.getTenantId());
		}else if(us!=null&&us.getRoleName().equals(StringIteration.USER)) {
			return	tenantManufactureRepository.listvalueManufacturer(us.getTenantId(),us.getHierarachyId());
		}
		
		
		
		
	} catch (Exception e) {
		logger.error(e);
	}
		return null;
	}

	public List<Object[]> tenantByManufacturer() {
		try {
			User us=getUser();
			
			if(us!=null&&us.getRoleName().equals(StringIteration.MANUFACTURERS)) {
				
				return tenantManufactureRepository.tenantByManufacturer(us.getUserId());
				
			}
			
			
		} catch (Exception e) {
		logger.error(e);
		}
		return null;
	}

	public Result<ManufacturerReport> update(MultipartFile file, String id,String fileName,Integer month) {
		Result<ManufacturerReport> result = new Result<>();
		try {
			
			User us = getUser();
			if(us !=null) {
			
				Result<FileResponse> s3 = fileStorageService.storeManufactureReportAws(file,month);
				if(s3 != null) {
					Optional<ManufacturerReport> mr =manufacturerReportRepository.findById(id);
					if(mr.isPresent()) {
					ManufacturerReport r=mr.get();
					r.setReportUrl(s3.getData().getUrl());
					r.setReportFileName(fileName);
					r.setMonth(month);
				
					manufacturerReportRepository.save(r);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.SUCCESSFULLY_UPLOADED);
				}}
			}

		} catch (Exception e) {
			logger.error(e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.ERROR_WHILE_UPLOADED);
		}
		return result;


}
}