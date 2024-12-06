package com.healthtraze.etraze.api.masters.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.documents.Country;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.documents.State;
import com.healthtraze.etraze.api.masters.dto.ChequeDTO;
import com.healthtraze.etraze.api.masters.dto.PaginationDTO;
import com.healthtraze.etraze.api.masters.dto.StockistImport;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.CountryRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.StateRepository;
import com.healthtraze.etraze.api.masters.repository.StockistManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.report.model.ChequeHistory;
import com.healthtraze.etraze.api.report.model.PaymentNotReceived;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class StockistService implements BaseService<Stockist, String> {

	private static final String PREFIX = "S";

	private Logger logger = LogManager.getLogger(StockistService.class);

	private final UserRepository userRepository;

	private final StockistRepository stockistRepository;

	private final TenantRepository tenantRepository;

	private final UserService userservice;
	private final ListValueRepository listvalue;

	private final StockistManufactureRepository stockistManufactureRepository;

	private final CountryRepository countryRepository;

	private final StateRepository stateRepository;

	private final CityRepository cityRepository;

	private final ManufacturerRepository manufacturerRepository;

	private final ListValueRepository listValueRepository;

	private final TenantManufactureRepository tenantManufactureRepository;

	private final FileStorageService fileStorageService;
	 @PersistenceContext
	    private EntityManager entityManagers;

	@Autowired
	public StockistService(TenantService tenantService, UserRepository userRepository,
			StockistRepository stockistRepository, EntityManager entityManager, TenantRepository tenantRepository,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate, UserService userservice,
			StockistManufactureRepository stockistManufactureRepository, CountryRepository countryRepository,
			StateRepository stateRepository, CityRepository cityRepository,
			ManufacturerRepository manufacturerRepository, ListValueRepository listValueRepository,
			TenantManufactureRepository tenantManufactureRepository, FileStorageService fileStorageService,ListValueRepository listvalue,
			EntityManager entityManagers) {
      this.entityManagers=entityManager;
		this.userRepository = userRepository;
		this.stockistRepository = stockistRepository;

		this.tenantRepository = tenantRepository;

		this.userservice = userservice;
		this.stockistManufactureRepository = stockistManufactureRepository;
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
		this.cityRepository = cityRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.listValueRepository = listValueRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.fileStorageService = fileStorageService;
		this.listvalue=listvalue;

	}

	@Override
	public List<Stockist> findAll() {

		Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
		if (user.isPresent()) {
			return stockistRepository.findAllTenants(user.get().getTenantId());
		}
		return Collections.emptyList();
	}

	public List<Stockist> findStockistByManufacturer(String userId) {
		User us = userRepository.getById(userId);
		if (us.getRoleName().equals(StringIteration.MANAGER)) {

			return stockistRepository.findStockistByManufacture(userId, us.getTenantId());
		} else if (us.getRoleName().equals(StringIteration.USER)) {
			return stockistRepository.findStockistByManufacture(us.getHierarachyId(), us.getTenantId());
		}
		return Collections.emptyList();

	}

	public List<Map<String, Object>> findStockistByManufacture() {

		List<Map<String, Object>> list = new ArrayList<>();
		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());

		if (u.isPresent()) {
			User us = u.get();
			List<Stockist> st = findStockistByManufacture(us);
			for (Stockist s : st) {
				Map<String, Object> map = new HashMap<>();
				map.put("stockistName", s.getStockistName());
				map.put("stockistId", s.getStockistId());
				if (s.getCityId() != null && s.getStateId() != null) {
					Optional<City> ct = cityRepository.findByCityCodeAndStateCode(s.getCityId(), s.getStateId());
					if (ct.isPresent()) {
						map.put(StringIteration.LOCATION, ct.get().getCityName());
					}
				}

				list.add(map);
			}

		}
		return list;

	}

	List<Stockist> findStockistByManufacture(User us) {
		try {
			if (us.getRoleName().equals(StringIteration.MANAGER)) {
				return stockistRepository.findStockistByManufacture(us.getUserId(), us.getTenantId());
			} else if (us.getRoleName().equals(StringIteration.USER)) {
				return stockistRepository.findStockistByManufacture(us.getHierarachyId(), us.getTenantId());
			} else if (us.getRoleName().equals(StringIteration.TRANSPORT)) {
				return stockistRepository.findStockistByTenantId(us.getTenantId());
			} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
				return stockistRepository.findStockistByTenantId(us.getTenantId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	public List<ChequeDTO> chequeDetailsReport(Map<String, String> params) {
		
		try {
			List<Object[]> ob = new ArrayList<>();
			String category = "";
			String stk = "";
			String manufacturer = "";
			String sapId = "";
			String location = "";
			String status = "";
			String stockistFilter = "";
			String locality = "";

			if (params.get(StringIteration.CATEGORY) != null) {
				category = params.get(StringIteration.CATEGORY);

			}

			if (params.get("isLocality") != null) {
				locality = params.get("isLocality");

			}
			if (params.get(StringIteration.ISSTOCKISTFILTER) != null) {
				stockistFilter = params.get(StringIteration.ISSTOCKISTFILTER);

			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stk = params.get(StringIteration.STOCKISTNAME).trim();
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}

			if (params.get(StringIteration.ISSECURITY) != null) {
				status = params.get(StringIteration.ISSECURITY);
			}

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}
			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();

				ob = chequeInHandReportObManager(ob, us, params, stockistFilter, status);
				ob = chequeInHandReportObUser(ob, us, params, stockistFilter, status);
				
				ob = chequeInHandReportObSuper(ob, us, params, stockistFilter, status);

			}
			List<ChequeDTO> ov = chequeInHandReportOb(ob);
			ov = chequeInHandReportOb(ov, locality, manufacturer, sapId, stk, category);

			if (!location.equals("")) {

				final String cities=location;
				

					ov = ov.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(cities))
							.collect(Collectors.toList());
				
			}

			return ov;

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private List<Object[]> chequeInHandReportObSuper(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter, String status) {
		if (!us.getRoleName().equals(StringIteration.SUPERADMIN)) {
			return ob;
		}

		String reportId = params.get(StringIteration.REPORTID);

		if (!reportId.equals(StringIteration.RP1004)) {
			return ob;
		}

		String tenantId = us.getTenantId();

		if (status.equals(StringIteration.INWARDCHEQUE)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportActiveAdvanceSuper(tenantId,  status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveInwardSuper(tenantId, status, stockistFilter);
			}
		} else if (status.equals(StringIteration.SECURITYCHEQUE)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportSecuritySuper(tenantId, status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveSecuritySuper(tenantId,  status, stockistFilter);
			}
		} 
		
		
		
		
		else if (status.equals("") && stockistFilter.equals(StringIteration.INACTIVE)) {
			ob = stockistRepository.chequeDetailsReportInActiveSuper(tenantId);
		} else if (status.equals("")
				&& (stockistFilter.equals("") || stockistFilter.equals(StringIteration.ACTIVE))) {
			ob = stockistRepository.chequeDetailsReportSuper(tenantId);
		}

		return ob;

	}

	private List<ChequeDTO> chequeInHandReportOb(List<ChequeDTO> ov, String locality, String manufacturer, String sapId,
			String stk, String category) {
		if (!locality.equals("")) {
			final String loc = locality;
			ov = ov.stream().filter(obj -> obj.getLocal().equalsIgnoreCase(loc)).collect(Collectors.toList());
		}
		if (!manufacturer.equals("")) {
			final String man = manufacturer;
			ov = ov.stream().filter(obj -> obj.getManufacture().equalsIgnoreCase(man)).collect(Collectors.toList());
		}
		if (!sapId.equals("")) {
			final String man = sapId.toLowerCase();
			ov = ov.stream().filter(obj -> obj.getSapId().toLowerCase().contains(man)).collect(Collectors.toList());
		}
		if (!stk.equals("")) {
			final String man = stk;
			ov = ov.stream().filter(obj -> obj.getStockistName().equalsIgnoreCase(man)).collect(Collectors.toList());
		}
		if (!category.equals("")) {
			final String cheque = category;
			ov = ov.stream().filter(obj -> obj.getCategory().equalsIgnoreCase(cheque)).collect(Collectors.toList());

		}

		return ov;
	}

	private List<ChequeDTO> chequeInHandReportOb(List<Object[]> ob) {
		List<ChequeDTO> ov = new ArrayList<>();
		for (Object[] b : ob) {
			ChequeDTO c = new ChequeDTO();
			c.setStockistName(String.valueOf(b[0]));
			c.setManufacture(String.valueOf(b[1]));
			c.setSapId(String.valueOf(b[2]));
			if(b[3]!=null) {
			c.setCategory(listvalue.findByCodeAndName(String.valueOf(b[3])));
			}else {
				c.setCategory(" -");
			}
			c.setTotal((BigInteger) b[4]);
			c.setLocation(String.valueOf(b[5]));

			c.setHold((BigInteger) (b[6]));
			if(b[7]!=null) {
			c.setLocal(listvalue.findByCodeAndName(String.valueOf(b[7])));
			}else {
				c.setLocal(" -");
			}
			Optional<String> city = cityRepository.findByCityCode(c.getLocation());
			if (city.isPresent()) {
				c.setLocation(city.get());
			}
			ov.add(c);
		}

		return ov;
	}

	private List<Object[]> chequeInHandReportObUser(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter, String status) {

		if (!us.getRoleName().equals("USER")) {
			return ob;
		}

		String reportId = params.get(StringIteration.REPORTID);

		if (!reportId.equals("RP1004")) {
			return ob;
		}

		String tenantId = us.getTenantId();
		String hierarchyId = us.getHierarachyId();

		if (status.equals(StringIteration.INWARDCHEQUE)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportActiveAdvance(tenantId, hierarchyId, status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveInward(tenantId, hierarchyId, status, stockistFilter);
			}
		} else if (status.equals("SECURITY")) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportSecurity(tenantId, hierarchyId, status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveSecurity(tenantId, hierarchyId, status, stockistFilter);
			}
		} else if (status.equals("") && stockistFilter.equals(StringIteration.INACTIVE)) {
			ob = stockistRepository.chequeDetailsReportInActive(tenantId, hierarchyId);
		} else if (status.equals("")
				&& (stockistFilter.equals("") || stockistFilter.equals(StringIteration.ACTIVE))) {
			ob = stockistRepository.chequeDetailsReport(tenantId, hierarchyId);
		}

		return ob;
	}

	private List<Object[]> chequeInHandReportObManager(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter, String status) {
		if (!us.getRoleName().equals(StringIteration.MANAGER)) {
			return ob;
		}

		String reportId = params.get(StringIteration.REPORTID);
		String tenantId = us.getTenantId();
		String userId = us.getUserId();

		if (!reportId.equals("RP1004")) {
			return ob;
		}

		if (status.equals(StringIteration.INWARDCHEQUE)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportActivesAdvance(tenantId, userId, status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveInward(tenantId, userId, status, stockistFilter);
			}
		} else if (status.equals("SECURITY")) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.chequeInHandReportSecurity(tenantId, userId, status);
			} else {
				ob = stockistRepository.chequeInHandReportActiveSecurity(tenantId, userId, status, stockistFilter);
			}
		} else if (status.equals("") && stockistFilter.equals(StringIteration.INACTIVE)) {
			ob = stockistRepository.chequeDetailsReportInActive(tenantId, userId);
		} else if (status.equals("") && (stockistFilter.equals("") || stockistFilter.equals(StringIteration.ACTIVE))) {
			ob = stockistRepository.chequeDetailsReport(tenantId, userId);
		}

		return ob;
	}

	
	public List<ChequeHistory> chequeStatusReport(Map<String, String> params) {
		try {
			
			List<ChequeHistory> obs=new ArrayList<>();
			String chequeNumber = "";
			String bankName = "";
			String chequeStatus = "";
			String location = "";
			String stockist = "";
            String locality="";
			String stockistFilter = "";
			String receiveDate = "";
			String depositeDate = "";
			String manufacturer = "";
			String category = "";
			String sapId = "";
			String invoice = "";
			String dtscndn = "";
			if (params.get("chequeStatus") != null) {
				chequeStatus = params.get("chequeStatus");
			}
			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME).trim();
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);

			}
			if (params.get("chequeNumber") != null) {
				chequeNumber = params.get("chequeNumber").trim();
			}
			if (params.get(StringIteration.BANKNAME) != null) {
				bankName = params.get(StringIteration.BANKNAME).trim();
			}
			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}

			if (params.get(StringIteration.ISSTOCKISTFILTER) != null) {
				stockistFilter = params.get(StringIteration.ISSTOCKISTFILTER);

			}
			if (params.get(StringIteration.CATEGORY) != null) {
				category = params.get(StringIteration.CATEGORY);

			}

			if (params.get("recieveDate") != null) {
				receiveDate = params.get("recieveDate");
			}
			if (params.get(StringIteration.DEPOSITEDATE) != null) {
				depositeDate = params.get(StringIteration.DEPOSITEDATE);
			}

			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoice = params.get(StringIteration.INVOICENUMBER).trim();
			}
			if (params.get("dtscndn") != null) {
				dtscndn = params.get("dtscndn").trim();
			}
			 if(params.get(StringIteration.IS_LOCALITY)!=null) {
					locality=params.get(StringIteration.IS_LOCALITY);
				}


			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				
				if(us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					
					if(stockistFilter.equals(StringIteration.INACTIVE))
					{
						obs=stockistRepository.chequeStatusReportSuperAdminInActive(us.getTenantId(),bankName, location, chequeNumber, chequeStatus);
					}else {
						obs=stockistRepository.chequeStatusReportSuperAdmin(us.getTenantId(),bankName, location, chequeNumber, chequeStatus);	
					}
					 
				}else if(us.getRoleName().equals(StringIteration.MANAGER)) {
					if(stockistFilter.equals(StringIteration.INACTIVE)) {
						
						obs=stockistRepository.chequeStatusReportManUserInActive(us.getTenantId(),us.getUserId(),bankName, location, chequeNumber, chequeStatus);
							
					}else {
						
						obs=stockistRepository.chequeStatusReportManUser(us.getTenantId(),us.getUserId(),bankName, location, chequeNumber, chequeStatus);
						
						
					}
					 
				}
				else if(us.getRoleName().equals(StringIteration.USER)) {
					
					if(stockistFilter.equals(StringIteration.INACTIVE)) {
						obs=stockistRepository.chequeStatusReportManUserInActive(us.getTenantId(),us.getHierarachyId(),bankName, location, chequeNumber, chequeStatus);
					}else {
						obs=stockistRepository.chequeStatusReportManUser(us.getTenantId(),us.getHierarachyId(),bankName, location, chequeNumber, chequeStatus);	

					}
				}
//				ob = chequeHistoryManager(us, ob, stockistFilter, bankName, location, chequeNumber, chequeStatus);
//				ob = chequeHistoryUser(us, ob, stockistFilter, bankName, location, chequeNumber, chequeStatus);
//				ob = chequeHistorySuper(us, ob, stockistFilter, bankName, location, chequeNumber, chequeStatus);
			}
			//List<ChequeDTO> ov = chequeHistoryOb(ob);
			obs = chequeHistoryObFilter(obs, stockist, manufacturer, receiveDate, depositeDate, category, sapId);
			if (!invoice.equals("")) {
				final String sap = invoice.toLowerCase();
				obs= obs.stream().filter(obj ->obj.getInvoice()!=null&& obj.getInvoice().toLowerCase().contains(sap))
						.collect(Collectors.toList());
			}
			if (!dtscndn.equals("")) {
				final String sap = dtscndn.toLowerCase();
				obs = obs.stream().filter(obj ->obj.getDtscndn()!=null&& obj.getDtscndn().toLowerCase().contains(sap))
						.collect(Collectors.toList());
			}
            if(!locality.equals("")) {
         	final String local =locality;
         
         	obs=obs.stream().filter(obj->obj.getDtscndn()!=null&&obj.getLocality().equalsIgnoreCase(local)).collect(Collectors.toList());
         }
     
			
			return obs;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}
		private List<ChequeHistory> chequeHistoryObFilter(List<ChequeHistory> ov, String stockist, String manufacturer,
			String receiveDate, String depositeDate, String category, String sapId) {

		if (!stockist.equals("")) {
			final String stk = stockist;
			ov = ov.stream().filter(obj -> obj.getStockist()!=null&& obj.getStockist().equalsIgnoreCase(stk)).collect(Collectors.toList());

		}
		if (!manufacturer.equals("")) {
			final String stk = manufacturer;
			System.err.println(stk+"stkstkstkstk");
			ov = ov.stream().filter(obj ->obj.getManufacture()!=null&& obj.getManufacture().equals(stk)).collect(Collectors.toList());

		}

		if (!receiveDate.equals("")) {
			final String receive = receiveDate;

			ov = ov.stream().filter(obj ->obj.getRecivedate()!=null&& obj.getRecivedate().toString().contains(receive)).collect(Collectors.toList());
		}
		if (!depositeDate.equals("")) {
			final String receive = depositeDate;

			ov = ov.stream().filter(obj ->obj.getDepositdate()!=null&& obj.getDepositdate().toString().contains(receive)).collect(Collectors.toList());
		}
		if (!category.equals("")) {
			final String cat = category;

			ov = ov.stream().filter(obj -> obj.getChequecategory()!=null&& obj.getChequecategory().equalsIgnoreCase(cat)).collect(Collectors.toList());
		}
		if (!sapId.equals("")) {
			final String cat = sapId.toLowerCase();

			ov = ov.stream().filter(obj ->obj.getSapid()!=null&& obj.getSapid().toLowerCase().contains(cat)).collect(Collectors.toList());
		}

		return ov;
	}
//	private List<ChequeDTO> chequeHistoryObFilter(List<ChequeDTO> ov, String stockist, String manufacturer,
//			String receiveDate, String depositeDate, String category, String sapId) {
//
//		if (!stockist.equals("")) {
//			final String stk = stockist;
//			ov = ov.stream().filter(obj -> obj.getStockistName().equalsIgnoreCase(stk)).collect(Collectors.toList());
//
//		}
//		if (!manufacturer.equals("")) {
//			final String stk = manufacturer;
//			ov = ov.stream().filter(obj -> obj.getManufacture().equalsIgnoreCase(stk)).collect(Collectors.toList());
//
//		}
//
//		if (!receiveDate.equals("")) {
//			final String receive = receiveDate;
//
//			ov = ov.stream().filter(obj -> obj.getReceiveDate().contains(receive)).collect(Collectors.toList());
//		}
//		if (!depositeDate.equals("")) {
//			final String receive = depositeDate;
//
//			ov = ov.stream().filter(obj -> obj.getDepositedate().contains(receive)).collect(Collectors.toList());
//		}
//		if (!category.equals("")) {
//			final String cat = category;
//
//			ov = ov.stream().filter(obj -> obj.getCategory().equalsIgnoreCase(cat)).collect(Collectors.toList());
//		}
//		if (!sapId.equals("")) {
//			final String cat = sapId.toLowerCase();
//
//			ov = ov.stream().filter(obj -> obj.getSapId().toLowerCase().contains(cat)).collect(Collectors.toList());
//		}
//
//		return ov;
//	}

	private String getStringValueWithDefault(Object obj, String defaultValue) {
		return obj != null ? String.valueOf(obj) : defaultValue;
	}

	private List<Object[]> chequeHistoryUser(User user, List<Object[]> ob, String stockistFilter, String bankName,
			String location, String chequeNumber, String chequeStatus) {
		if (!user.getRoleName().equals(StringIteration.USER)) {
			return ob;
		}

		if (stockistFilter.equals(StringIteration.ACTIVE)) {
			return handleActiveFilterForUser(user, bankName, location, chequeNumber, chequeStatus);
		} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
			return handleInactiveFilterForUser(user, bankName, location, chequeNumber, chequeStatus);
		} else {
			return handleNoFilterForUser(user, bankName, location, chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleActiveFilterForUser(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHand(user.getTenantId(), user.getHierarachyId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHold(user.getTenantId(), user.getHierarachyId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReport(user.getTenantId(), user.getHierarachyId(), bankName, location,
					chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleInactiveFilterForUser(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHandInActive(user.getTenantId(), user.getHierarachyId(),
					bankName, location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHoldInActive(user.getTenantId(), user.getHierarachyId(),
					bankName, location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReportInactive(user.getTenantId(), user.getHierarachyId(), bankName,
					location, chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleNoFilterForUser(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHand(user.getTenantId(), user.getHierarachyId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHold(user.getTenantId(), user.getHierarachyId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReport(user.getTenantId(), user.getHierarachyId(), bankName, location,
					chequeNumber, chequeStatus);
		}
	}
	
	private List<Object[]> chequeHistorySuper(User user, List<Object[]> ob, String stockistFilter, String bankName,
			String location, String chequeNumber, String chequeStatus) {
		if (!user.getRoleName().equals(StringIteration.SUPERADMIN)) {
			return ob;
		}

		if (stockistFilter.equals(StringIteration.ACTIVE)) {
			return handleActiveFilterForSuper(user, bankName, location, chequeNumber, chequeStatus);
		} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
			return handleInactiveFilterForSuper(user, bankName, location, chequeNumber, chequeStatus);
		} else {
			return handleActiveFilterForSuper(user, bankName, location, chequeNumber, chequeStatus);
		}
	}
		

	private List<Object[]> handleActiveFilterForSuper(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHandSuper(user.getTenantId(),  bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHoldSuper(user.getTenantId(),  bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReportSuper(user.getTenantId(), bankName, location,
					chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleInactiveFilterForSuper(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHandInActiveSuper(user.getTenantId(),
					bankName, location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHoldInActiveSuper(user.getTenantId(),
					bankName, location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReportInactiveSuper(user.getTenantId(),  bankName,
					location, chequeNumber, chequeStatus);
		}
	}



	

	private List<Object[]> chequeHistoryManager(User user, List<Object[]> ob, String stockistFilter, String bankName,
			String location, String chequeNumber, String chequeStatus) {
		if (!user.getRoleName().equals(StringIteration.MANAGER)) {
			return ob;
		}

		if (stockistFilter.equals(StringIteration.ACTIVE)) {
			return handleActiveFilter(user, bankName, location, chequeNumber, chequeStatus);
		} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
			return handleInactiveFilter(user, bankName, location, chequeNumber, chequeStatus);
		} else {
			return handleNoFilter(user, bankName, location, chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleActiveFilter(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHand(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHold(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReport(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleInactiveFilter(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHandInActive(user.getTenantId(), user.getUserId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHoldInActive(user.getTenantId(), user.getUserId(), bankName,
					location, chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReportInactive(user.getTenantId(), user.getUserId(), bankName,
					location, chequeNumber, chequeStatus);
		}
	}

	private List<Object[]> handleNoFilter(User user, String bankName, String location, String chequeNumber,
			String chequeStatus) {
		if (chequeStatus.equals(StringIteration.INWARDCHEQUE)) {
			return stockistRepository.chequeStatusReportInHand(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, StringIteration.INWARDCHEQUE);
		} else if (chequeStatus.equals("HOLD")) {
			return stockistRepository.chequeStatusReportOnHold(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, StringIteration.INWARDCHEQUE);
		} else {
			return stockistRepository.chequeStatusReport(user.getTenantId(), user.getUserId(), bankName, location,
					chequeNumber, chequeStatus);
		}
	}

	@Override
	public Stockist findById(String id) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			if (us.isPresent()) {
				User u = us.get();
				Optional<Stockist> option = stockistRepository.findById(id);
				if (option.isPresent()) {

					List<State> s = stateRepository.findAllByStateCode(option.get().getStateId());

					option.get().setStateName(s.get(0).getStateName());

					Optional<String> city = cityRepository.findByCityCode(option.get().getCityId().trim());
					if (city.isPresent()) {

						option.get().setCityName(city.get().trim());
					}

					option.get().setStockistManufacturesList(stockistManufactureRepository
							.findListByStockist(option.get().getStockistId(), u.getTenantId()));

					return option.get();
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public Stockist findstockistById(String id) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User uu = user.get();
				Optional<Stockist> option = stockistRepository.findById(id);
				if (option.isPresent()) {
					Stockist st = option.get();
					List<StockistManufacture> sm = stockistManufactureRepository.findListByStockist(id,
							uu.getTenantId());
					st.setStockistManufacturesList(sm);
					return st;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}


	public Stockist findStockistBySap(String id, String sap) {
	    try {
	        Optional<User> user = getUser();
	        if (user.isPresent()) {
	            User uu = user.get();
	            Optional<Stockist> option = stockistRepository.findById(id);
	            if (option.isPresent()) {
	                Stockist stk = option.get();
	                Optional<StockistManufacture> sm = findStockistManufactureBySap(sap, id, uu.getTenantId());
	                if (sm.isPresent()) {
	                    updateCityNameIfPresent(stk);
	                    updateManufactureName(sm.get());
	                    stk.setStockistManufacture(sm.get());
	                }
	                return stk;
	            }
	        }
	    } catch (Exception e) {
	        logger.error(e);
	    }
	    return null;
	}

	private Optional<User> getUser() {
	    return userRepository.findByUserId(SecurityUtil.getUserName());
	}

	private Optional<StockistManufacture> findStockistManufactureBySap(String sap, String id, String tenantId) {
	    return stockistManufactureRepository.findBySap(sap, id, tenantId);
	}

	private void updateCityNameIfPresent(Stockist stk) {
	    if (stk.getCityId() != null && stk.getStateId() != null) {
	        Optional<City> ct = cityRepository.findByCityCodeAndStateCode(stk.getCityId(), stk.getStateId());
	        ct.ifPresent(city -> stk.setCityName(city.getCityName()));
	    }
	}

	private void updateManufactureName(StockistManufacture smk) {
	    Optional<Manufacturer> man = manufacturerRepository.findById(smk.getManufacture());
	    man.ifPresent(manufacturer -> smk.setManufactureName(manufacturer.getManufacturerName()));
	}
	


	
	public String generateStockistId(Optional<Tenant> tenant) {
		try {
			if (tenant.isPresent()) {
				String tenantIdFormat = String.format("%s%s", tenant.get().getTenantCode(), PREFIX);
				BigInteger lastSequence = stockistRepository
						.getNextSequentialNumberForStockist(tenant.get().getTenantId());
				return lastSequence != null
						? String.format(StringIteration.SEQUENCENUMBER, tenantIdFormat,
								lastSequence.add(BigInteger.ONE))
						: String.format(StringIteration.SEQUENCENUMBER, tenantIdFormat, BigInteger.ONE);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return StringIteration.EMPTY_STRING;
	}

	@Override
	public Result<Stockist> create(Stockist stockist) {
		int count = 0;
		Result<Stockist> result = new Result<>();
		try {
			Optional<User> isuser = userRepository.findByUserId(SecurityUtil.getUserName());

			if (isuser.isPresent()) {
				User us = isuser.get();

				Optional<Tenant> tenant = tenantRepository.findById(us.getTenantId());
				if (tenant.isPresent() && tenant.get().getStatus().equals("ONBOARDED")) {
					result.setCode("1111");
					result.setMessage("Tenant Not Approved");
					return result;
				}

				Optional<Stockist> isexist = stockistRepository.findStockistBystockistName(us.getTenantId(),
						stockist.getStockistName().replace(" ", "").toLowerCase(), stockist.getCityId());

				if (isexist.isPresent()) {
					result.setCode(StringIteration.ERROR_CODE3);
					result.setMessage("This stockist is already exits in same city");
					return result;
				}

				stockist.setStatus("ACTIVE");
				stockist.setTenantId(us.getTenantId());
				CommonUtil.setCreatedOn(stockist);

				User user = new User();
				user.setFirstName(stockist.getStockistName());
				user.setEmail(stockist.getEmail());
				user.setPhoneNo(stockist.getMobile());
				user.setIsUserOnboarded(true);
				user.setNewUserValidateWeb(true);
				user.setOtpVerified(true);
				user.setStatus(StringIteration.ACTIVE);
				user.setOnline(true);
				user.setMobileChannel(true);
				user.setRoleId("2");
				user.setTenantId(us.getTenantId());

				String sequenceNumber = generateStockistId(tenant);
				stockist.setStockistId(sequenceNumber);
				user.setUserId(sequenceNumber);

				Result<User> isUserCreated = userservice.signUp(user);
				if (isUserCreated.getCode().equals("0000")) {
					List<StockistManufacture> manufactures = stockist.getStockistManufacturesList();
					for (StockistManufacture manufacture : manufactures) {
						manufacture.setId(System.currentTimeMillis() + (count++));
						manufacture.setTenantId(us.getTenantId());
						manufacture.setEnable(true);
						manufacture.setStockistId(stockist.getStockistId());
					}

					stockistRepository.save(stockist);

					result.setMessage("created");
					result.setCode("0000");
					return result;
				} else {
					result.setCode("1111");
					result.setMessage(isUserCreated.getMessage());
				}

			}

		} catch (Exception e) {
			logger.error(e);
			logger.info(e.getMessage());
			result.setMessage("Error while Creating User For Stockist");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	@Override
	public Result<Stockist> delete(String id) {
		Result<Stockist> result = new Result<>();
		try {
			stockistRepository.deleteById(id);
			result.setMessage("deleted");
			result.setCode("0000");
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	public Result<Stockist> update(Stockist t) {
		Result<Stockist> result = new Result<>();

		try {

			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			Optional<Stockist> optional = stockistRepository.findById(t.getStockistId());

			if (user.isPresent() && optional.isPresent()) {
				User us = user.get();
				Stockist cus = optional.get();
				cus.setStockistId(t.getStockistId());
				cus.setStockistName(t.getStockistName());
				cus.setFirstName(t.getFirstName());
				cus.setLastName(t.getLastName());
				cus.setEmail(t.getEmail());
				cus.setStatus(t.getStatus());
				cus.setCountry(t.getCountry());
				cus.setStateId(t.getStateId());
				cus.setCityId(t.getCityId());
				cus.setMobile(t.getMobile());
				cus.setGstNumber(t.getGstNumber());
				cus.setCinNumber(t.getCinNumber());
				cus.setTanNumber(t.getTanNumber());
				cus.setTinNumber(t.getTinNumber());
				cus.setPanNumber(t.getPanNumber());
				cus.setFssaiNumber(t.getFssaiNumber());
				cus.setFssaiExpiryDate(t.getFssaiExpiryDate());
				cus.setDrugLicenseNumber20B(t.getDrugLicenseNumber20B());
				cus.setDrugLicenseNumber21B(t.getDrugLicenseNumber21B());
				cus.setDrugLicenseExpiryDate(t.getDrugLicenseExpiryDate());
				cus.setCompanyRegisteredAddress(t.getCompanyRegisteredAddress());
				cus.setCompanyOwnerContact(t.getCompanyOwnerContact());
				cus.setCompanyOwnerEmailId(t.getCompanyOwnerEmailId());
				cus.setCompanyManagerContact(t.getCompanyManagerContact());
				cus.setCompanyManagerEmailId(t.getCompanyManagerEmailId());
				List<StockistManufacture> manufactures = t.getStockistManufacturesList();
				List<StockistManufacture> manf = new ArrayList<>();

				for (StockistManufacture manufacture : manufactures) {
					manufacture.setStockistId(t.getStockistId());
					Optional<StockistManufacture> sm = stockistManufactureRepository.findById(manufacture.getId());
					if (sm.isPresent()) {
						StockistManufacture s = sm.get();
						s.setStockistId(t.getStockistId());
						s.setManufacture(manufacture.getManufacture());
						s.setChequeCategory(manufacture.getChequeCategory());
						s.setThreshold(manufacture.getThreshold());
						s.setCreditDays(manufacture.getCreditDays());
						s.setLocation(manufacture.getLocation());
						s.setTlt(manufacture.getTlt());
						s.setSapId(manufacture.getSapId());
						s.setEnable(true);
						manf.add(s);
					} else {
						manufacture.setId(System.currentTimeMillis());
						manufacture.setEnable(true);
						manufacture.setTenantId(us.getTenantId());

						manf.add(manufacture);
					}

				}

				cus.setStockistManufacturesList(manf);
				stockistRepository.save(cus);
				result.setCode("0000");
				result.setMessage("Updated Successfully");
				result.setData(cus);
				return result;

			} else {
				result.setMessage("not able to update");
				result.setCode("1111");
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			result.setMessage(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public Result<Stockist> updateExcel(Stockist t) {
		Result<Stockist> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			Optional<Stockist> optional = stockistRepository.findById(t.getStockistId());
			if (us.isPresent() && optional.isPresent()) {
				User u = us.get();
				Stockist cus = optional.get();
				cus.setFirstName(t.getFirstName());
				cus.setLastName(t.getLastName());
				cus.setEmail(t.getEmail());
				cus.setMobile(t.getMobile());
				cus.setGstNumber(t.getGstNumber());
				cus.setCinNumber(t.getCinNumber());
				cus.setTanNumber(t.getTanNumber());
				cus.setTinNumber(t.getTinNumber());
				cus.setPanNumber(t.getPanNumber());
				cus.setFssaiNumber(t.getFssaiNumber());
				cus.setFssaiExpiryDate(t.getFssaiExpiryDate());
				cus.setDrugLicenseNumber20B(t.getDrugLicenseNumber20B());
				cus.setDrugLicenseNumber21B(t.getDrugLicenseNumber21B());
				cus.setDrugLicenseExpiryDate(t.getDrugLicenseExpiryDate());
				cus.setCompanyRegisteredAddress(t.getCompanyRegisteredAddress());
				cus.setCompanyOwnerContact(t.getCompanyOwnerContact());
				cus.setCompanyOwnerEmailId(t.getCompanyOwnerEmailId());
				cus.setCompanyManagerContact(t.getCompanyManagerContact());
				cus.setCompanyManagerEmailId(t.getCompanyManagerEmailId());
				cus.setPinCode(t.getPinCode());

				List<StockistManufacture> manufactures = t.getStockistManufacturesList();
				List<StockistManufacture> manf = new ArrayList<>();
				for (StockistManufacture manufacture : manufactures) {
					manufacture.setStockistId(t.getStockistId());
					Optional<StockistManufacture> sm = stockistManufactureRepository.findByManufacturer(
							manufacture.getManufacture(), manufacture.getStockistId(), u.getTenantId());
					if (sm.isPresent()) {
						StockistManufacture s = sm.get();
						s.setChequeCategory(manufacture.getChequeCategory());
						String value = listValueRepository.findByCodeAndValu(manufacture.getChequeCategory());
						s.setThreshold(value);
						s.setCreditDays(manufacture.getCreditDays());
						s.setLocation(manufacture.getLocation());
						s.setTlt(manufacture.getTlt());
						s.setEnable(true);
						manf.add(s);
					} else {
						if (duplicateSap(manufacture.getSapId(), manufacture.getStockistId(), u.getTenantId())) {
							result.setCode(StringIteration.ERROR_CODE2);
							return result;
						}
						manufacture.setId(System.currentTimeMillis());
						manufacture.setEnable(true);
						manufacture.setTenantId(u.getTenantId());
						manf.add(manufacture);
					}

				}

				cus.setStockistManufacturesList(manf);
				stockistRepository.save(cus);
				result.setCode("0000");
				result.setMessage("Updated Successfully");
				result.setData(cus);
				return result;

			} else {
				result.setMessage("not able to update");
				result.setCode("1111");
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			result.setMessage(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	boolean duplicateSap(String sapId, String stockistId, String tenantId) {
		Optional<StockistManufacture> duplicateSap = stockistManufactureRepository.findBySap(sapId, stockistId,
				tenantId);
		return duplicateSap.isPresent();
	}

	public void sortByAndSortDirection(PaginationDTO paging) {
		if (StringUtils.isNullOrEmpty(paging.getSortBy())) {
			paging.setSortBy(StringIteration.STOCKIST_ID);
		}
		if (StringUtils.isNullOrEmpty(paging.getSortDir())) {
			 paging.setSortDir(StringIteration.ASC);
		}
	}

	public List<StockistManufacture> setStockistManufacturerName(List<StockistManufacture> list) {
		for (StockistManufacture man : list) {
			Optional<Manufacturer> m = manufacturerRepository.findById(man.getManufacture());
			if (m.isPresent()) {
				man.setManufactureName(m.get().getManufacturerName());
			}
		}
		return list;
	}
	
	public void setStockistManufacturerName(StockistManufacture sm) {	
			Optional<Manufacturer> m = manufacturerRepository.findById(sm.getManufacture());
			if (m.isPresent()) {
				sm.setManufactureName(m.get().getManufacturerName());
			}
	}

	public void setcityName(Stockist s) {
		if (s.getCityId() != null && s.getStateId() != null) {

			Optional<City> ct = cityRepository.findByCityCodeAndStateCode(s.getCityId(), s.getStateId());
			if (ct.isPresent()) {
				s.setCityName(ct.get().getCityName());
			}
		}
	}

	public Result<Page<Stockist>> getAllStockist(PaginationDTO page) {
		Result<Page<Stockist>> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				
				sortByAndSortDirection(page);
				Pageable paging = PageRequest.of(page.getPage(),page.getSize(), Sort.by(Sort.Direction.fromString(page.getSortDir()),page.getSortBy()));
				Page<Stockist> ret = stockistRepository.findAllStockistByTenantId(paging, us.get().getTenantId(),
						page.getSearch());
//				int totalItems = stockistRepository.findAllStockistByTenantId(us.get().getTenantId(),page.getSearch()).size();
				for (Stockist s : ret.getContent()) {
					setcityName(s);
					s.setStockistManufacturesList(setStockistManufacturerName(stockistManufactureRepository
							.findListByStockist(s.getStockistId(), us.get().getTenantId())));
				}
				

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("sucess");
				result.setData(ret);

				return result;
			}

		} catch (Exception e) {
			logger.error("", e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public Boolean cityChange(String stockistName, String cityId, String tenantId) {
		try {
			Optional<Stockist> s = stockistRepository.findStockistBystockistName(tenantId, stockistName, cityId);
			return s.isPresent();
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	public List<ChequeDTO> chequeInHandReport(Map<String, String> params) {
		try {

			String stk = "";
			String manufacturer = "";
			String sapId = "";
			String location = "";
			String category = "";

			String stockistFilter = "";
			String locality="";
			List<Object[]> ob = new ArrayList<>();

			if (params.get(StringIteration.ISSTOCKISTFILTER) != null) {
				stockistFilter = params.get(StringIteration.ISSTOCKISTFILTER);

			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stk = params.get(StringIteration.STOCKISTNAME);
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}
			if (params.get(StringIteration.CATEGORY) != null) {
				category = params.get(StringIteration.CATEGORY);
			}

            if(params.get(StringIteration.IS_LOCALITY)!=null) {
					locality=params.get(StringIteration.IS_LOCALITY);
				}
            int months=0;
			if(params.get("selectedMonth") != null) {
				 months = Integer.parseInt(params.get("selectedMonth"));
			    }
	

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();

				ob = chequeInHandObManger(ob, us, params, stockistFilter,months);
				ob = chequeInHandObUser(ob, us, params, stockistFilter,months);
				ob = chequeInHandObsuper(ob, us, params, stockistFilter,months);
			}

			List<ChequeDTO> ov = chequeInHandOv(ob);
			ov = chequeInHandOvFilter(ov, stk, manufacturer, sapId, category);

			if (!location.equals("")) {

				final String city=location;
			

					ov = ov.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(city))
							.collect(Collectors.toList());
				
			}
			  if(!locality.equals("")) {
	              	final String local =locality;
	              
	              	ov=ov.stream().filter(obj->obj.getLocal().equalsIgnoreCase(local)).collect(Collectors.toList());
	              }
	          

			return ov;
			

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private List<Object[]> chequeInHandObsuper(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter,int month) {
		
		if (!us.getRoleName().equals(StringIteration.SUPERADMIN)) {
			return ob;
	
		}

		String reportId = params.get(StringIteration.REPORTID);
		if (reportId == null) {
			return ob;
		}
		

		switch (reportId) {	
		case StringIteration.RP4011:
			
			if(month!=0) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.thresholdLimitSuperMonth(us.getTenantId(),month);
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.thresholdLimitSuperMonth(us.getTenantId(),month);
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.thresholdLimitInActiveSuperMonth(us.getTenantId(),month);
			}
			}
			else {
				
				if (stockistFilter.equals("")) {
					ob = stockistRepository.thresholdLimitSuper(us.getTenantId());
				} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
					ob = stockistRepository.thresholdLimitSuper(us.getTenantId());
				} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
					ob = stockistRepository.thresholdLimitInActiveSuper(us.getTenantId());
				}
			}
			break;
		
		case StringIteration.RP4012:
			  if(month!=0) {
		
			if (stockistFilter.equals("")) {
				ob = stockistRepository.noSecuritChequesSuperMonth(us.getTenantId(),month);
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.noSecuritChequesSuperMonth(us.getTenantId(),month);
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.noSecuritChequesInactiveSuperMonth(us.getTenantId(),month);
			}
			
			  }
			  else {
				
				  if (stockistFilter.equals("")) {
						ob = stockistRepository.noSecuritChequesSuper(us.getTenantId());
					} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
						ob = stockistRepository.noSecuritChequesSuper(us.getTenantId());
					} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
						ob = stockistRepository.noSecuritChequesInactiveSuper(us.getTenantId());
					}
					
			}
			break;
		default:
			throw new IllegalArgumentException(reportId);
		}

		return ob;
		
	}

	private List<ChequeDTO> chequeInHandOvFilter(List<ChequeDTO> ov, String stk, String manufacturer, String sapId,
			String category) {
		if (!stk.equals("")) {
			final String stockist = stk;
			ov = ov.stream().filter(obj -> obj.getStockistName().equalsIgnoreCase(stockist))
					.collect(Collectors.toList());

		}
		if (!manufacturer.equals("")) {
			final String man = manufacturer;
			ov = ov.stream().filter(obj -> obj.getManufacture().equalsIgnoreCase(man)).collect(Collectors.toList());
		}
		if (!sapId.equals("")) {
			final String sap = sapId.toLowerCase();
			ov = ov.stream().filter(obj -> obj.getSapId().toLowerCase().contains(sap)).collect(Collectors.toList());
		}
		if (!category.equals("")) {
			final String sap = category;
			ov = ov.stream().filter(obj -> obj.getCategory().equalsIgnoreCase(sap)).collect(Collectors.toList());
		}

		return ov;
	}

	private List<ChequeDTO> chequeInHandOv(List<Object[]> ob) {
		List<ChequeDTO> ov = new ArrayList<>();
		for (Object[] b : ob) {
			ChequeDTO c = new ChequeDTO();
			c.setStockistName(String.valueOf(b[0]));
			c.setManufacture(String.valueOf(b[1]));
			c.setSapId(String.valueOf(b[2]));
			if(b[3]!=null) {
			c.setCategory(listvalue.findByCodeAndName(String.valueOf(b[3])));
			}else {
				c.setCategory(" ");
			}
			c.setTotal((BigInteger) b[4]);
			c.setLocation(String.valueOf(b[5]));
			c.setLocal(listvalue.findByCodeAndName(String.valueOf(b[6])));

			Optional<String> city = cityRepository.findByCityCode(c.getLocation());
			if (city.isPresent()) {
				c.setLocation(city.get());
			}

			ov.add(c);

		}

		return ov;
	}

	private List<Object[]> chequeInHandObUser(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter,int month) {
		if (!us.getRoleName().equals(StringIteration.USER)) {
			return ob;
		}

		String reportId = params.get(StringIteration.REPORTID);
		if (reportId == null) {
			return ob;
		}

		switch (reportId) {
		case "RP4014":
			ob = stockistRepository.chequeInHandReportInActiveStockist(us.getTenantId(), us.getHierarachyId());
			break;
		case "RP4011":
		     if(month!=0) {
					if (stockistFilter.equals("")) {
						ob = stockistRepository.thresholdLimitMonth(us.getTenantId(), us.getHierarachyId(),month);
					} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
						ob = stockistRepository.thresholdLimitMonth(us.getTenantId(), us.getHierarachyId(),month);
					} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
						ob = stockistRepository.thresholdLimitInActiveMonth(us.getTenantId(), us.getHierarachyId(),month);
					}
					
					}
					else {
						if (stockistFilter.equals("")) {
							ob = stockistRepository.thresholdLimit(us.getTenantId(), us.getHierarachyId());
						} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
							ob = stockistRepository.thresholdLimit(us.getTenantId(), us.getHierarachyId());
						} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
							ob = stockistRepository.thresholdLimitInActive(us.getTenantId(), us.getHierarachyId());
						}	
						
					}
					
			break;
		case "RP4013":
			if (stockistFilter.equals("")) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getHierarachyId());
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getHierarachyId());
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.securityTooutWardInActive(us.getTenantId(), us.getHierarachyId());
			}
			break;
		case "RP4012":
			if(month!=0) {
		if (stockistFilter.equals("")) {
			ob = stockistRepository.noSecuritChequesMonth(us.getTenantId(), us.getHierarachyId(),month);
		} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
			ob = stockistRepository.noSecuritChequesMonth(us.getTenantId(), us.getHierarachyId(),month);
		} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
			ob = stockistRepository.noSecuritChequesInactiveMonth(us.getTenantId(), us.getHierarachyId(),month);
		}
		}else {
			
			if (stockistFilter.equals("")) {
				ob = stockistRepository.noSecuritCheques(us.getTenantId(), us.getHierarachyId());
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.noSecuritCheques(us.getTenantId(), us.getHierarachyId());
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.noSecuritChequesInactive(us.getTenantId(), us.getHierarachyId());
			}
			}
			break;
		default:
			throw new IllegalArgumentException(reportId);
		}

		return ob;

	}

	private List<Object[]> chequeInHandObManger(List<Object[]> ob, User us, Map<String, String> params,
			String stockistFilter,int month) {
		if (!us.getRoleName().equals(StringIteration.MANAGER)) {
			return ob;
		}

		String reportId = params.get(StringIteration.REPORTID);
		if (reportId == null) {
			return ob;
		}

		switch (reportId) {
		case "RP4014":
			ob = stockistRepository.chequeInHandReportInActiveStockist(us.getTenantId(), us.getUserId());
			break;
		case "RP4011":
			
			if(month!=0) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.thresholdLimitMonth(us.getTenantId(), us.getUserId(),month);
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.thresholdLimitMonth(us.getTenantId(), us.getUserId(),month);
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.thresholdLimitInActiveMonth(us.getTenantId(), us.getUserId(),month);
			}
			
			}
			else {
				if (stockistFilter.equals("")) {
					ob = stockistRepository.thresholdLimit(us.getTenantId(), us.getUserId());
				} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
					ob = stockistRepository.thresholdLimit(us.getTenantId(), us.getUserId());
				} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
					ob = stockistRepository.thresholdLimitInActive(us.getTenantId(), us.getUserId());
				}	
				
			}
			break;
		case "RP4013":
			if (stockistFilter.equals("")) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getUserId());
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getUserId());
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.securityTooutWardInActive(us.getTenantId(), us.getUserId());
			}
			break;
		case "RP4012":
			
			if(month!=0) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.noSecuritChequesMonth(us.getTenantId(), us.getUserId(),month);
			} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.noSecuritChequesMonth(us.getTenantId(), us.getUserId(),month);
			} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.noSecuritChequesInactiveMonth(us.getTenantId(), us.getUserId(),month);
			}
			}else {
				
				if (stockistFilter.equals("")) {
					ob = stockistRepository.noSecuritCheques(us.getTenantId(), us.getUserId());
				} else if (stockistFilter.equals(StringIteration.ACTIVE)) {
					ob = stockistRepository.noSecuritCheques(us.getTenantId(), us.getUserId());
				} else if (stockistFilter.equals(StringIteration.INACTIVE)) {
					ob = stockistRepository.noSecuritChequesInactive(us.getTenantId(), us.getUserId());
				}

				
			}
			break;
		default:
			throw new IllegalArgumentException(reportId);
		}

		return ob;
	}

	public Result<Stockist> createExcel(Stockist stockist) {
		Result<Stockist> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<Tenant> tenant = tenantRepository.findById(u.getTenantId());
				if (tenant.isPresent() && tenant.get().getStatus().equals("ONBOARDED")) {
					result.setCode(StringIteration.ERROR_CODE10);
					result.setMessage("Tenant Not Approved");
					return result;
				}

				User user = new User();
				user.setFirstName(stockist.getStockistName());
				user.setEmail(stockist.getEmail());
				user.setPhoneNo(stockist.getMobile());
				user.setIsUserOnboarded(true);
				user.setNewUserValidateWeb(true);
				user.setOtpVerified(true);
				user.setStatus(StringIteration.ACTIVE_STATUS);
				user.setOnline(true);
				user.setMobileChannel(true);
				user.setRoleId("2");
				user.setTenantId(u.getTenantId());

				stockist.setTenantId(u.getTenantId());
				stockist.setStatus(StringIteration.ACTIVE_STATUS);

				String sequenceNumber = generateStockistId(tenant);
				stockist.setStockistId(sequenceNumber);
				user.setUserId(sequenceNumber);

				Result<User> isUserCreated = userservice.signUp(user);
				if (isUserCreated.getCode().equals("0000")) {
					List<StockistManufacture> manufactures = stockist.getStockistManufacturesList();

					for (StockistManufacture manufacture : manufactures) {

						manufacture.setId(System.currentTimeMillis());
						manufacture.setStockistId(sequenceNumber);
						manufacture.setEnable(true);
						manufacture.setTenantId(u.getTenantId());
						if (duplicateSap(manufacture.getSapId(), manufacture.getStockistId(), u.getTenantId())) {
							result.setCode(StringIteration.ERROR_CODE2);
							return result;
						}
					}
					Stockist newStockist = stockistRepository.save(stockist);

					result.setData(newStockist);
					result.setMessage("created");
					result.setCode("0000");
					return result;
				} else {
					result.setCode("1111");
					result.setMessage(isUserCreated.getMessage());
				}
			}
		} catch (Exception e) {

			logger.error(e);
			logger.info(e.getMessage());
			result.setMessage("Error while Creating User For Stockist");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	void prepareStockist(Row row, Stockist stockist) {
		try {
			prepareFirstName(stockist,row);
			prepareLastName(stockist, row);
			prepareCin(stockist,row);
			preparePan(stockist,row);
			prepareTin(stockist,row);
			prepareTan(stockist,row);
			prepareGst(stockist,row);
			prepareFssai(stockist,row);
			prepareFssaiExpiri(stockist,row);
			prepareLicence20B(stockist,row);
			prepareLicence21B(stockist,row);
			prepareLicanceExpiri(stockist,row);
			prepareManagerContact(stockist,row);
			prepareManagerEmail(stockist,row);
			prepareOwnerContact(stockist,row);
			prepareOwnerEmail(stockist,row);
			prepareRegisterAddress(stockist,row);
			preparePinCode(stockist,row);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
			
			
	void prepareFirstName(Stockist stockist, Row row) {
	    try {
	        Cell firstName = row.getCell(0); 
	        if (firstName != null && firstName.getCellType() != CellType.BLANK) {
	        String fName = firstName.getStringCellValue().trim();
	        stockist.setFirstName(fName);
	        }
	    } catch (Exception e) {
	        logger.error(e.getMessage());
	    }
	}


            void prepareLastName(Stockist stockist,Row row) {
            	try {
			Cell lastName = row.getCell(2);
			 if (lastName != null && lastName.getCellType() != CellType.BLANK) {
			String lName = lastName.getStringCellValue().trim();
			stockist.setLastName(lName);
            }
            	 } catch (Exception e) {
         	        logger.error(e.getMessage());
         	    }
         	}
			 
            
            
            void prepareCin(Stockist stockist, Row row) {
            	try {
                Cell cinCell = row.getCell(14);
                if (cinCell != null && cinCell.getCellType() != CellType.BLANK) {
                    if (cinCell.getCellType() == CellType.NUMERIC) {
                        long numericValue = (long) cinCell.getNumericCellValue();
                        stockist.setCinNumber(String.valueOf(numericValue));
                    } else if (cinCell.getCellType() == CellType.STRING) {
                        String cinNumber = cinCell.getStringCellValue();
                        stockist.setCinNumber(cinNumber);
                    }
                }
            	 } catch (Exception e) {
                     logger.error(e.getMessage());
                 }
            }


            void preparePan(Stockist stockist, Row row) {
            	try {
                Cell panCell = row.getCell(15);
                if (panCell != null && panCell.getCellType() != CellType.BLANK) {
                    if (panCell.getCellType() == CellType.NUMERIC) {
                        long numericValue = (long) panCell.getNumericCellValue();
                        stockist.setPanNumber(String.valueOf(numericValue));
                    } else if (panCell.getCellType() == CellType.STRING) {
                        String stringValue = panCell.getStringCellValue();
                        stockist.setPanNumber(stringValue);
                    }
                }
            	 } catch (Exception e) {
                     logger.error(e.getMessage());
                 }
            }


            void prepareTin(Stockist stockist, Row row) {
                try {
                    Cell tinCell = row.getCell(16);
                    if (tinCell != null && tinCell.getCellType() != CellType.BLANK) {
                        if (tinCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) tinCell.getNumericCellValue();
                            stockist.setTinNumber(String.valueOf(numericValue));
                        } else if (tinCell.getCellType() == CellType.STRING) {
                            String stringValue = tinCell.getStringCellValue();
                            stockist.setTinNumber(stringValue);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            
            void prepareTan(Stockist stockist, Row row) {
            	try {
			Cell tanCell = row.getCell(17);
			 if (tanCell != null && tanCell.getCellType() != CellType.BLANK) {
			if (tanCell.getCellType() == CellType.NUMERIC) {
				long numericValue = (long) tanCell.getNumericCellValue();
				stockist.setTanNumber(String.valueOf(numericValue));
			} else if (tanCell.getCellType() == CellType.STRING) {
				String stringValue = tanCell.getStringCellValue();
				stockist.setTanNumber(stringValue);
			}
		}
            	 } catch (Exception e) {
                     logger.error(e.getMessage());
                 }
            }
			
			
            void prepareGst(Stockist stockist, Row row) {
                try {
                    Cell gstCell = row.getCell(18);
                    if (gstCell != null && gstCell.getCellType() != CellType.BLANK) {
                        if (gstCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) gstCell.getNumericCellValue();
                            stockist.setGstNumber(String.valueOf(numericValue));
                        } else if (gstCell.getCellType() == CellType.STRING) {
                            String stringValue = gstCell.getStringCellValue();
                            stockist.setGstNumber(stringValue);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            

            void prepareFssai(Stockist stockist, Row row) { 
                try {
                    Cell fssaiCell = row.getCell(19);
                    if (fssaiCell != null && fssaiCell.getCellType() != CellType.BLANK) {
                        if (fssaiCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) fssaiCell.getNumericCellValue();
                            stockist.setFssaiNumber(String.valueOf(numericValue));
                        } else if (fssaiCell.getCellType() == CellType.STRING) {
                            String stringValue = fssaiCell.getStringCellValue();
                            stockist.setFssaiNumber(stringValue);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            
            
            void prepareFssaiExpiri(Stockist stockist, Row row) { 
                try {
                    Cell fssaiExpiriCell = row.getCell(20);
                    if (fssaiExpiriCell != null && fssaiExpiriCell.getCellType() != CellType.BLANK) {
                        if (fssaiExpiriCell.getCellType() == CellType.STRING) {
                            String fssaiNumber = fssaiExpiriCell.getStringCellValue().trim();
                            if (containsDateFormat(fssaiNumber)) {
                                Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(fssaiNumber);
                                stockist.setFssaiExpiryDate(date);
                            }
                        } else if (fssaiExpiriCell.getCellType() == CellType.NUMERIC) {
                            String numericValue = String.valueOf(fssaiExpiriCell.getNumericCellValue());
                            if (containsDateFormat(numericValue)) {
                                Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(numericValue);
                                stockist.setFssaiExpiryDate(date);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            
            
            void prepareLicence20B(Stockist stockist, Row row) { 
                try {
                    Cell licence20BCell = row.getCell(22);
                    if (licence20BCell != null && licence20BCell.getCellType() != CellType.BLANK) {
                        if (licence20BCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) licence20BCell.getNumericCellValue();
                            stockist.setDrugLicenseNumber20B(String.valueOf(numericValue));
                        } else if (licence20BCell.getCellType() == CellType.STRING) {
                            String stringValue = licence20BCell.getStringCellValue();
                            stockist.setDrugLicenseNumber20B(stringValue);
                        }
                    }	
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }


            
            void prepareLicence21B(Stockist stockist, Row row) { 
                try {
                    Cell licence21BCell = row.getCell(23);
                    if (licence21BCell != null && licence21BCell.getCellType() != CellType.BLANK) {
                        if (licence21BCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) licence21BCell.getNumericCellValue();
                            stockist.setDrugLicenseNumber21B(String.valueOf(numericValue));
                        } else if (licence21BCell.getCellType() == CellType.STRING) {
                            String stringValue = licence21BCell.getStringCellValue();
                            stockist.setDrugLicenseNumber21B(stringValue);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }


            void prepareLicanceExpiri(Stockist stockist, Row row) { 
                try {
                    Cell licanceExpiriCell = row.getCell(24);
                    if (licanceExpiriCell != null && licanceExpiriCell.getCellType() != CellType.BLANK) {
                        if (licanceExpiriCell.getCellType() == CellType.STRING) {
                            String licanceExpiriDate = licanceExpiriCell.getStringCellValue().trim();
                            if (containsDateFormat(licanceExpiriDate)) {
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(licanceExpiriDate);
                                stockist.setDrugLicenseExpiryDate(date);
                            }
                        } else if (licanceExpiriCell.getCellType() == CellType.NUMERIC
                                && DateUtil.isCellDateFormatted(licanceExpiriCell)) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String cellValue = dateFormat.format(licanceExpiriCell.getDateCellValue());
                            stockist.setDrugLicenseExpiryDate(dateFormat.parse(cellValue));
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            
            
            void prepareManagerContact(Stockist stockist, Row row) { 
            	try {
			Cell managerContactCell = row.getCell(25);
			if (managerContactCell != null && managerContactCell.getCellType() != CellType.BLANK) {
			if (managerContactCell.getCellType() == CellType.NUMERIC) {
				double numericValue = managerContactCell.getNumericCellValue();
				stockist.setCompanyManagerContact(String.valueOf(numericValue));
			} else if (managerContactCell.getCellType() == CellType.STRING) {
				String stringValue = managerContactCell.getStringCellValue();
				stockist.setCompanyManagerContact(stringValue);
			}
			}
            	}catch (Exception e) {
        			logger.error(e.getMessage());
        		}
           }
            
            void prepareManagerEmail(Stockist stockist, Row row) { 
            	try {
                Cell managerEmailCell = row.getCell(26);
                if (managerEmailCell != null && managerEmailCell.getCellType() != CellType.BLANK) {
                    String managerEmail = managerEmailCell.getStringCellValue().trim();
                    stockist.setCompanyManagerEmailId(managerEmail);
                }
            	}catch (Exception e) {
        			logger.error(e.getMessage());
        		}
            }


            void prepareOwnerContact(Stockist stockist, Row row) { 
            	try {
                Cell ownerContactCell = row.getCell(27);
                if (ownerContactCell != null && ownerContactCell.getCellType() != CellType.BLANK) {
                    if (ownerContactCell.getCellType() == CellType.NUMERIC) {
                        long numericValue = (long) ownerContactCell.getNumericCellValue();
                        stockist.setCompanyOwnerContact(String.valueOf(numericValue));
                    } else if (ownerContactCell.getCellType() == CellType.STRING) {
                        String stringValue = ownerContactCell.getStringCellValue();
                        stockist.setCompanyOwnerContact(stringValue);
                    }
                }
            	}catch (Exception e) {
        			logger.error(e.getMessage());
        		}
            }
            

            void prepareOwnerEmail(Stockist stockist, Row row) { 
                try {
                    Cell ownerEmailCell = row.getCell(28);	
                    if (ownerEmailCell != null && ownerEmailCell.getCellType() != CellType.BLANK) {
                        String ownerEmail = ownerEmailCell.getStringCellValue().trim();
                        stockist.setCompanyOwnerEmailId(ownerEmail);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }


			

            void prepareRegisterAddress(Stockist stockist, Row row) {
                try {
                    Cell registerAddressCell = row.getCell(29);
                    if (registerAddressCell != null && registerAddressCell.getCellType() != CellType.BLANK) {
                        if (registerAddressCell.getCellType() == CellType.NUMERIC) {
                            long numericValue = (long) registerAddressCell.getNumericCellValue();
                            stockist.setCompanyRegisteredAddress(String.valueOf(numericValue));
                        } else if (registerAddressCell.getCellType() == CellType.STRING) {
                            String registorAssress = registerAddressCell.getStringCellValue().trim();
                            stockist.setCompanyRegisteredAddress(registorAssress);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }


			void preparePinCode(Stockist stockist, Row row) {
			    try {
			        Cell pincodeCell = row.getCell(30);
			        if (pincodeCell != null && pincodeCell.getCellType() != CellType.BLANK) {
			            if (pincodeCell.getCellType() == CellType.NUMERIC) {
			                long numericValue = (long) pincodeCell.getNumericCellValue();
			                stockist.setPinCode(String.valueOf(numericValue));
			            } else if (pincodeCell.getCellType() == CellType.STRING) {
			                String stringValue = pincodeCell.getStringCellValue();
			                stockist.setPinCode(stringValue);
			            }
			        }
			    } catch (Exception e) {
			        logger.error(e.getMessage());
			    }
			}



	void prapareStckistName(StockistImport importValue) {
		try {
			Row row = importValue.getRow();
			Cell stockistName = row.getCell(0);
			if (stockistName == null || stockistName.getCellType() == CellType.BLANK
					|| stockistName.getStringCellValue().trim().isEmpty()) {
				importValue.setFlag(false);
				importValue.setMessage("StockistName can not be empty");
			} else {
				String name = stockistName.getStringCellValue().trim();
				importValue.getStockist().setStockistName(name);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistEmail(StockistImport importValue) {
		try {
			Cell emailCell = importValue.getRow().getCell(3);
			if (emailCell == null || emailCell.getCellType() == CellType.BLANK) {
				importValue.setFlag(false);
				importValue.setMessage(" Email can not be empty");
			} else {
				String email = emailCell.getStringCellValue().trim();
				importValue.getStockist().setEmail(email);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistMobile(StockistImport importValue) {
		try {
			Cell mobileCell = importValue.getRow().getCell(4);
			if (mobileCell == null || mobileCell.getCellType() == CellType.BLANK) {
				importValue.setFlag(false);
				importValue.setMessage(" Mobile number can not be empty");
			} else {
				if (mobileCell.getCellType() == CellType.NUMERIC) {
					long numericValue = (long) mobileCell.getNumericCellValue();
					importValue.getStockist().setMobile(String.valueOf(numericValue));
				} else if (mobileCell.getCellType() == CellType.STRING) {
					String mobile = mobileCell.getStringCellValue().trim();
					importValue.getStockist().setMobile(mobile);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistCountry(StockistImport importValue) {
		try {
			Cell countrycell = importValue.getRow().getCell(5);
			if (countrycell == null || countrycell.getCellType() == CellType.BLANK
					|| countrycell.getStringCellValue().trim().isEmpty()) {
				importValue.setFlag(false);
				importValue.setMessage(" Country can not be empty");
			} else {
				String country = countrycell.getStringCellValue();
				importValue.getStockist().setCountry(country);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistState(StockistImport importValue) {
		try {
			Cell stateCell = importValue.getRow().getCell(6);
			if (stateCell == null || stateCell.getStringCellValue().trim().isEmpty()
					|| stateCell.getCellType() == CellType.BLANK) {
				importValue.setMessage(" State can not be empty");
				importValue.setFlag(false);
			} else {
				String state = stateCell.getStringCellValue();
				importValue.getStockist().setStateId(state);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistLocation(StockistImport importValue) {
		try {
			Cell locationCell = importValue.getRow().getCell(7);
			if (locationCell == null || locationCell.getCellType() == CellType.BLANK
					|| locationCell.getStringCellValue().trim().isEmpty()) {
				importValue.setMessage(" Location can not be empty");
				importValue.setFlag(false);
			} else {
				String city = locationCell.getStringCellValue();
				importValue.getStockist().setCityId(city);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistManufacturer(StockistImport importValue) {
		try {
			Cell manufacturerCell = importValue.getRow().getCell(8);
			if (manufacturerCell == null || manufacturerCell.getCellType() == CellType.BLANK
					|| manufacturerCell.getStringCellValue().trim().isEmpty()) {
				importValue.setMessage(" Manufacture can not be empty");
				importValue.setFlag(false);
			} else {
				String manufacturer = manufacturerCell.getStringCellValue();
				importValue.getStockistManufacture().setManufacture(manufacturer);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistSap(StockistImport importValue) {
		try {
			Cell sapCell = importValue.getRow().getCell(9);
			if (sapCell == null || sapCell.getCellType() == CellType.BLANK) {
				importValue.setMessage(" SAPID cannot be empty");
				importValue.setFlag(false);
			} else {
				if (sapCell.getCellType() == CellType.NUMERIC) {
					long numericValue = (long) sapCell.getNumericCellValue();
					importValue.getStockist().getStockistManufacture().setSapId(String.valueOf(numericValue));
				} else if (sapCell.getCellType() == CellType.STRING) {
					String sapId = sapCell.getStringCellValue();
					importValue.getStockistManufacture().setSapId(sapId);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistCredit(StockistImport importValue) {
		try {
			Cell creditdays = importValue.getRow().getCell(10);

			if (creditdays == null || creditdays.getCellType() == CellType.BLANK) {
				importValue.setMessage(" CreditDays can not be empty");
				importValue.setFlag(false);
			} else {
				if (creditdays.getCellType() == CellType.NUMERIC) {
					long numericValue = (long) creditdays.getNumericCellValue();
					importValue.getStockist().getStockistManufacture().setCreditDays(String.valueOf(numericValue));
				} else if (creditdays.getCellType() == CellType.STRING) {
					String cdays = creditdays.getStringCellValue();
					importValue.getStockistManufacture().setCreditDays(cdays);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	

	void prapareStckistTlt(StockistImport importValue) {
		try {
			Cell tltCell = importValue.getRow().getCell(11);
			if (tltCell == null || tltCell.getCellType() == CellType.BLANK) {
				importValue.setMessage(" TLT can not be empty");
				importValue.setFlag(false);
			} else {
				if (tltCell.getCellType() == CellType.NUMERIC) {
					long numericValue = (long) tltCell.getNumericCellValue();
					importValue.getStockist().getStockistManufacture().setTlt(String.valueOf(numericValue));
				} else if (tltCell.getCellType() == CellType.STRING) {
					String tlt = tltCell.getStringCellValue();
					importValue.getStockistManufacture().setTlt(tlt);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistLocality(StockistImport importValue) {
		try {
			Cell localityCell = importValue.getRow().getCell(12);
			if (localityCell == null || localityCell.getCellType() == CellType.BLANK
					|| localityCell.getStringCellValue().trim().isEmpty()) {
				importValue.setMessage(" Locality can not be empty");
				importValue.setFlag(false);
			} else {
				String local = localityCell.getStringCellValue().toUpperCase();
				importValue.getStockistManufacture().setLocation(local);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareStckistCheque(StockistImport importValue) {
		try {
			Cell chequeCell = importValue.getRow().getCell(13);
			if (chequeCell == null || chequeCell.getCellType() == CellType.BLANK
					|| chequeCell.getStringCellValue().trim().isEmpty()) {
				importValue.setMessage(" Cheque Category can not be empty");
				importValue.setFlag(false);
			} else {
				String cheque = chequeCell.getStringCellValue().trim().toUpperCase();
				importValue.getStockistManufacture().setChequeCategory(cheque);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prapareMandantryField(StockistImport importValue) {
		try {
			prapareStckistName(importValue);
			prapareStckistEmail(importValue);
			prapareStckistMobile(importValue);
			prapareStckistCountry(importValue);
			prapareStckistState(importValue);
			prapareStckistLocation(importValue);
			
			prapareStckistManufacturer(importValue);
			prapareStckistSap(importValue);
			prapareStckistCredit(importValue);
			prapareStckistTlt(importValue);
			prapareStckistLocality(importValue);
			prapareStckistCheque(importValue);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Result<Map<String, Object>> fileReader(MultipartFile file, String sheetIndex) throws IOException {
		Result<Map<String, Object>> result = new Result<>();
		List<Stockist> invalidStockists = new ArrayList<>();
		List<Stockist> valid = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();

		try (FileInputStream fileInputStream = new FileInputStream(convertMultiPartToFile(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

			XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			if (us.isPresent()) {
				if (sheet.getLastRowNum() < 1) {
					result.setCode(StringIteration.ERROR_CODE3);
					result.setMessage("No data found in the Excel sheet");
					return result;
				}
				int rowIndex = 0;
				for (Row row : sheet) {
					Stockist stockist = new Stockist();
					CommonUtil.setCreatedOn(stockist);
					if (rowIndex == 0) {
						rowIndex++;
						continue;
					}
					
					StockistManufacture  sm = new StockistManufacture();
					StockistImport smi = new StockistImport(stockist, true, row,sm);
					prepareStockist(row, stockist);
					prapareMandantryField(smi);
					stockist.setStockistManufacturesList(Arrays.asList(smi.getStockistManufacture()));
					if (smi.isFlag()) {
						valid.add(stockist);
					} else {
						stockist.setRemarks(smi.getMessage());
						invalidStockists.add(stockist);
					}
					rowIndex++;
				}
				StockistCreationFromExcel(valid, invalidStockists, result, us.get());
				writeInvalidStockist(map, invalidStockists, result);

				return result;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE6);
			result.setMessage(e.getMessage());

		}
		return result;
	}

	void StockistCreationFromExcel(List<Stockist> valid, List<Stockist> invalidStockists,
			Result<Map<String, Object>> result, User us) {
		try {

			for (Stockist stockist : valid) {
				StockistManufacture stockistManufacture = stockist.getStockistManufacturesList().get(0);
				Optional<Country> country = countryRepository
						.findByCountryNameOrCode(stockist.getCountry().replace(" ", "").toLowerCase());
				if (country.isPresent()) {
					Optional<State> state = stateRepository.findByStateNameWithStatusAndCountry(
							stockist.getStateId().toLowerCase().replace(" ", ""), country.get().getCountryCode());
					if (state.isPresent()) {
						Optional<City> loc = cityRepository.findByCityNameWithStateCodeAndStatus(
								stockist.getCityId().toLowerCase().replace(" ", ""), state.get().getStateCode());
						if (loc.isPresent()) {
							Optional<Manufacturer> manufacturer = manufacturerRepository
									.findByManufacturerNameAndStatus(
											stockistManufacture.getManufacture().replace(" ", "").toLowerCase());
							if (manufacturer.isPresent()) {
								Optional<TenantManufacture> tm = tenantManufactureRepository
										.findByManufactureIdAndTenantAndStatus(manufacturer.get().getManufacturerId(),
												us.getTenantId());
								if (tm.isPresent()) {

									String loca = stockistManufacture.getLocation().replace(" ", "").toLowerCase();
									String cat = stockistManufacture.getChequeCategory().replace(" ", "").toLowerCase();

									Optional<String> locality = listValueRepository.findByNameAndCode("700", loca);
									Optional<String> category = listValueRepository.findByNameAndCode("200", cat);
									if (locality.isPresent() && category.isPresent()) {
										stockistManufacture.setLocation(locality.get());
										stockistManufacture.setChequeCategory(category.get());
										stockistManufacture.setManufacture(tm.get().getManufacturerId());
										stockist.setStockistManufacturesList(Arrays.asList(stockistManufacture));

										stockist.setCountry(country.get().getCountryCode());
										stockist.setStateId(state.get().getStateCode());
										stockist.setCityId(loc.get().getCityCode());

										Optional<Stockist> exisList = stockistRepository.findByNameAndCityPresent(
												stockist.getStockistName().replace(" ", "").toLowerCase(),
												stockist.getCityId(), us.getTenantId());
										if (exisList.isPresent()) {
											stockist.setStockistId(exisList.get().getStockistId());

											Result<Stockist> stk = updateExcel(stockist);
											if (stk.getCode().equals(StringIteration.ERROR_CODE2)) {

												stockist.setRemarks(stockist.getRemarks() + " Sap Id Already Exist");
												invalidStockists.add(stockist);
											}
										} else {

											Result<Stockist> stk = createExcel(stockist);
											if (stk.getCode().equals(StringIteration.ERROR_CODE10)) {
												result.setCode(StringIteration.ERROR_CODE10);
												result.setMessage(stk.getMessage());
												break;
											} else if (stk.getCode().equals(StringIteration.ERROR_CODE2)) {

												stockist.setRemarks(stockist.getRemarks() + " Sap Id Already Exist");
												invalidStockists.add(stockist);
											}
										}
									} else if (!locality.isPresent()) {

										stockist.setRemarks(
												stockist.getRemarks() + " Locality can be LOCAL or OUTSTATION");
										invalidStockists.add(stockist);
									} else if (!category.isPresent()) {

										stockist.setRemarks(stockist.getRemarks() + " Check Cheque Category Entered");
										invalidStockists.add(stockist);
									}
								} else {
									stockist.setRemarks(stockist.getRemarks()
											+ " Manfacturer not mapped with this Tenant OR manufacture is not activated for this tenant");
									invalidStockists.add(stockist);
								}
							} else {

								stockist.setRemarks(stockist.getRemarks() + " Check the Manufacturer Entered");
								invalidStockists.add(stockist);
							}
						} else {
							stockist.setRemarks(stockist.getRemarks() + " Check City Entered");
							invalidStockists.add(stockist);
						}
					} else {
						stockist.setRemarks(stockist.getRemarks() + " Check State Entered");
						invalidStockists.add(stockist);
					}
				} else {
					stockist.setRemarks(stockist.getRemarks() + " Check Country Entered Country");
					invalidStockists.add(stockist);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void writeInvalidStockist(Map<String, Object> map, List<Stockist> invalidStockists,
			Result<Map<String, Object>> result) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()){
			if (!invalidStockists.isEmpty()) {
				XSSFSheet spreadsheet = workbook.createSheet(" Stockist download Excel ");
				int rowid = 0;
				XSSFRow row;
				row = spreadsheet.createRow(rowid++);
				int cellId = 0;
				for (String header : StringIteration.HEADERS_LIST) {
					row.createCell(cellId).setCellValue(header);
					cellId++;
				}
				for (Stockist s : invalidStockists) {
					row = spreadsheet.createRow(rowid++);
					StockistImport imp = new StockistImport(s, row);
					prepareInvalidSheet(imp);
				}
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				workbook.write(byteArrayOutputStream);
				InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
				String fileId = UUID.randomUUID().toString();
				com.healthtraze.etraze.api.file.model.File invalidStockistFile = fileStorageService.uploadFileToAWS(
						fileId, fileId, byteArrayInputStream, "InvalidStockistData.xlsx", "document", 0);
				map.put("invalidStockistFile", invalidStockistFile);
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("");
				result.setData(map);
			} else {
				result.setCode("0000");
				result.setMessage("Data saved successfully");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static boolean containsDateFormat(String input) {
		String datePattern = "\\b\\d{2}-\\d{2}-\\d{4}\\b";
		Pattern pattern = Pattern.compile(datePattern);
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}

	void prepareInvalidSheet(StockistImport imports) {
		Row row = imports.getRow();
		Stockist s = imports.getStockist();
		row.createCell(0).setCellValue(s.getStockistName());
		row.createCell(1).setCellValue(s.getFirstName());
		row.createCell(2).setCellValue(s.getLastName());
		row.createCell(3).setCellValue(s.getEmail());
		row.createCell(4).setCellValue(s.getMobile());
		row.createCell(5).setCellValue(s.getCountry());
		row.createCell(6).setCellValue(s.getStateId());
		row.createCell(7).setCellValue(s.getCityId());

		StockistManufacture sm = s.getStockistManufacturesList().get(0);

		row.createCell(8).setCellValue(sm.getManufacture());
		row.createCell(9).setCellValue(sm.getSapId());
		row.createCell(10).setCellValue(sm.getCreditDays());
		row.createCell(11).setCellValue(sm.getTlt());
		row.createCell(12).setCellValue(sm.getLocation());
		row.createCell(13).setCellValue(sm.getChequeCategory());

		row.createCell(14).setCellValue(s.getCinNumber());
		row.createCell(15).setCellValue(s.getPanNumber());
		row.createCell(16).setCellValue(s.getTinNumber());
		row.createCell(17).setCellValue(s.getTanNumber());
		row.createCell(18).setCellValue(s.getGstNumber());
		row.createCell(19).setCellValue(s.getFssaiNumber());

		SimpleDateFormat date = new SimpleDateFormat(StringIteration.DDMMYY);
		String cellValue = StringIteration.EMPTY_STRING;
		if (s.getFssaiExpiryDate() != null) {
			cellValue = date.format(s.getFssaiExpiryDate());
		}
		row.createCell(20).setCellValue(String.valueOf(cellValue));
		row.createCell(22).setCellValue(s.getDrugLicenseNumber20B());
		row.createCell(23).setCellValue(s.getDrugLicenseNumber21B());
		String cellValu = StringIteration.EMPTY_STRING;
		if (s.getFssaiExpiryDate() != null) {
			cellValu = date.format(s.getDrugLicenseExpiryDate());
		}
		row.createCell(24).setCellValue(String.valueOf(cellValu));
		row.createCell(25).setCellValue(s.getCompanyManagerContact());
		row.createCell(26).setCellValue(s.getCompanyManagerEmailId());
		row.createCell(27).setCellValue(s.getCompanyOwnerContact());
		row.createCell(28).setCellValue(s.getCompanyOwnerEmailId());
		row.createCell(29).setCellValue(s.getCompanyRegisteredAddress());
		row.createCell(30).setCellValue(s.getPinCode());
		row.createCell(31).setCellValue(s.getRemarks());

	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(file.getBytes());
		}
		return convFile;
	}

	public List<ChequeDTO> chequeDiffrentBank(Map<String, String> params) {

		try {
			String stockistFilter = "";
			String stk = "";
			String manufacturer = "";
			String location = "";
			String sapId = "";
			String bankName = "";
			String category = "";
			String chequeNumber = "";
			String locality="";
			if (params.get("isStockistFilter") != null) {
				stockistFilter = params.get("isStockistFilter");

			}
			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stk = params.get(StringIteration.STOCKISTNAME).trim();
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}

			if (params.get("bankName") != null) {
				bankName = params.get("bankName");
			}
			if (params.get(StringIteration.CATEGORY) != null) {
				category = params.get(StringIteration.CATEGORY);
			}
			if (params.get(StringIteration.CHEQUENUMBER) != null) {
				chequeNumber = params.get(StringIteration.CHEQUENUMBER).trim();
			}

            if(params.get(StringIteration.IS_LOCALITY)!=null) {
					locality=params.get(StringIteration.IS_LOCALITY);
				}


			List<Object[]> ob = new ArrayList<>();

			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				ob = chequeIndiffrentBankUser(us, stockistFilter, ob);
			}
			List<ChequeDTO> ov = chequeIndiffrentBankOb(ob);
			ov = chequeIndiffrentBankOvFilter(ov, stk, manufacturer, sapId, chequeNumber, bankName, category);

			if (!location.equals("")) {

				final String city=location;
				

					ov = ov.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(city))
							.collect(Collectors.toList());
			}
			  if(!locality.equals("")) {
	              	final String local =locality;
	              
	              	ov=ov.stream().filter(obj->obj.getLocal().equalsIgnoreCase(local)).collect(Collectors.toList());
	              }

			return ov;
		} catch (Exception exc) {
			logger.error(exc);

		}
		return new ArrayList<>();
	}

	private List<ChequeDTO> chequeIndiffrentBankOvFilter(List<ChequeDTO> ov, String stk, String manufacturer,
			String sapId, String chequeNumber, String bankName, String category) {

		if (!stk.equals("")) {
			final String stockist = stk;
			ov = ov.stream().filter(obj -> obj.getStockistName().equalsIgnoreCase(stockist))
					.collect(Collectors.toList());

		}
		if (!manufacturer.equals("")) {
			final String man = manufacturer;
			ov = ov.stream().filter(obj -> obj.getManufacture().equalsIgnoreCase(man)).collect(Collectors.toList());
		}
		if (!sapId.equals("")) {
			final String sap = sapId;
			ov = ov.stream().filter(obj -> obj.getSapId().toLowerCase().contains(sap)).collect(Collectors.toList());
		}
		if (!chequeNumber.equals("")) {
			final String sap = chequeNumber;
			ov = ov.stream().filter(obj -> obj.getChequeNumber().toLowerCase().contains(sap))
					.collect(Collectors.toList());
		}

		if (!bankName.equals("")) {
			final String sap = bankName;
			ov = ov.stream().filter(obj -> obj.getBankName().equalsIgnoreCase(sap)).collect(Collectors.toList());
		}
		if (!category.equals("")) {
			final String sap = category;
			ov = ov.stream().filter(obj -> obj.getCategory().equalsIgnoreCase(sap)).collect(Collectors.toList());
		}

		return ov;
	}

	private List<ChequeDTO> chequeIndiffrentBankOb(List<Object[]> ob) {
		List<ChequeDTO> ov = new ArrayList<>();
		for (Object[] b : ob) {
			ChequeDTO c = new ChequeDTO();
			c.setStockistName(String.valueOf(b[0]));
			c.setManufacture(String.valueOf(b[1]));
			c.setSapId(String.valueOf(b[2]));
			c.setChequeNumber(String.valueOf(b[4]));
			if(b[3]!=null) {
			c.setCategory(listvalue.findByCodeAndName(String.valueOf(b[3])));
			}else {
				c.setCategory("-");
			}
			c.setTotal((BigInteger) b[5]);
			c.setLocation(String.valueOf(b[6]));
			c.setBankName(String.valueOf(b[7]));
            if(b[8]!=null) {
			c.setStatus(String.valueOf(b[8]));
            }else {
            	c.setStatus(" -");
            }
			c.setLocal(listvalue.findByCodeAndName(String.valueOf(b[9])));

			Optional<String> city = cityRepository.findByCityCode(c.getLocation());
			if (city.isPresent()) {
				c.setLocation(city.get());
			}

			ov.add(c);

		}

		return ov;
	}

	private List<Object[]> chequeIndiffrentBankUser(User us, String stockistFilter, List<Object[]> ob) {
		if (us.getRoleName().equals(StringIteration.MANAGER)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getUserId());
			}
			if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getUserId());
			}
			if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.securityTooutWardInActive(us.getTenantId(), us.getUserId());
			}
		}

		if (us.getRoleName().equals(StringIteration.USER)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getHierarachyId());
			}
			if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.securityTooutWard(us.getTenantId(), us.getHierarachyId());
			}
			if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.securityTooutWardInActive(us.getTenantId(), us.getHierarachyId());
			}
		}
		
		
		if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
			if (stockistFilter.equals("")) {
				ob = stockistRepository.securityTooutWardSuper(us.getTenantId());
			}
			if (stockistFilter.equals(StringIteration.ACTIVE)) {
				ob = stockistRepository.securityTooutWardSuper(us.getTenantId());
			}
			if (stockistFilter.equals(StringIteration.INACTIVE)) {
				ob = stockistRepository.securityTooutWardInActiveSuper(us.getTenantId());
			}
		}

		return ob;
	}
	
	@SuppressWarnings("unchecked")
	public List<PaymentNotReceived> paymentReceived(Map<String, String> map) {
	 
			try {
				List<Object[]> ob = new ArrayList<>();
				List<PaymentNotReceived> obs=new ArrayList<>();;
	 
				String invoice = "";
				String invoiceDate = "";
				String stockist = "";
				String manufacturer = "";
				String deliverdDate = "";
				String depositeDate = "";
				String locality="";
				String city="";
				if (map.get("invoiceNumber") != null) {
	 
					invoice = map.get("invoiceNumber").trim();
	 
				}
				if (map.get("invoiceDate") != null) {
					invoiceDate = map.get("invoiceDate");
				}
				if (map.get("manufacturer") != null) {
					manufacturer = map.get("manufacturer");
				}
				if (map.get("depositeDate") != null) {
					depositeDate = map.get("depositeDate");
				}
				if (map.get("stockist") != null) {
					stockist = map.get("stockist");
				}
				if (map.get("deliverdDate") != null) {
					deliverdDate = map.get("deliverdDate");
				}
				
				if(map.get("isLocality")!=null) {
					locality=map.get("isLocality");
				}
				if(map.get("location")!=null) {
					city=map.get("location");
				}
                int months=0;
				  if(map.get("selectedMonth") != null) {
					 months = Integer.parseInt(map.get("selectedMonth"));
				    }

				
				Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
				if (us.isPresent()) {
					int year=LocalDateTime.now().getYear();
					User user = us.get();
					if (user.getRoleName().equals(StringIteration.MANAGER)) {
	                        if(months!=0) {
	                        	
	                        	Query query=entityManagers.createNativeQuery("select * from paymentnotreceived where tenantid=:tenantId and user_id=:userId and deliverydate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND deliverydate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month')   ", PaymentNotReceived.class);
	        					query.setParameter(StringIteration.TENANTID,user.getTenantId() );
	        					query.setParameter(StringIteration.USERID, user.getUserId());
	        					query.setParameter(StringIteration.MONTH, months);
	        					query.setParameter("year", year);
	        					obs=  query.getResultList();
	                        	
	                        	
	                        	
	                        	
	                        }else {
	                        	Query query=entityManagers.createNativeQuery("select * from paymentnotreceived where tenantid=:tenantId and user_id=:userId  ", PaymentNotReceived.class);
	        					query.setParameter(StringIteration.TENANTID,user.getTenantId() );
	        					query.setParameter(StringIteration.USERID, user.getUserId());
	        					
	        					obs=  query.getResultList();
	                        	
	                        	
	                        }
						
	 
					} else if (user.getRoleName().equals(StringIteration.USER)) {
						  if(months!=0) {
							  Query query=entityManagers.createNativeQuery("select * from paymentnotreceived where tenantid=:tenantId and user_id=:userId and deliverydate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND deliverydate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month')   ", PaymentNotReceived.class);
	        					query.setParameter(StringIteration.TENANTID,user.getTenantId() );
	        					query.setParameter(StringIteration.USERID, user.getHierarachyId());
	        					query.setParameter(StringIteration.MONTH, months);
	        					query.setParameter("year", year);
	        					obs=  query.getResultList();
	                        	
	                        }else {
	                        	Query query=entityManagers.createNativeQuery("select * from paymentnotreceived where tenantid=:tenantId and user_id=:userId  ", PaymentNotReceived.class);
	        					query.setParameter(StringIteration.TENANTID,user.getTenantId() );
	        					query.setParameter(StringIteration.USERID, user.getHierarachyId());
	        					obs=  query.getResultList();
	                        	
	                        	
	                        }
						
					}
					else if (user.getRoleName().equals(StringIteration.SUPERADMIN)) {
						
						if(months!=0) {
							Query query=entityManagers.createNativeQuery("select * from paymentnotreceivedsuper where tenantid=:tenantId and deliverydate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND deliverydate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month')   ", PaymentNotReceived.class);
        					query.setParameter(StringIteration.TENANTID,user.getTenantId());
        					query.setParameter(StringIteration.MONTH,months);
        					query.setParameter("year", year);
        					
        					obs=  query.getResultList();
							//ob = stockistRepository.paymentReceivedSuper(user.getTenantId());
						}
						else {
						Query query=entityManagers.createNativeQuery("select * from paymentnotreceivedsuper where tenantid=:tenantId ", PaymentNotReceived.class);
    					query.setParameter(StringIteration.TENANTID,user.getTenantId() );
    					obs=  query.getResultList();
						}
					}
					
				}
				
				
				for(PaymentNotReceived payment:obs) {
				
				if(payment.getLocality()!=null) {
					payment.setLocality(listvalue.findByCodeAndName(payment.getLocality()));
				
					}
				}
				
				//ov=paymentReceivedOb(ob);
							obs=paymentReceivedFilter(obs,invoice,invoiceDate,stockist,manufacturer,deliverdDate,depositeDate);
							obs=paymentReceivedFilterLocation(obs,city,locality);
				
				return obs;
			} catch (Exception e) {
				e.printStackTrace();
			}
	 
			return Collections.emptyList();
		}
	 
		private List<PaymentNotReceived> paymentReceivedFilterLocation(List<PaymentNotReceived> ov, String cities, String locality) {
	
			if (!cities.equals("")) {

				ov = ov.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(cities))
							.collect(Collectors.toList());
				
			}
			if(!locality.equals("")) {
				ov=ov.stream().filter(obj->obj.getLocality().equalsIgnoreCase(locality)).collect(Collectors.toList());
				
			}
			
						
		return ov;
	}

		private List<ChequeDTO> paymentReceivedOb(List<Object[]> ob) {
			try {
			List<ChequeDTO> ov =new ArrayList<>();
			for (Object[] b : ob) {
				ChequeDTO ch = new ChequeDTO();
				ch.setInvoice(String.valueOf(b[0]));
				if (b[1] != null) {
					ch.setInvoiceDate(new SimpleDateFormat(StringIteration.YYYYMMDD)
							.format(new SimpleDateFormat(StringIteration.YYYYMMDD).parse(String.valueOf(b[1]))));
				} else {
					ch.setInvoiceDate(" -");
				}
				ch.setStockistName(String.valueOf(b[2]));
				ch.setManufacture(String.valueOf(b[3]));
				
				if (b[4] != null) {
					ch.setDeliveryDate(new SimpleDateFormat(StringIteration.YYYYMMDD)
							.format(new SimpleDateFormat(StringIteration.YYYYMMDD).parse(String.valueOf(b[4]))));
				} else {
					ch.setDeliveryDate(" -");
				}
				if (b[5] != null) {
					ch.setDueDate(new SimpleDateFormat("yyyy-MM-dd")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(b[5]))));
				} else {
					ch.setDueDate(" -");
				}
				ch.setRemarks(b[6].toString());
				if (b[7] != null) {
					ch.setDaysTaken(b[7].toString());
				} else {
					ch.setDaysTaken(" -");
				}
				
				if(b[8]!=null) {
					ch.setLocal(listvalue.findByCodeAndName(String.valueOf(b[8])));
						}
				
				else {
					ch.setLocal("");
					}
				if(b[9]!=null) {
					
					Optional<String> ct=cityRepository.findByCityCode(String.valueOf(b[9]));
					if(ct.isPresent()) {
					ch.setLocation(ct.get());
					}
				}else {
					ch.setLocation(" -");
				}
				
				ov.add(ch);
			}
	 
			return ov;
			}
			catch(Exception e) {
				logger.error(e);
			}
			return Collections.emptyList();
		}
	 
		private List<PaymentNotReceived> paymentReceivedFilter(List<PaymentNotReceived> ov, String invoice, String invoiceDate, String stockist,
				String manufacturer, String deliverdDate, String depositeDate) {
			if (!invoice.equals("")) {
				final String sap = invoice.toLowerCase();
				ov = ov.stream().filter(obj ->obj.getInvoicenumber()!=null&& obj.getInvoicenumber().toLowerCase().contains(sap))
						.collect(Collectors.toList());
			}
			if (!invoiceDate.equals("")) {
				final String sap = invoiceDate;
				ov = ov.stream().filter(obj ->obj.getInvoicedate()!=null&& obj.getInvoicedate().toString().equalsIgnoreCase(sap)).collect(Collectors.toList());
			}
			if (!stockist.equals("")) {
				final String sap = stockist;
				ov = ov.stream().filter(obj ->obj.getStockist()!=null&& obj.getStockist().equalsIgnoreCase(sap))
						.collect(Collectors.toList());
			}
			if (!manufacturer.equals("")) {
				final String sap = manufacturer;
				ov = ov.stream().filter(obj -> obj.getManufacturename()!=null&& obj.getManufacturename().equalsIgnoreCase(sap)).collect(Collectors.toList());
			}
			if (!deliverdDate.equals("")) {
				final String sap = deliverdDate;
				ov = ov.stream().filter(obj ->obj.getDeliverydate()!=null&& obj.getDeliverydate().toString().equalsIgnoreCase(sap))
						.collect(Collectors.toList());
			}
			if (!depositeDate.equals("")) {
				final String sap = depositeDate;
				ov = ov.stream().filter(obj ->obj.getDuedate()!=null&& obj.getDuedate().toString().equalsIgnoreCase(sap))
						.collect(Collectors.toList());
			}
	 
			return ov;
		}

		public List<Object> stockisMnaufacture(String userId) {
			try {
				
			List<Object> ob =new ArrayList<>();
			Optional<User> us=userRepository.findById(SecurityUtil.getUserName());
			
			if(us.isPresent()) {
			List<Object[]> b=stockistRepository.stockisMnaufacture(us.get().getTenantId(),userId);
			
			for (Object[] c:b) {
				ChequeDTO ch=new ChequeDTO();
				ch.setStockistId(String.valueOf(c[0]));
				ch.setStockistName(String.valueOf(c[1]));
	           ch.setLocation(String.valueOf(c[2]));
	           if(ch.getLocation()!=null){
	        	   Optional<String> ct=cityRepository.findByCityCode(ch.getLocation());
	        	   if(ct.isPresent()) {
	        	   ch.setLocation(ct.get());
	           }}
	           ch.setCode(String.valueOf(c[2]));
	          
				ob.add(ch);
			}
				}
			
			return ob;
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			return Collections.emptyList();
		}
	 
	
}
