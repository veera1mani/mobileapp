package com.healthtraze.etraze.api.masters.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.BooleanResult;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.service.NotificationService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.ClaimsReturnsReportDTO;
import com.healthtraze.etraze.api.masters.dto.CnImport;
import com.healthtraze.etraze.api.masters.dto.ReturnDetails;
import com.healthtraze.etraze.api.masters.dto.ReturnQrDTO;
import com.healthtraze.etraze.api.masters.model.Claim;
import com.healthtraze.etraze.api.masters.model.Return;
import com.healthtraze.etraze.api.masters.model.ReturnNote;
import com.healthtraze.etraze.api.masters.model.ReturnStatusHistory;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.ClaimRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManagerManufacturerMappingRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnAttachmentRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnNoteRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnStatusHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;
import com.healthtraze.etraze.api.security.service.NotificationTemplateService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class ReturnService implements BaseService<Return, String> {

	private static final String PREFIX = "C";

	private static final String RETURN_PREFIX = "R";

	private Logger logger = LogManager.getLogger(ReturnService.class);

	private final ReturnRepository returnRepository;

	private final ClaimRepository claimRepository;
	private final ListValueRepository listvalue;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ManufacturerRepository manufacturerRepository;

	private final ReturnStatusHistoryRepository returnStatusHistoryRepository;

	private final EmailTemplateService emailTemplateService;

	private final EmailService emailService;

	private final NotificationTemplateService notificationTemplateService;

	private final NotificationService notificationService;

	private final ReturnNoteRepository returnNoteRepository;

	private final StockistRepository stockistRepository;

	private final CityRepository cityRepository;

	private final TenantRepository tenantRepository;

	private final TenantManufactureRepository tenantManufactureRepository;

	private final TransportRepository transportRepository;

	private final FileStorageService fileStorageService;

	private final ReturnNoteService returnNoteService;

	@Autowired
	public ReturnService(ReturnRepository returnRepository, ClaimRepository claimRepository,
			UserRepository userRepository, RoleRepository roleRepository, ManufacturerRepository manufacturerRepository,
			ReturnStatusHistoryRepository returnStatusHistoryRepository, EmailTemplateService emailTemplateService,
			EmailService emailService, NotificationTemplateService notificationTemplateService,
			NotificationService notificationService, ReturnNoteRepository returnNoteRepository,
			ManagerManufacturerMappingRepository managerManufacturerMappingRepository,
			ReturnAttachmentRepository returnAttachmentRepository, StockistRepository stockistRepository,
			CityRepository cityRepository, TenantRepository tenantRepository,
			TenantManufactureRepository tenantManufactureRepository, TransportRepository transportRepository,
			FileStorageService fileStorageService, ReturnNoteService returnNoteService, ListValueRepository listvalue) {

		this.returnRepository = returnRepository;
		this.claimRepository = claimRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.returnStatusHistoryRepository = returnStatusHistoryRepository;
		this.emailTemplateService = emailTemplateService;
		this.emailService = emailService;
		this.notificationTemplateService = notificationTemplateService;
		this.notificationService = notificationService;
		this.returnNoteRepository = returnNoteRepository;
		this.stockistRepository = stockistRepository;
		this.cityRepository = cityRepository;
		this.tenantRepository = tenantRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.transportRepository = transportRepository;
		this.fileStorageService = fileStorageService;
		this.returnNoteService = returnNoteService;
		this.listvalue = listvalue;

	}

	@Override
	public List<Return> findAll() {
		try {
			return returnRepository.findAll();

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<Object[]> findAllReturnId() {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					return returnRepository.getAllReturnIdByManager(us.getUserId(), us.getTenantId());
				} else if (us.getRoleName().equals("USER")) {
					return returnRepository.getAllReturnIdByManager(us.getHierarachyId(), us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.STOCKIST)) {
					return returnRepository.getAllReturnIdByStockist(us.getUserId(), us.getTenantId());
				}

			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private String setStockistName(String id) {
		try {
			Optional<Stockist> stockist = stockistRepository.findById(id);
			if (stockist.isPresent()) {
				return stockist.get().getStockistName();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return StringIteration.EMPTY_STRING;
	}

	public Result<String> serialNumber() {
		Result<String> result = new Result<>();
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();
				Optional<Tenant> tn = tenantRepository.findById(us.getTenantId());
				if (tn.isPresent()) {
					String lastNum = returnRepository.getLastSequence(us.getTenantId());
					ZoneId zoneId = ZoneId.systemDefault();
					ZonedDateTime now = ZonedDateTime.now(zoneId);
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringIteration.UUUUMM, Locale.ENGLISH);
					String strToday = dtf.format(now);

					if (lastNum != null) {
						String s = String.format("%s%02d", RETURN_PREFIX, new BigInteger(lastNum).add(BigInteger.ONE));
						result.setData(tn.get().getTenantCode() + strToday + s);
						result.setCode("0000");
					} else {
						result.setData(tn.get().getTenantCode() + strToday + RETURN_PREFIX + "01");
						result.setCode("0000");
					}
				}
			}

		} catch (Exception e) {
			logger.error("", e);
			result.setCode("1111");
			result.setMessage("Invalid");
		}
		return result;
	}

	public List<Return> mobLogin(String status, String type) {
		try {
			Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				return processReturnList(user, status, type);
			}
		} catch (Exception e) {
			logger.error("Error during mobile login", e);
		}
		return new ArrayList<>();
	}

	private List<Return> processReturnList(User user, String status, String type) {
		List<Return> returnList = new ArrayList<>();
		if (user.getRoleName().equals(StringIteration.MANAGER)) {
			returnList = processManagerReturnList(user, status, type);
		} else if (user.getRoleName().equals(StringIteration.USER)) {
			returnList = processUserReturnList(user, status, type);
		}
		enrichReturnList(returnList);
		return returnList;
	}

	private List<Return> processManagerReturnList(User user, String status, String type) {
		if (StringUtils.isNullOrEmpty(type)) {
			return returnRepository.getByManager(user.getUserId(), user.getTenantId(), status);
		} else if (type.equals("Salable")) {
			return returnRepository.getByUserMobileS(user.getUserId(), user.getTenantId(), status);
		} else if (type.equals("NonSalable")) {
			return returnRepository.getByUserMobileNS(user.getUserId(), user.getTenantId(), status);
		}
		return new ArrayList<>();
	}

	private List<Return> processUserReturnList(User user, String status, String type) {
		if (StringUtils.isNullOrEmpty(type)) {
			return returnRepository.getByManager(user.getHierarachyId(), user.getTenantId(), status);
		} else if (type.equals("Salable")) {
			return returnRepository.getByUserMobileS(user.getHierarachyId(), user.getTenantId(), status);
		} else if (type.equals("NonSalable")) {
			return returnRepository.getByUserMobileNS(user.getHierarachyId(), user.getTenantId(), status);
		}
		return new ArrayList<>();
	}

	private void enrichReturnList(List<Return> returnList) {
		for (Return rn : returnList) {
			if (rn.getStockistId() != null) {
				Optional<Stockist> stockistOptional = stockistRepository.findById(rn.getStockistId());
				stockistOptional.ifPresent(stockist -> {
					rn.setStockistName(stockist.getStockistName());
					Optional<City> cityOptional = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
							stockist.getStateId());
					cityOptional.ifPresent(city -> rn.setLocation(city.getCityName()));
				});
			}
			if (rn.getTransporterId() != null) {
				Optional<Transport> transportOptional = transportRepository.findById(rn.getTransporterId());
				transportOptional.ifPresent(transport -> rn.setTransporterName(transport.getTransportName()));
			}
			if (rn.getManufacturer() != null) {
				Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(rn.getManufacturer());
				manufacturerOptional.ifPresent(manufacturer -> {
					rn.setManufacturertName(manufacturer.getManufacturerName());
					Optional<TenantManufacture> tenantManufactureOptional = tenantManufactureRepository
							.findByManufactureId(rn.getManufacturer(), rn.getTenantId());
					tenantManufactureOptional.ifPresent(tm -> rn.setSecondCheck(tm.isSecondCheck()));
				});
			}
		}
	}

	public List<ReturnQrDTO> findAllReturns(String search) {
		try {
			List<ReturnQrDTO> list = new ArrayList<>();
			Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());

			if (userOptional.isPresent()) {
				User user = userOptional.get();
				Tenant tenant = tenantRepository.getById(user.getTenantId());
				List<Return> returns = getReturnsByUserRole(user, search);

				for (Return returnObj : returns) {
					ReturnQrDTO qrDTO = createReturnQrDTO(returnObj, user, tenant);
					list.add(qrDTO);
				}
			}

			return list;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private List<Return> getReturnsByUserRole(User user, String search) {

		if (user.getRoleName().equals(StringIteration.MANAGER)) {
			return returnRepository.getByManagerNotPending(user.getUserId(), user.getTenantId(), search);
		} else if (user.getRoleName().equals("USER")) {
			return returnRepository.getByManagerNotPending(user.getHierarachyId(), user.getTenantId(), search);
		}

		return Collections.emptyList();
	}

	private ReturnQrDTO createReturnQrDTO(Return returnObj, User user, Tenant tenant) {

		ReturnQrDTO qrDTO = new ReturnQrDTO();
		qrDTO.setRecivedDate(formatDate(returnObj.getReceivedDate()));
		qrDTO.setClaimDate(formatDate(returnObj.getClaimDate()));
		qrDTO.setNumberOfNonSalableCases(returnObj.getNumOfNonSalableCases());
		qrDTO.setSerialNumber(returnObj.getSerialNumber());
		qrDTO.setReturnNumber(returnObj.getReturnNumber());
		qrDTO.setClaimNumber(returnObj.getClaimNumber());
		qrDTO.setNumberOfBoxes(String.valueOf(returnObj.getNumberOfBoxes()));
		List<ReturnNote> notes = returnNoteRepository.findAllNonSalableGRRNByReturnId(returnObj.getReturnId(),
				user.getTenantId());
		qrDTO.setGrrnNumber(notes.stream().map(ReturnNote::getNoteNumber).collect(Collectors.joining(",")));
		qrDTO.setGrrnDate(notes.stream().map(ReturnNote::getNoteDate).findFirst().map(this::formatDate).orElse(""));
		qrDTO.setCases("");
		qrDTO.setTenantName(tenant.getTenantName());
		qrDTO.setClaimType(returnObj.getClaimType());
		qrDTO.setStatus(returnObj.getStatus());

		setManufacturerDetails(qrDTO, returnObj);
		setStockistDetails(qrDTO, returnObj);

		return qrDTO;
	}

	private void setManufacturerDetails(ReturnQrDTO qrDTO, Return returnObj) {

		if (returnObj.getManufacturer() != null) {

			Manufacturer manufacturer = manufacturerRepository.getById(returnObj.getManufacturer());
			Optional<TenantManufacture> tenantManufactureOptional = tenantManufactureRepository
					.findByManufactureId(returnObj.getManufacturer(), returnObj.getTenantId());
			if (tenantManufactureOptional.isPresent()) {
				TenantManufacture tm = tenantManufactureOptional.get();
				qrDTO.setManufacturerName(manufacturer.getManufacturerName());
				qrDTO.setManufacturerPincode(tm.getPinCode());
				qrDTO.setDistributionModel(tm.getDistributionModel());

				if (tm.getCityId() != null && tm.getStateId() != null) {

					Optional<City> cityOptional = cityRepository.findByCityCodeAndStateCode(tm.getCityId(),
							tm.getStateId());
					cityOptional.ifPresent(city -> qrDTO.setManufacturerLocation(city.getCityName()));
				}
			}
		}
	}

	private void setStockistDetails(ReturnQrDTO qrDTO, Return returnObj) {

		if (returnObj.getStockistId() != null) {
			Optional<Stockist> stockistOptional = stockistRepository.findById(returnObj.getStockistId());
			if (stockistOptional.isPresent()) {
				Stockist stockist = stockistOptional.get();
				qrDTO.setStockistName(stockist.getStockistName());

				if (stockist.getCityId() != null && stockist.getStateId() != null) {
					Optional<City> cityOptional = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
							stockist.getStateId());
					cityOptional.ifPresent(city -> qrDTO.setLocation(city.getCityName()));
				}
			}
		}
	}

	private String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringIteration.DDMMYY);
		return sdf.format(date);
	}

	public Result<HashMap<String, Object>> getAllReturnsByUser(int page, String sortBy, String sortDir, String search,
			String status) {
		Result<HashMap<String, Object>> result = new Result<>();

		try {

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					return getAllReturnsByManager(page, sortBy, sortDir, search, status, us);
				} else if (us.getRoleName().equals("USER")) {
					return getAllReturnsByUser(page, sortBy, sortDir, search, status, us);
				} else {
					result.setMessage("Invalid User");
					result.setCode(StringIteration.SUCCESS_CODE);
				}
			}

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.SUCCESS_CODE);

		}

		return result;
	}

	public Result<HashMap<String, Object>> getAllReturnsByUser(int page, String sortBy, String sortDir, String search,
			String status, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			Map<String, Object> cardView = new HashMap<>();

			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (!user.isPresent()) {
				return result;
			}

			int size = 10;
			sortBy = StringUtils.isNullOrEmpty(sortBy) ? StringIteration.SERIAL_NUMBER : sortBy;
			sortDir = StringUtils.isNullOrEmpty(sortDir) ? "DESC" : sortDir;

			Optional<Tenant> tn = tenantRepository.findById(us.getTenantId());
			tn.ifPresent(tenant -> map.put(StringIteration.SECONDCHECK, tenant.isSecondCheck()));

			List<Return> returnsList;
			int totalItems;
			Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
			if (status != null && !status.isEmpty()) {
				returnsList = returnRepository.getByManager(us.getHierarachyId(), us.getTenantId(), status, search,
						paging);
				totalItems = returnRepository.getByManager(us.getHierarachyId(), us.getTenantId(), status).size();
			} else {
				returnsList = returnRepository.getAllByManager(us.getHierarachyId(), us.getTenantId(), search, paging);
				totalItems = returnRepository.getAllByManager(us.getHierarachyId(), us.getTenantId(), search).size();
			}

			for (Return rn : returnsList) {
				setStockistAndTransporterInfo(rn);
				setManufacturerInfo(rn, us.getTenantId());
				setGrrnNumber(rn, user.get().getTenantId());
			}

			setCardViewInfo(cardView, us.getTenantId(), us.getHierarachyId());

			map.put(StringIteration.RETURNS, returnsList);
			map.put(StringIteration.TOTALCOUNT, totalItems);
			map.put(StringIteration.CARDVIEW, cardView);

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCESS);
			result.setData(map);
		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private void setStockistAndTransporterInfo(Return rn) {
		if (rn.getStockistId() != null) {
			Optional<Stockist> st = stockistRepository.findById(rn.getStockistId());
			st.ifPresent(stockist -> {
				rn.setStockistName(stockist.getStockistName());
				if (stockist.getCityId() != null && stockist.getStateId() != null) {
					Optional<City> ct = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
							stockist.getStateId());
					ct.ifPresent(city -> rn.setLocation(city.getCityName()));
				}
			});
		}
		if (rn.getTransporterId() != null) {
			Optional<Transport> tran = transportRepository.findById(rn.getTransporterId());
			tran.ifPresent(transport -> rn.setTransporterName(transport.getTransportName()));
		}
	}

	private void setManufacturerInfo(Return rn, String tenantId) {
		if (rn.getManufacturer() != null) {
			Optional<Manufacturer> manf = manufacturerRepository.findById(rn.getManufacturer());
			manf.ifPresent(manufacturer -> rn.setManufacturertName(manufacturer.getManufacturerName()));

			Optional<TenantManufacture> tm = tenantManufactureRepository.findByManufactureId(rn.getManufacturer(),
					tenantId);
			tm.ifPresent(tenantManufacture -> rn.setSecondCheck(tenantManufacture.isSecondCheck()));
		}
	}

	private void setGrrnNumber(Return rn, String tenantId) {
		List<ReturnNote> note = returnNoteRepository.findAllGRRNByReturnId(rn.getReturnId(), tenantId);
		rn.setGrrnNumber(note.stream().map(ReturnNote::getNoteNumber).collect(Collectors.joining(",")));
	}

	private void setCardViewInfo(Map<String, Object> cardView, String tenantId, String string) {
		List<Object[]> ob = returnRepository.getReturnsCountByManager(tenantId, string);

		for (Object[] b : ob) {
			cardView.put(StringIteration.PENDING, String.valueOf(b[0]));
			cardView.put(StringIteration.RECIVED, String.valueOf(b[1]));
			cardView.put(StringIteration.CHECKED, String.valueOf(b[2]));
			cardView.put("grrn", String.valueOf(b[3]));
			cardView.put(StringIteration.SECONDCHECK, String.valueOf(b[4]));
			cardView.put("cn", String.valueOf(b[5]));
			cardView.put(StringIteration.TOTAL, String.valueOf(b[6]));
		}
	}

	public Result<HashMap<String, Object>> getAllReturnsByManager(int page, String sortBy, String sortDir,
			String search, String status, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				HashMap<String, Object> map = prepareReturnsData(page, sortBy, sortDir, search, status, us);
				result.setData(map);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private HashMap<String, Object> prepareReturnsData(int page, String sortBy, String sortDir, String search,
			String status, User us) {
		HashMap<String, Object> map = new HashMap<>();
		Map<String, Object> cardView = new HashMap<>();
		int size = 10;

		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = "serial_number";
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir = "DESC";
		}

		Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

		List<Return> returns = fetchReturnsData(status, search, us, paging);
		populateAdditionalReturnInfo(returns, us.getTenantId());

		Optional<Tenant> tenant = tenantRepository.findById(us.getTenantId());
		if (tenant.isPresent()) {
			map.put(StringIteration.SECONDCHECK, tenant.get().isSecondCheck());
		}

		fillCardViewInfo(cardView, us.getTenantId(), us.getUserId());

		map.put("returns", returns);
		map.put("totalCount", getTotalReturnsCount(status, search, us));
		map.put("cardView", cardView);

		return map;
	}

	private List<Return> fetchReturnsData(String status, String search, User us, Pageable paging) {
		List<Return> returns;
		if (status != null && !"".equals(status)) {
			returns = returnRepository.getByManager(us.getUserId(), us.getTenantId(), status, search, paging);
		} else {
			returns = returnRepository.getAllByManager(us.getUserId(), us.getTenantId(), search, paging);
		}
		return returns;
	}

	private void populateAdditionalReturnInfo(List<Return> returns, String tenantId) {
		for (Return rn : returns) {
			populateStockistInfo(rn);
			populateTransporterInfo(rn);
			populateManufacturerInfo(rn, tenantId);
			populateReturnNoteInfo(rn, tenantId);
		}
	}

	private void populateStockistInfo(Return rn) {
		if (rn.getStockistId() != null) {
			Optional<Stockist> stockist = stockistRepository.findById(rn.getStockistId());
			stockist.ifPresent(st -> {
				rn.setStockistName(st.getStockistName());
				if (st.getCityId() != null && st.getStateId() != null) {
					Optional<City> city = cityRepository.findByCityCodeAndStateCode(st.getCityId(), st.getStateId());
					city.ifPresent(ct -> rn.setLocation(ct.getCityName()));
				}
			});
		}
	}

	private void populateTransporterInfo(Return rn) {
		if (rn.getTransporterId() != null) {
			Optional<Transport> transporter = transportRepository.findById(rn.getTransporterId());
			transporter.ifPresent(tran -> rn.setTransporterName(tran.getTransportName()));
		}
	}

	private void populateManufacturerInfo(Return rn, String tenantId) {
		if (rn.getManufacturer() != null) {
			Optional<Manufacturer> manufacturer = manufacturerRepository.findById(rn.getManufacturer());
			manufacturer.ifPresent(manf -> rn.setManufacturertName(manf.getManufacturerName()));

			Optional<TenantManufacture> tenantManufacturer = tenantManufactureRepository
					.findByManufactureId(rn.getManufacturer(), tenantId);
			tenantManufacturer.ifPresent(tm -> rn.setSecondCheck(tm.isSecondCheck()));
		}
	}

	private void populateReturnNoteInfo(Return rn, String tenantId) {
		List<ReturnNote> notes = returnNoteRepository.findAllGRRNByReturnId(rn.getReturnId(), tenantId);
		rn.setGrrnNumber(notes.stream().map(ReturnNote::getNoteNumber).collect(Collectors.joining(",")));
	}

	private void fillCardViewInfo(Map<String, Object> cardView, String tenantId, String userId) {
		List<Object[]> counts = returnRepository.getReturnsCountByManager(tenantId, userId);
		for (Object[] count : counts) {
			cardView.put("pending", String.valueOf(count[0]));
			cardView.put("recived", String.valueOf(count[1]));
			cardView.put("checked", String.valueOf(count[2]));
			cardView.put("grrn", String.valueOf(count[3]));
			cardView.put("secondCheck", String.valueOf(count[4]));
			cardView.put("cn", String.valueOf(count[5]));
			cardView.put("Total", String.valueOf(count[6]));
		}
	}

	private int getTotalReturnsCount(String status, String search, User us) {
		if (status != null && !"".equals(status)) {
			return returnRepository.getAllByManager(us.getUserId(), us.getTenantId(), status, search).size();
		} else {
			return returnRepository.getAllByManager(us.getUserId(), us.getTenantId(), search).size();
		}
	}

	public Result<HashMap<String, Object>> getAllReturns(int page, String sortBy, String sortDir, String search,
			String status, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			Map<String, Object> cardView = new HashMap<>();

			int size = 10;

			sortBy = StringUtils.isNullOrEmpty(sortBy) ? "serial_number" : sortBy;
			sortDir = StringUtils.isNullOrEmpty(sortDir) ? "DESC" : sortDir;

			Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
			List<Return> ret = returnRepository.getAllReturns(paging, us.getTenantId(), search, status);
			processReturnData(ret);

			processCardViewData(cardView, us);

			int totalItems = returnRepository.getAllReturnsByTenant(us.getTenantId(), status).size();
			map.put("returns", ret);
			map.put("totalCount", totalItems);
			map.put("cardView", cardView);

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage("success");
			result.setData(map);

			return result;

		} catch (Exception e) {
			logger.error("", e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private void processReturnData(List<Return> ret) {
		for (Return rn : ret) {
			processStockistData(rn);
			processTransporterData(rn);
			processManufacturerData(rn);
		}
	}

	private void processStockistData(Return rn) {
		if (rn.getStockistId() != null) {
			Optional<Stockist> st = stockistRepository.findById(rn.getStockistId());
			st.ifPresent(stockist -> {
				rn.setStockistName(stockist.getStockistName());
				rn.setLocation(stockist.getCityId());
			});
		}
	}

	private void processTransporterData(Return rn) {
		if (rn.getTransporterId() != null) {
			Optional<Transport> tran = transportRepository.findById(rn.getTransporterId());
			tran.ifPresent(transport -> rn.setTransporterName(transport.getTransportName()));
		}
	}

	private void processManufacturerData(Return rn) {
		if (rn.getManufacturer() != null) {
			Manufacturer manufacturer = manufacturerRepository.getById(rn.getManufacturer());
			rn.setManufacturertName(manufacturer.getManufacturerName());

		}
	}

	private void processCardViewData(Map<String, Object> cardView, User us) {
		List<Object[]> ob = returnRepository.getReturnsCountAll(us.getTenantId());

		BigInteger t = BigInteger.ZERO;
		for (Object[] b : ob) {
			cardView.put("pending", String.valueOf(b[0]));
			cardView.put("recived", String.valueOf(b[1]));
			cardView.put("checked", String.valueOf(b[2]));
			cardView.put("grrn", String.valueOf(b[3]));
			cardView.put("secondCheck", String.valueOf(b[4]));
			cardView.put("cn", String.valueOf(b[5]));

			t = t.add(((BigInteger) b[0])).add(((BigInteger) b[1])).add(((BigInteger) b[2])).add(((BigInteger) b[3]))
					.add(((BigInteger) b[4])).add(((BigInteger) b[5]));
			cardView.put("Total", t);
		}
	}

	@Override
	public Return findById(String id) {
		try {
			Optional<Return> optional = returnRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public ReturnDetails findReturnDetails(String serialNumber) {
		ReturnDetails details = new ReturnDetails();
		try {

			Optional<User> user = getCurrentUser();
			user.ifPresent(u -> {
				Optional<Return> optionalReturn = getReturn(serialNumber, u.getTenantId());
				optionalReturn.ifPresent(rn -> {
					updateStockistDetails(rn);
					updateManufacturerDetails(rn);
					updateTransporterDetails(rn);
					List<ReturnNote> notes = returnNoteRepository.findByReturnId(serialNumber);
					List<ReturnStatusHistory> history = returnStatusHistoryRepository.findByReturnId(serialNumber,
							u.getTenantId());
					details.setReturnData(rn);
					details.setReturnNotes(notes);
					details.setHistory(history);
				});
			});
		} catch (Exception e) {
			logger.error("Error finding return details", e);
		}
		return details;
	}

	private Optional<User> getCurrentUser() {
		return userRepository.findById(SecurityUtil.getUserName());
	}

	private Optional<Return> getReturn(String serialNumber, String tenantId) {
		return returnRepository.getReturnsByTenant(serialNumber, tenantId);
	}

	private void updateStockistDetails(Return rn) {

		if (rn.getStockistId() != null) {
			Optional<Stockist> stockist = stockistRepository.findById(rn.getStockistId());
			stockist.ifPresent(st -> {
				rn.setStockistName(st.getStockistName());
				if (st.getCityId() != null && st.getStateId() != null) {
					Optional<City> city = cityRepository.findByCityCodeAndStateCode(st.getCityId(), st.getStateId());
					city.ifPresent(ct -> rn.setLocation(ct.getCityName()));
				}
			});
		}
	}

	private void updateManufacturerDetails(Return rn) {

		if (rn.getManufacturer() != null) {
			Optional<Manufacturer> manufacturer = manufacturerRepository.findById(rn.getManufacturer());
			manufacturer.ifPresent(man -> rn.setManufacturertName(man.getManufacturerName()));
			Optional<TenantManufacture> tm = tenantManufactureRepository.findByManufactureId(rn.getManufacturer(),
					rn.getTenantId());
			tm.ifPresent(tenantManufacture -> rn.setSecondCheck(tenantManufacture.isSecondCheck()));
		}
	}

	private void updateTransporterDetails(Return rn) {

		if (rn.getTransporterId() != null) {
			Optional<Transport> transport = transportRepository.findById(rn.getTransporterId());
			transport.ifPresent(tran -> rn.setTransporterName(tran.getTransportName()));
		}
	}

	public List<Return> findAllReturnDetails() {
		try {
			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User user = op.get();
				List<Return> returns = getReturnsByUser(user);
				enrichReturnDetails(returns);
				return returns;
			}
		} catch (Exception e) {
			logger.error("Error fetching return details", e);
		}
		return Collections.emptyList();
	}

	private List<Return> getReturnsByUser(User user) {

		if (user.getRoleName().equals(StringIteration.MANAGER)) {
			return returnRepository.getByManager(user.getUserId(), user.getTenantId(), StringIteration.RECEIVED);
		} else if (user.getRoleName().equals("USER")) {
			return returnRepository.getByManager(user.getHierarachyId(), user.getTenantId(), StringIteration.RECEIVED);
		} else {
			return returnRepository.getAllReturnsByTenant(user.getTenantId());
		}
	}

	private void enrichReturnDetails(List<Return> returns) {

		for (Return ret : returns) {
			enrichStockistDetails(ret);
			enrichManufacturerDetails(ret);
			enrichTransporterDetails(ret);
		}
	}

	private void enrichStockistDetails(Return ret) {
		if (ret.getStockistId() != null) {
			Optional<Stockist> stockistOptional = stockistRepository.findById(ret.getStockistId());
			stockistOptional.ifPresent(stockist -> {
				ret.setStockistName(stockist.getStockistName());
				ret.setLocation(stockist.getCityId());
			});
		}
	}

	private void enrichManufacturerDetails(Return ret) {
		if (ret.getManufacturer() != null) {
			Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(ret.getManufacturer());
			manufacturerOptional
					.ifPresent(manufacturer -> ret.setManufacturertName(manufacturer.getManufacturerName()));
		}
	}

	private void enrichTransporterDetails(Return ret) {
		if (ret.getTransporterId() != null) {
			Optional<Transport> transportOptional = transportRepository.findById(ret.getTransporterId());
			transportOptional.ifPresent(transport -> ret.setTransporterName(transport.getTransportName()));
		}
	}

	@Override
	public Result<Return> create(Return t) {
		Result<Return> result = new Result<>();
		try {

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				String adminMail = "RETURN_RECIVED";
				String stkMail = "RETURN_RECIVED_STOCKIST";
				t.setStatus(StringIteration.RECEIVED);
				t.setReceivedDate(new Date());
				t.setTenantId(us.get().getTenantId());
				CommonUtil.setCreatedOn(t);
				Return r = returnRepository.save(t);
//				returMail(t, adminMail, stkMail);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESSFULCREATED);
				result.setData(t);

				saveHistory(r.getSerialNumber(), r.getStatus(), r.getReturnId());

			}

		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Return> createReturn(Return t) {
		Result<Return> result = new Result<>();
		try {
			String adminMail = "RETURN_RECIVED";
			String stkMail = "RETURN_RECIVED_STOCKIST";
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			String serialNumber;
			String returnNumber;
			if (us.isPresent()) {
				User u = us.get();
				Optional<Tenant> tn = tenantRepository.findById(u.getTenantId());
				if (tn.isPresent()) {
					if (t.getReturnId() != null) {
						Optional<Return> rtn = returnRepository.findById(t.getReturnId());
						if (rtn.isPresent()) {
							Return rn = rtn.get();
							String lastNum = returnRepository.getLastSequenceByManufacturer(u.getTenantId(),
									rn.getManufacturer());
							String manufacturerName = manufacturerRepository.findManufacturName(rn.getManufacturer());
							ZoneId zoneId = ZoneId.systemDefault();
							ZonedDateTime now = ZonedDateTime.now(zoneId);
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMM", Locale.ENGLISH);
							String strToday = dtf.format(now);

							if (lastNum != null) {
								String s = String.format("%s%03d", "", new BigInteger(lastNum).add(BigInteger.ONE));
								serialNumber = tn.get().getTenantCode() + manufacturerName + strToday + s;
								returnNumber = manufacturerName + strToday + s;
							} else {
								serialNumber = tn.get().getTenantCode() + manufacturerName + strToday + "001";
								returnNumber = manufacturerName + strToday + "001";
							}
							rn.setReturnNumber(returnNumber);
							rn.setSerialNumber(serialNumber);
							rn.setStatus(StringIteration.RECEIVED);
							rn.setSalabletatus(StringIteration.RECEIVED);
							rn.setNonSalabletatus(StringIteration.RECEIVED);
							rn.setReceivedDate(t.getReceivedDate());
							rn.setTenantId(u.getTenantId());
							rn.setLrBookingDate(t.getLrBookingDate());
							rn.setNumberOfBoxes(t.getNumberOfBoxes());
							rn.setLrNumber(t.getLrNumber());
							rn.setTransporterId(t.getTransporterId());

							rn.setClaimNumber(t.getClaimNumber());
							rn.setClaimDate(t.getClaimDate());
							Optional<Claim> cl = claimRepository.findById(rn.getClaimId());
							if (cl.isPresent()) {
								Claim claim = cl.get();
								claim.setClaimNumber(t.getClaimNumber());
								claim.setStatus("Inprocess");
								claim.setClaimDate(t.getClaimDate());
								claim.setReceivedDate(new Date());
								claimRepository.save(claim);
							}
							returnRepository.save(rn);
							returMailStockist(rn, u, stkMail);
//							returMail(rn, adminMail, stkMail);

							result.setCode("0000");
							result.setMessage(StringIteration.SUCCESSFULCREATED);

							ReturnStatusHistory history = new ReturnStatusHistory();

							history.setHistoryBy(u.getFirstName());
							history.setHistoryOn(t.getReceivedDate());
							history.setReturnId(t.getReturnId());
							history.setSerialNumber(serialNumber);
							history.setStatus(StringIteration.RECEIVED);
							history.setId(System.currentTimeMillis() + "");
							history.setTenantId(us.get().getTenantId());
							returnStatusHistoryRepository.save(history);
						}
					} else {

						String lastNum = returnRepository.getLastSequenceByManufacturer(u.getTenantId(),
								t.getManufacturer());
						String manufacturerName = manufacturerRepository.findManufacturName(t.getManufacturer());

						ZoneId zoneId = ZoneId.systemDefault();
						ZonedDateTime now = ZonedDateTime.now(zoneId);
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMM", Locale.ENGLISH);
						String strToday = dtf.format(now);

						if (lastNum != null) {
							String s = String.format("%s%03d", "", new BigInteger(lastNum).add(BigInteger.ONE));
							serialNumber = tn.get().getTenantCode() + manufacturerName + strToday + s;
							returnNumber = manufacturerName + strToday + s;
						} else {
							serialNumber = tn.get().getTenantCode() + manufacturerName + strToday + "001";
							returnNumber = manufacturerName + strToday + "001";
						}

						t.setReturnNumber(returnNumber);
						t.setSerialNumber(serialNumber);
						t.setReturnId(System.currentTimeMillis() + "");
						t.setStatus(StringIteration.RECEIVED);
						t.setSalabletatus(StringIteration.RECEIVED);
						t.setNonSalabletatus("RECEIVED");
						t.setReceivedDate(t.getReceivedDate());
						t.setCreatedOn(new Date());
						t.setTenantId(u.getTenantId());

						Claim claim = new Claim();

						String lastNum1 = claimRepository.getLastSequence(u.getTenantId());
						ZoneId zoneId1 = ZoneId.systemDefault();
						ZonedDateTime now1 = ZonedDateTime.now(zoneId1);
						DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("uuuuMMdd", Locale.ENGLISH);
						String strToday1 = dtf1.format(now1);

						if (lastNum1 != null) {
							String s = String.format("%s%02d", PREFIX, new BigInteger(lastNum1).add(BigInteger.ONE));
							claim.setClaimId(tn.get().getTenantCode() + strToday1 + s);
						} else {
							claim.setClaimId(tn.get().getTenantCode() + strToday1 + PREFIX + "01");
						}

						claim.setStatus("Inprocess");
						claim.setStockistId(t.getStockistId());
						claim.setTenantId(t.getTenantId());
						claim.setCreatedBy(SecurityUtil.getUserName());
						claim.setCreatedOn(new Date());
						claim.setClaimDate(t.getClaimDate());
						claim.setReceivedDate(t.getReceivedDate());
						claim.setClaimNumber(t.getClaimNumber());
						claim.setManufacturerId(t.getManufacturer());

						Claim cl = claimRepository.save(claim);
						CommonUtil.setCreatedOn(t);
						t.setClaimId(cl.getClaimId());
						Return r = returnRepository.save(t);
						returMailStockist(t, u, stkMail);
//						returMail(t, adminMail, stkMail);
						result.setCode("0000");
						result.setMessage(StringIteration.SUCCESSFULCREATED);
						result.setData(t);

						ReturnStatusHistory history = new ReturnStatusHistory();

						history.setHistoryOn(r.getReceivedDate());
						history.setReturnId(r.getReturnId());
						history.setSerialNumber(serialNumber);
						history.setStatus("RECEIVED");
						history.setId(System.currentTimeMillis() + "");
						history.setHistoryBy(u.getFirstName());
						history.setTenantId(us.get().getTenantId());
						returnStatusHistoryRepository.save(history);
					}
				}
			}

		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	@Override
	public Result<Return> update(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<Return> list = returnRepository.findById(t.getSerialNumber());
			if (!list.isPresent()) {
				CommonUtil.setModifiedOn(t);
				Return r = returnRepository.save(t);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESSFULCREATED);
				result.setData(t);
				saveHistory(r.getSerialNumber(), r.getStatus(), r.getReturnId());
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Return> updateReturnNote(List<ReturnNote> returnNotes) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				if (returnNotes == null) {
					result.setCode("1111");
					result.setMessage(StringIteration.GRRNORCNSHOULDNOTBEEMPTY);
					return result;
				}
				String serialNum = null;
				String status = null;
				for (ReturnNote returnNote : returnNotes) {
					returnNote.setId(System.currentTimeMillis() + 2 + "");
					serialNum = returnNote.getSerialNumber();
					status = returnNote.getNoteType();
					CommonUtil.setCreatedOn(returnNote);
					returnNoteRepository.save(returnNote);
				}
				String rnsts = "";
				Optional<Return> ret = returnRepository.findBySerialNumber(serialNum);
				if (status != null && ret.isPresent()) {
					if (status.equals("GRRN")) {
						rnsts = status;
						status = (StringIteration.GRRN_CREATED);
					} else if (status.equals("CN")) {
						rnsts = status;
						status = (StringIteration.CN_CREATED);
						Optional<Claim> cl = claimRepository.findByClaimNumber(ret.get().getClaimNumber());
						if (cl.isPresent()) {
							Claim claim = cl.get();
							claim.setStatus(StringIteration.COMPLETED);
							claim.setReceivedDate(new Date());
							claimRepository.save(claim);
						}
					}
				}

				boolean saleble = false;
				boolean nonsaleable = false;
				if (ret.isPresent()) {
					Return rn = ret.get();
					String sts = rn.getStatus();
					if (rn.getClaimType().equals("Both")) {
						List<ReturnNote> rtn = returnNoteRepository.findBySerialNumber(rn.getSerialNumber(), rnsts,
								user.get().getTenantId());
						for (ReturnNote retNote : rtn) {
							if (retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								saleble = true;

							} else if (!retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								nonsaleable = true;
							}
						}
						if (saleble && nonsaleable) {
							rn.setStatus(status);
						} else {
							rn.setStatus(sts);
						}
					} else {
						rn.setStatus(status);

					}

					Return rtn = returnRepository.save(rn);
//					returMail(rtn, status, status + "_STOCKIST");
					saveHistory(serialNum, status, rtn.getReturnId());
				}

				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESSFULLYCREATED);

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Return> updateReturnSalable(List<ReturnNote> returnNotes) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {

				User us = user.get();

				if (returnNotes == null) {
					result.setCode("1111");
					result.setMessage("GRRN or CN should not be empty");
					return result;
				}

				String _type = returnNotes.get(0).getNoteType();
				if (_type.equals(StringIteration.GRRN)) {
					BooleanResult grrn = returnNoteService.checkDuplicateGrrnNumber(returnNotes, us);
					if (grrn.isFlag()) {
						result.setCode(StringIteration.ERROR_CODE3);
						result.setMessage("Duplicate GRRN Number " + grrn.getMessage());
						return result;
					}
				} else if (_type.equals(StringIteration.CN)) {
					BooleanResult cn = returnNoteService.checkDuplicateCnNumber(returnNotes, us);
					if (cn.isFlag()) {
						result.setCode(StringIteration.ERROR_CODE3);
						result.setMessage("Duplicate CN Number " + cn.getMessage());
						return result;
					}
				}

				String serialNum = null;
				String status = null;
				String returnId = null;

				for (ReturnNote returnNote : returnNotes) {
					returnNote.setId(System.currentTimeMillis() + 2 + "");
					serialNum = returnNote.getSerialNumber();
					returnId = returnNote.getReturnId();
					status = returnNote.getNoteType();
					returnNote.setTenantId(us.getTenantId());
					returnNote.setClaimType(StringIteration.SALEABLE);
					CommonUtil.setCreatedOn(returnNote);
					returnNoteRepository.save(returnNote);
				}
				String type = status;
				if (status != null) {
					if (status.equals("GRRN")) {
						status = (StringIteration.GRRN_CREATED);
					} else if (status.equals("CN")) {
						status = (StringIteration.CN_CREATED);
					}
				}
				Optional<Return> ret = returnRepository.findById(returnId);
				if (ret.isPresent()) {
					Return r = ret.get();
					boolean saleble = false;
					boolean nonsaleable = false;
					if (r.getClaimType().equals("Both")) {
						List<ReturnNote> rtnNote = returnNoteRepository.findBySerialNumber(r.getReturnId(), type,
								user.get().getTenantId());
						for (ReturnNote retNote : rtnNote) {
							if (retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								saleble = true;

							} else if (!retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								nonsaleable = true;
							}
						}
						if (saleble && nonsaleable) {
							r.setStatus(status);

							if (status.equals(StringIteration.CN_CREATED)) {
								Optional<Claim> cl = claimRepository.findById(r.getClaimId());
								if (cl.isPresent()) {
									Claim claim = cl.get();
									claim.setStatus(StringIteration.COMPLETED);
									claim.setReceivedDate(new Date());
									claimRepository.save(claim);
								}
							}
						}
					} else {
						r.setStatus(status);
						if (status.equals(StringIteration.CN_CREATED)) {
							Optional<Claim> cl = claimRepository.findById(r.getClaimId());
							if (cl.isPresent()) {
								Claim claim = cl.get();
								claim.setStatus(StringIteration.COMPLETED);
								claim.setReceivedDate(new Date());
								claimRepository.save(claim);
							}
						}
					}

					r.setSalabletatus(status);
					returnRepository.save(r);

					ReturnStatusHistory history = new ReturnStatusHistory();
					history.setId(System.currentTimeMillis() + "");
					history.setSerialNumber(serialNum);
					history.setStatus(status + " SALEABLE");
					history.setHistoryBy(us.getFirstName());
					history.setHistoryOn(new Date());
					history.setReturnId(ret.get().getReturnId());
					history.setTenantId(us.getTenantId());
					returnStatusHistoryRepository.save(history);

					result.setCode("0000");
					result.setMessage("Successfully Created");
				}
			}

		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Return> updateReturnNonSalable(List<ReturnNote> returnNotes) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {

				User us = user.get();

				if (returnNotes == null) {
					result.setCode("1111");
					result.setMessage("GRRN or CN should not be empty");
					return result;
				}

				String _type = returnNotes.get(0).getNoteType();
				if (_type.equals(StringIteration.GRRN)) {
					BooleanResult grrn = returnNoteService.checkDuplicateGrrnNumber(returnNotes, us);
					if (grrn.isFlag()) {
						result.setCode(StringIteration.ERROR_CODE3);
						result.setMessage("Duplicate GRRN Number " + grrn.getMessage());
						return result;
					}
				} else if (_type.equals(StringIteration.CN)) {
					BooleanResult cn = returnNoteService.checkDuplicateCnNumber(returnNotes, us);
					if (cn.isFlag()) {
						result.setCode(StringIteration.ERROR_CODE3);
						result.setMessage("Duplicate CN Number " + cn.getMessage());
						return result;
					}
				}

				String serialNum = null;
				String status = null;
				String returnId = null;
				for (ReturnNote returnNote : returnNotes) {
					returnNote.setId(System.currentTimeMillis() + 2 + "");
					serialNum = returnNote.getSerialNumber();
					returnId = returnNote.getReturnId();
					status = returnNote.getNoteType();
					returnNote.setTenantId(user.get().getTenantId());
					returnNote.setClaimType(StringIteration.NONSALEABLE);
					CommonUtil.setCreatedOn(returnNote);
					returnNoteRepository.save(returnNote);
				}
				String type = status;
				if (status != null) {
					if (status.equals("GRRN")) {
						status = (StringIteration.GRRN_CREATED);
					} else if (status.equals("CN")) {
						status = (StringIteration.CN_CREATED);
					}
				}
				Optional<Return> ret = returnRepository.findById(returnId);
				if (ret.isPresent()) {
					Return r = ret.get();
					boolean saleble = false;
					boolean nonsaleable = false;
					if (r.getClaimType().equals("Both")) {
						List<ReturnNote> rtnNote = returnNoteRepository.findBySerialNumber(r.getReturnId(), type,
								user.get().getTenantId());
						for (ReturnNote retNote : rtnNote) {
							if (retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								saleble = true;

							} else if (!retNote.getClaimType().equals(StringIteration.SALEABLE)) {
								nonsaleable = true;
							}
						}
						if (saleble && nonsaleable) {
							r.setStatus(status);

							if (status.equals("CN_CREATED")) {
								Optional<Claim> cl = claimRepository.findById(r.getClaimId());
								if (cl.isPresent()) {
									Claim claim = cl.get();
									claim.setStatus("Completed");
									claim.setReceivedDate(new Date());
									claimRepository.save(claim);
								}
							}
						}
					} else {
						r.setStatus(status);
						if (status.equals("CN_CREATED")) {
							Optional<Claim> cl = claimRepository.findById(r.getClaimId());
							if (cl.isPresent()) {
								Claim claim = cl.get();
								claim.setStatus("Completed");
								claim.setReceivedDate(new Date());
								claimRepository.save(claim);
							}
						}
					}

					r.setNonSalabletatus(status);
					returnRepository.save(r);

					ReturnStatusHistory history = new ReturnStatusHistory();
					history.setId(System.currentTimeMillis() + "");
					history.setSerialNumber(serialNum);
					history.setStatus(status + " NON SALEABLE");
					history.setHistoryBy(us.getFirstName());
					history.setHistoryOn(new Date());
					history.setTenantId(us.getTenantId());
					history.setReturnId(ret.get().getReturnId());
					returnStatusHistoryRepository.save(history);

					result.setCode("0000");
					result.setMessage("Successfully Created");
				}
			}

		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Return> updatePhysicalChecks(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<Return> option = returnRepository.findBySerialNumber(t.getSerialNumber());
			if (option.isPresent()) {
				Return returnObject = option.get();

				if (returnObject.getStatus().equals(StringIteration.GRRN_CREATED)) {
					returnObject.setStatus(StringIteration.CHECKEDII);
					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getStatus(), returnObject.getReturnId());

				} else {
					returnObject.setStatus(StringIteration.CHECKEDD);
					returnObject.setClaimType(t.getClaimType());
					returnObject.setNumberOfLineItems(t.getNumberOfLineItems());

					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);

//					returMail(returnObject, StringIteration.RETURN_CHECKED, StringIteration.RETURN_CHECKED_STOCKIST);
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getStatus(), returnObject.getReturnId());
				}

			} else {
				result.setCode("1111");
				result.setMessage(StringIteration.SERIALNUMBER);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	public Result<Return> updatePhysicalChecksBoth(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			Optional<Return> option = returnRepository.findById(t.getReturnId());
			if (user.isPresent() && option.isPresent() && option.get().getStatus().equals(StringIteration.RECEIVED)) {
				Return returnObject = option.get();
				User us = user.get();
				returnObject.setSalabletatus(StringIteration.CHECKEDD);
				returnObject.setNonSalabletatus(StringIteration.CHECKEDD);
				returnObject.setStatus(StringIteration.CHECKEDD);
				returnObject.setChannel(t.getChannel());
				returnObject.setClaimNumber(t.getClaimNumber());
				Optional<Claim> cl = claimRepository.findById(returnObject.getClaimId());
				if (cl.isPresent()) {
					cl.get().setClaimNumber(returnObject.getClaimNumber());
					claimRepository.save(cl.get());
				}
				returnObject.setDocumentURL(t.getDocumentURL());
				if (t.getMobileDocumentName() != null) {
					returnObject.setMobileDocumentName(t.getMobileDocumentName());
				} else {
					returnObject.setMobileDocumentName(StringIteration.UPLOADEDFILE);
				}
				returnObject.setNumOfNonSalableCases(t.getNumOfNonSalableCases());
				returnObject.setMisMatch(t.isMisMatch());
				returnObject.setMisMatchType(t.getMisMatchType());
				returnObject.setRemarks(t.getRemarks());

				returnObject.setClaimType(t.getClaimType());
				returnObject.setNumberOfLineItems(t.getNumberOfLineItems());

				CommonUtil.setModifiedOn(returnObject);
				returnRepository.save(returnObject);
				if (returnObject.isMisMatch()) {
					returMailStockist(returnObject, us, StringIteration.MISMATCH_IN_CLAIMS_RECEIVED_STOCKIST);
				}

//				returMail(returnObject, StringIteration.RETURN_CHECKED, StringIteration.RETURN_CHECKED_STOCKIST);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESSFULCREATED);
				result.setData(t);
				saveHistory(returnObject.getSerialNumber(), returnObject.getStatus(), returnObject.getReturnId());

			} else {
				result.setCode("2222");
				result.setMessage(StringIteration.SERIALNUMBER);

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	public Result<Return> updatePhysicalChecksSalable(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			Optional<Return> option = returnRepository.findById(t.getReturnId());
			if (option.isPresent() && user.isPresent()) {
				User us = user.get();
				Return returnObject = option.get();

				if (returnObject.getSalabletatus().equals("GRRN_CREATED")) {
					returnObject.setSalabletatus(StringIteration.CHECKEDII);
					if (returnObject.getSalabletatus().equals(StringIteration.CHECKEDII)
							&& returnObject.getNonSalabletatus().equals(StringIteration.CHECKEDII)) {
						returnObject.setStatus(StringIteration.CHECKEDII);
					}
					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getSalabletatus() + " SALEABLE",
							returnObject.getReturnId());

				} else if (returnObject.getStatus().equals(StringIteration.RECEIVED)) {
					returnObject.setSalabletatus(StringIteration.CHECKEDD);
					returnObject.setStatus(StringIteration.CHECKEDD);
					returnObject.setClaimType(t.getClaimType());
					returnObject.setNumberOfLineItems(t.getNumberOfLineItems());
					returnObject.setChannel(t.getChannel());
					returnObject.setDocumentURL(t.getDocumentURL());
					returnObject.setNumOfNonSalableCases(t.getNumOfNonSalableCases());
					returnObject.setMisMatch(t.isMisMatch());
					returnObject.setClaimNumber(t.getClaimNumber());
					if (t.getMobileDocumentName() != null) {
						returnObject.setMobileDocumentName(t.getMobileDocumentName());
					} else {
						returnObject.setMobileDocumentName("uploadedfile");
					}

					Optional<Claim> cl = claimRepository.findById(returnObject.getClaimId());
					if (cl.isPresent()) {
						cl.get().setClaimNumber(returnObject.getClaimNumber());
						claimRepository.save(cl.get());
					}
					returnObject.setMisMatchType(t.getMisMatchType());
					returnObject.setRemarks(t.getRemarks());

					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);

//					returMail(returnObject, "RETURN_CHECKED", "RETURN_CHECKED_STOCKIST");

					if (returnObject.isMisMatch()) {
						returMailStockist(returnObject, us, StringIteration.MISMATCH_IN_CLAIMS_RECEIVED_STOCKIST);
					}

					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getStatus(), returnObject.getReturnId());
				} else {
					result.setCode(StringIteration.ERROR_CODE3);
				}

			} else {
				result.setCode("1111");
				result.setMessage(StringIteration.SERIALNUMBER);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	public Result<Return> updatePhysicalChecksNonSalable(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			Optional<Return> option = returnRepository.findById(t.getReturnId());
			if (option.isPresent() && user.isPresent()) {
				Return returnObject = option.get();
				User us = user.get();

				if (returnObject.getNonSalabletatus().equals("GRRN_CREATED")) {

					returnObject.setNonSalabletatus(StringIteration.CHECKEDII);
					if (returnObject.getSalabletatus().equals(StringIteration.CHECKEDII)
							&& returnObject.getNonSalabletatus().equals("CHECKED II")) {
						returnObject.setStatus("CHECKED II");
					}
					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getNonSalabletatus() + " NON SALEABLE",
							returnObject.getReturnId());

				} else if (returnObject.getStatus().equals(StringIteration.RECEIVED)) {
					returnObject.setStatus(StringIteration.CHECKEDD);
					returnObject.setNonSalabletatus(StringIteration.CHECKEDD);
					returnObject.setClaimType(t.getClaimType());
					returnObject.setNumberOfLineItems(t.getNumberOfLineItems());
					returnObject.setChannel(t.getChannel());
					returnObject.setClaimNumber(t.getClaimNumber());
					Optional<Claim> cl = claimRepository.findById(returnObject.getClaimId());
					if (cl.isPresent()) {
						cl.get().setClaimNumber(returnObject.getClaimNumber());
						claimRepository.save(cl.get());
					}
					returnObject.setDocumentURL(t.getDocumentURL());
					if (t.getMobileDocumentName() != null) {
						returnObject.setMobileDocumentName(t.getMobileDocumentName());
					} else {
						returnObject.setMobileDocumentName("uploadedfile");
					}
					returnObject.setNumOfNonSalableCases(t.getNumOfNonSalableCases());
					returnObject.setMisMatch(t.isMisMatch());
					returnObject.setMisMatchType(t.getMisMatchType());
					returnObject.setRemarks(t.getRemarks());

					CommonUtil.setModifiedOn(returnObject);
					returnRepository.save(returnObject);

//					returMail(returnObject, "RETURN_CHECKED", "RETURN_CHECKED_STOCKIST");
					if (returnObject.isMisMatch()) {
						returMailStockist(returnObject, us, StringIteration.MISMATCH_IN_CLAIMS_RECEIVED_STOCKIST);
					}

					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);
					result.setData(t);
					saveHistory(returnObject.getSerialNumber(), returnObject.getStatus(), returnObject.getReturnId());
				} else {
					result.setCode(StringIteration.ERROR_CODE3);
				}

			} else {
				result.setCode("1111");
				result.setMessage(StringIteration.SERIALNUMBER);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	public Result<Return> updatePhysicalChecksWeb(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<Return> option = returnRepository.findById(t.getReturnId());
			if (option.isPresent()) {
				Return returnObject = option.get();
				returnObject.setClaimType(t.getClaimType());
				if (t.getClaimType().equals("Saleable")) {
					returnObject.setSalabletatus(StringIteration.CHECKEDD);
					returnObject.setNonSalabletatus("");

				} else if (t.getClaimType().equals("NonSaleable")) {
					returnObject.setNonSalabletatus(StringIteration.CHECKEDD);
					returnObject.setSalabletatus("");
				} else if (t.getClaimType().equals("Both")) {
					returnObject.setSalabletatus("CHECKED");
					returnObject.setNonSalabletatus("CHECKED");
				}

				returnObject.setNumberOfLineItems(t.getNumberOfLineItems());
				returnObject.setNumOfNonSalableCases(t.getNumOfNonSalableCases());
				CommonUtil.setModifiedOn(returnObject);
				returnRepository.save(returnObject);

				result.setCode("0000");
				result.setMessage("Successful Created");
				result.setData(t);

			} else {
				result.setCode("1111");
				result.setMessage("SerialNumber");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	public Result<Return> updatePhysicalReturnsWeb(Return t) {
		Result<Return> result = new Result<>();
		try {
			Optional<Return> option = returnRepository.findById(t.getReturnId());
			if (option.isPresent()) {
				Return returnObject = option.get();

				returnObject.setStockistId(t.getStockistId());
				returnObject.setStockistName(t.getStockistName());
				returnObject.setLocation(t.getLocation());
				returnObject.setTransporterId(t.getTransporterId());
				returnObject.setTransporterName(t.getTransporterName());
				returnObject.setLrNumber(t.getLrNumber());
				returnObject.setLrBookingDate(t.getLrBookingDate());
				returnObject.setClaimNumber(t.getClaimNumber());
				returnObject.setClaimDate(t.getClaimDate());
				returnObject.setReceivedDate(t.getReceivedDate());
				returnObject.setNumberOfBoxes(t.getNumberOfBoxes());
				CommonUtil.setModifiedOn(returnObject);
				returnRepository.save(returnObject);

				Optional<ReturnStatusHistory> his = returnStatusHistoryRepository
						.findByReturnIdForReceived(t.getReturnId());
				if (his.isPresent()) {
					ReturnStatusHistory rsh = his.get();
					rsh.setHistoryOn(t.getReceivedDate());
					returnStatusHistoryRepository.save(rsh);
				}
				result.setCode("0000");
				result.setMessage("Successful Created");
				result.setData(t);

			} else {
				result.setCode("1111");
				result.setMessage("SerialNumber");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}

		return result;
	}

	/*
	 * Return recived
	 */
//	public void returMail(Return returns, String adminMail, String stkMail) {
//
//		try {
//			EmailTemplate emailTemplate = emailTemplateService.findById(adminMail);
//			List<String> list = new ArrayList<>();
//			Optional<Stockist> stockist = stockistRepository.findById(returns.getStockistId());
//			List<String> name=new ArrayList<String>();
//
//			if (stockist.isPresent()) {
//				returnNotification(stockist.get().getStockistId(), stkMail);
//
//				if (emailTemplate != null) {
//					Optional<Tenant> tenant = tenantRepository.findById(stockist.get().getTenantId());
//					if (tenant.isPresent()) {
//						list.add(tenant.get().getEmailId());
//						name.add(tenant.get().getTenantName());
//						returnNotification(tenant.get().getTenantId(), adminMail);
//						Optional<Role> role = roleRepository.findByRoleName(StringIteration.MANAGER);
//						if (role.isPresent()) {
//							Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
//									role.get().getRoleId());
//							if (user.isPresent()) {
//								list.add(user.get().getEmail());
//								name.add(user.get().getFirstName()+" "+user.get().getFirstName());
//								returnNotification(user.get().getUserId(), adminMail);
//							}
//						}
//						
//						Optional<Role> roles = roleRepository.findByRoleName(StringIteration.USER);
//						if (roles.isPresent()) {
//							Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
//									roles.get().getRoleId());
//							if (user.isPresent()) {
//								list.add(user.get().getEmail());
//								name.add(user.get().getFirstName()+" "+user.get().getFirstName());
//								returnNotification(user.get().getUserId(), adminMail);
//							}
//						}
//						
//						
//						
//					}
//
//					VelocityContext context = new VelocityContext();
//					context.put("origin", ConfigUtil.getAppLink());
//					context.put("language", "en");
//					StringWriter writer = new StringWriter();
//					String templateStr = emailTemplate.getMailTemplate();
//					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
//					String[] s = list.toArray(new String[0]);
//					emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),name);
//				}
//
//				EmailTemplate emailTemplateStockist = emailTemplateService.findById(stkMail);
//				String[] str = { stockist.get().getEmail() };
//				if (emailTemplateStockist != null) {
//					Optional<Tenant> tenant = tenantRepository.findById(stockist.get().getTenantId());
//					
//					Optional<Role> role = roleRepository.findByRoleName(StringIteration.MANAGER);
//					if (role.isPresent()) {
//						Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
//								role.get().getRoleId());
//						if (user.isPresent()) {
//							list.add(user.get().getEmail());
//							name.add(user.get().getFirstName()+" "+user.get().getFirstName());
//							returnNotification(user.get().getUserId(), adminMail);
//						}
//					}
//					
//					Optional<Role> roles = roleRepository.findByRoleName(StringIteration.USER);
//					if (roles.isPresent()) {
//						Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
//								roles.get().getRoleId());
//						if (user.isPresent()) {
//							list.add(user.get().getEmail());
//							name.add(user.get().getFirstName()+" "+user.get().getFirstName());
//							returnNotification(user.get().getUserId(), adminMail);
//						}
//					}
//					
//
//					
//					VelocityContext context = new VelocityContext();
//					context.put("origin", ConfigUtil.getAppLink());
//					context.put("language", "en");
//					context.put("fullname", stockist.get().getFirstName());
//					StringWriter writer = new StringWriter();
//					String templateStr = emailTemplateStockist.getMailTemplate();
//					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
//					emailService.sendEmails(str, writer.toString(), emailTemplateStockist.getSubject(),name);
//				}
//			}
//		} catch (Exception e) {
//			logger.error(StringIteration.SPACE, e);
//		}
//	}

	public void returMailStockist(Return returns, User user, String stkMail) {
		EmailTemplate emailTemplate = emailTemplateService.findById(stkMail);

		if (emailTemplate != null) {
			Optional<Stockist> stockist = stockistRepository.findById(returns.getStockistId());
			if (stockist.isPresent()) {
				Stockist st = stockist.get();

				VelocityContext context = new VelocityContext();
				context.put(Constants.ORIGIN, ConfigUtil.getAppLink());
				context.put(StringIteration.LANGUAGE, Constants.EN);
				SimpleDateFormat sdf = new SimpleDateFormat(StringIteration.DDMMYY);
				context.put(StringIteration.RECIVED_DATE, sdf.format(returns.getReceivedDate()));
				context.put(StringIteration.CLAIMNUMBER, returns.getClaimNumber());
				context.put(StringIteration.STOCKISTNAME, st.getFirstName());
				StringWriter writer = new StringWriter();
				String templateStr = emailTemplate.getMailTemplate();
				Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
				String[] s = { st.getEmail() };
				emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(), st.getStockistName(), user);
			}

		}
	}

	public void returnNotification(String userId, String temp) {
		Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
		NotificationTemplate template = notificationTemplateService.findById(temp);
		String tid = null;
		if (user.isPresent()) {
			tid = user.get().getTenantId();
		}
		if (template != null) {
			notificationService.notifications(userId, template.getSubject(), template.getNotificationTemplate(), null,
					tid);
		}
	}

	@Override
	public Result<Return> delete(String id) {
		Result<Return> result = new Result<>();
		try {
			Optional<Return> optional = returnRepository.findById(id);
			if (optional.isPresent()) {
				returnRepository.deleteById(optional.get().getSerialNumber());
				result.setCode("0000");
				result.setMessage("Successful deleted");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	private void saveHistory(String serialNumber, String status, String returnId) {

		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (u.isPresent()) {
			ReturnStatusHistory history = new ReturnStatusHistory();
			history.setHistoryBy(u.get().getFirstName());
			history.setHistoryOn(new Date());
			history.setReturnId(returnId);
			history.setSerialNumber(serialNumber);
			history.setStatus(status);
			history.setTenantId(u.get().getTenantId());
			history.setId(System.currentTimeMillis() + "");
			returnStatusHistoryRepository.save(history);
		}
	}

	public Result<Object> getAllTransportStockist() {
		Result<Object> result = new Result<>();
		Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());

		try {
			if (user.isPresent()) {
				Map<String, Object> dataMap = new HashMap<>();
				List<Map<String, Object>> transportDataList = getTransportData(user.get());
				List<Map<String, Object>> stockistDataList = getStockistData(user.get());

				dataMap.put("transport", transportDataList);
				dataMap.put(StringIteration.STOCKISTNAME, stockistDataList);

				result.setData(dataMap);
				result.setCode("0000");
				result.setMessage("Successfully");
				return result;
			}
		} catch (Exception exc) {
			logger.error("Error", exc);
		}

		return result;
	}

	private List<Map<String, Object>> getTransportData(User user) {
		List<Transport> transports = transportRepository.findByTenantId(user.getTenantId());
		List<Map<String, Object>> transportDataList = new ArrayList<>();

		for (Transport transport : transports) {
			Map<String, Object> transportData = new HashMap<>();
			transportData.put("transportId", transport.getTransportId());
			transportData.put("transportName", transport.getTransportName());
			transportDataList.add(transportData);
		}

		return transportDataList;
	}

	private List<Map<String, Object>> getStockistData(User user) {
		List<Stockist> stockists = stockistRepository.findStockistByTenantId(user.getTenantId());
		List<Map<String, Object>> stockistDataList = new ArrayList<>();

		for (Stockist stockist : stockists) {
			Map<String, Object> stockistData = new HashMap<>();
			stockistData.put("stockistId", stockist.getStockistId());
			stockistData.put("stockistName", stockist.getStockistName());
			setStockistLocation(stockist, stockistData);
			setManufactureList(stockist, stockistData);
			stockistDataList.add(stockistData);
		}

		return stockistDataList;
	}

	private void setStockistLocation(Stockist stockist, Map<String, Object> stockistData) {
		if (stockist.getCityId() != null && stockist.getStateId() != null) {
			Optional<City> city = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
					stockist.getStateId());
			city.ifPresent(ct -> stockistData.put("stockistLocation", ct.getCityName()));
		}
	}

	private void setManufactureList(Stockist stockist, Map<String, Object> stockistData) {
		List<StockistManufacture> manufactures = stockist.getStockistManufacturesList();
		for (StockistManufacture manufacture : manufactures) {
			if (manufacture.getManufacture() != null) {
				Optional<Manufacturer> manufacturer = manufacturerRepository.findById(manufacture.getManufacture());
				manufacturer.ifPresent(mf -> manufacture.setManufactureName(mf.getManufacturerName()));
			}
		}
		stockistData.put("ManufactureList", manufactures);
	}

	public List<ClaimsReturnsReportDTO> claimsNotClosed(Map<String, String> params) {
		String stockist = "";
		String location = "";
		String status = "";
		String manufacturer = "";
		String sapId = "";
		String claimNumber = "";
		String lrDate = "";
		String daysTaken = "";
		String locality = "";
		try {
			List<Object[]> ob = new ArrayList<>();

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME).trim();
			}

			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}
			if (params.get(StringIteration.CLAIMNUMBER) != null) {
				claimNumber = params.get(StringIteration.CLAIMNUMBER).trim();
			}

			if (params.get("lrDate") != null) {
				lrDate = params.get("lrDate");
			}

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get("daysTaken") != null) {
				daysTaken = params.get("daysTaken").trim();
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					if (months != 0) {
						ob = returnRepository.claimsNotClosedMonth(us.getTenantId(), us.getUserId(), months);
					} else {
						try {
							ob = returnRepository.claimsNotClosed(us.getTenantId(), us.getUserId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					if (months != 0) {
						ob = returnRepository.claimsNotClosedMonth(us.getTenantId(), us.getHierarachyId(), months);
					} else {
						ob = returnRepository.claimsNotClosed(us.getTenantId(), us.getHierarachyId());
					}

				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					if (months != 0) {

						ob = returnRepository.claimsNotClosedSuperMonth(us.getTenantId(), months);
					} else {
						ob = returnRepository.claimsNotClosedSuperMonth(us.getTenantId());
					}

				}
			}

			List<ClaimsReturnsReportDTO> tr = claimsNotClosedOb(ob);
			tr = claimsNotClosedObFilter(tr, stockist, sapId, claimNumber, lrDate, status, manufacturer);
			tr = claimsNotClosedObFilters(tr, daysTaken, location, locality);

			return tr;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	private List<ClaimsReturnsReportDTO> claimsNotClosedObFilters(List<ClaimsReturnsReportDTO> tr, String daysTaken,
			String location, String locality) {
		if (!daysTaken.equals("")) {
			final String stk = daysTaken;

			tr = tr.stream().filter(obj -> obj.getDays().equals(stk)).collect(Collectors.toList());

		}

		if (!location.equals("")) {

			final String cities=location;
			
			

				tr = tr.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(cities)).collect(Collectors.toList());
			

		}
		if (!locality.equals("")) {
			final String local = locality;

			tr = tr.stream().filter(obj -> obj.getLocal().equalsIgnoreCase(local)).collect(Collectors.toList());
		}

		return tr;
	}

	private List<ClaimsReturnsReportDTO> claimsNotClosedObFilter(List<ClaimsReturnsReportDTO> tr, String stockist,
			String sapId, String claimNumber, String lrDate, String status, String manufacturer) {
		if (!stockist.equals("")) {
			final String stk = stockist.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getStockistName().toLowerCase().contains(stk))
					.collect(Collectors.toList());

		}
		if (!sapId.equals("")) {
			final String stk = sapId.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getSapId().toLowerCase().contains(stk)).collect(Collectors.toList());

		}
		if (!claimNumber.equals("")) {
			final String stk = claimNumber.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getClaimNumber().toLowerCase().contains(stk))
					.collect(Collectors.toList());

		}

		if (!lrDate.equals("")) {
			final String stk = lrDate.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getLrDate().contains(stk)).collect(Collectors.toList());

		}
		if (status != null && !status.isEmpty()) {
			final String sts = status;
			tr = tr.stream().filter(obj -> obj.getStatus().equals(sts)).collect(Collectors.toList());
		}

		if (manufacturer != null && !manufacturer.isEmpty()) {
			final String manufacuturers = manufacturer;
			tr = tr.stream().filter(obj -> obj.getManufacturer().contains(manufacuturers)).collect(Collectors.toList());
		}
		return tr;
	}

	private List<ClaimsReturnsReportDTO> claimsNotClosedOb(List<Object[]> ob) {
		List<ClaimsReturnsReportDTO> tr = new ArrayList<>();
		for (Object[] b : ob) {
			Optional<Manufacturer> manufacture = manufacturerRepository.findById(String.valueOf(b[6]));
			ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();
			td.setClaimNumber(String.valueOf(b[1]));
			td.setStatus(String.valueOf(b[2]));
			td.setLrDate(String.valueOf(b[5]));
			td.setStockistName(String.valueOf(b[8]));
			if (manufacture.isPresent()) {
				td.setManufacturer(manufacture.get().getManufacturerName());
			}
			td.setDays(String.valueOf(b[4]));
			td.setLocation(String.valueOf(b[10]));
			td.setSapId(String.valueOf(b[11]));
			if (b[12] != null) {
				td.setLocal(listvalue.findByCodeAndName(String.valueOf(b[12])));
			} else {
				td.setLocal(" -");
			}
			Optional<String> city = cityRepository.findByCityCode(td.getLocation());
			if (city.isPresent()) {
				td.setLocation(city.get());
			}

			tr.add(td);
		}

		return tr;
	}

	public List<ClaimsReturnsReportDTO> receivedNotChecked(Map<String, String> params) {
		try {

			List<Object[]> ob = new ArrayList<>();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
			LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
			if (params.get(StringIteration.FROMDATE) != null) {
				startDate = LocalDate.parse(params.get(StringIteration.FROMDATE), formatter).atStartOfDay();
			}
			if (params.get(StringIteration.TODATE) != null) {
				endDate = LocalDate.parse(params.get(StringIteration.TODATE), formatter).atTime(23, 59, 59);
			}

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent() && us.get().getRoleName().equals(StringIteration.MANAGER)) {
				ob = returnRepository.receivedNotChecked(us.get().getTenantId(), us.get().getUserId(), startDate,
						endDate, new Date());
			} else if (us.isPresent() && us.get().getRoleName().equals("USER")) {

				ob = returnRepository.receivedNotChecked(us.get().getTenantId(), us.get().getHierarachyId(), startDate,
						endDate, new Date());
			}

			List<ClaimsReturnsReportDTO> tr = new ArrayList<>();
			for (Object[] b : ob) {
				ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();
				td.setClaimNumber(String.valueOf(b[0]));
				td.setDays(String.valueOf(b[4]));
				td.setStatus(String.valueOf(b[1]));
				td.setReceivedDate(String.valueOf(b[2]));
				td.setStockistName(String.valueOf(b[3]));
				td.setManufacturer(String.valueOf(b[5]));
				tr.add(td);

			}
			return tr;

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<ClaimsReturnsReportDTO> secondCheckNotCompleted(Map<String, String> params) {
		try {
			List<Object[]> ob = new ArrayList<>();
			String location = "";
			String transporter = "";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
			LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
			if (params.get("fromDate") != null) {
				startDate = LocalDate.parse(params.get("fromDate"), formatter).atStartOfDay();
			}
			if (params.get("toDate") != null) {
				endDate = LocalDate.parse(params.get("toDate"), formatter).atTime(23, 59, 59);
			}

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get("transporter") != null) {
				transporter = params.get("transporter");
			}

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent() && us.get().getRoleName().equals(StringIteration.MANAGER)) {
				ob = returnRepository.secondCheckNotCompleted(us.get().getTenantId(), us.get().getUserId(), startDate,
						endDate, new Date());
			}
			if (us.isPresent() && us.get().getRoleName().equals(StringIteration.MANAGER)) {
				ob = returnRepository.secondCheckNotCompleted(us.get().getTenantId(), us.get().getHierarachyId(),
						startDate, endDate, new Date());
			}

			List<ClaimsReturnsReportDTO> tr = new ArrayList<>();
			for (Object[] b : ob) {
				ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();
				td.setClaimNumber(String.valueOf(b[0]));
				td.setGrrnDate(String.valueOf(b[1]));
				td.setStockistName(String.valueOf(b[2]));
				td.setTransporter(String.valueOf(b[3]));
				td.setLocation(String.valueOf(b[4]));
				td.setDays(String.valueOf(b[5]));
				td.setManufacturer(String.valueOf(b[6]));
				tr.add(td);
			}
			if (!location.equals("")) {
				String city = location;
				tr = tr.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(city))
						.collect(Collectors.toList());

			}

			if (!transporter.equals("")) {
				String transport = transporter;
				tr = tr.stream().filter(obj -> obj.getTransporter().toLowerCase().contains(transport))
						.collect(Collectors.toList());

			}

			return tr;

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<ClaimsReturnsReportDTO> returndetailsReport(Map<String, String> params) {
		try {

			List<Object[]> ob = new ArrayList<>();

			String claimNumber = "";
			String status = "";
			String location = "";
			String stockistName = "";
			String manufacturer = "";
			String sapId = "";
			String grrnDate = "";
			String cnDate = "";
			String grrnNumber = "";
			String cnNumber = "";
			String claimType = "";
			String recieveDate = "";
			String locality = "";

			if (params.get("status") != null) {
				status = params.get("status");
			}
			if (params.get("stockist") != null) {
				stockistName = params.get("stockist").trim();
			}
			if (params.get("location") != null) {
				location = params.get("location");
			}
			if (params.get("manufacturer") != null) {
				manufacturer = params.get("manufacturer").trim();
			}
			if (params.get("sapId") != null) {
				sapId = params.get("sapId").trim();
			}
			if (params.get("claimNumber") != null) {
				claimNumber = params.get("claimNumber").trim();
			}
			if (params.get("recieveDate") != null) {
				recieveDate = params.get("recieveDate");
			}
			if (params.get("grrnDate") != null) {
				grrnDate = params.get("grrnDate");
			}
			if (params.get("cnDate") != null) {
				cnDate = params.get("cnDate");
			}
			if (params.get("grrnNumber") != null) {
				grrnNumber = params.get("grrnNumber").trim();
			}
			if (params.get("cnNumber") != null) {
				cnNumber = params.get("cnNumber").trim();
			}
			if (params.get("claimType") != null) {
				claimType = params.get("claimType");
			}

			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent() && us.get().getRoleName().equals(StringIteration.MANAGER)) {
				if (months != 0) {
					ob = claimRepository.returnsDetatilReportMonth(us.get().getTenantId(), us.get().getUserId(), months);
				} else {
					ob = claimRepository.returnsDetatilReport(us.get().getTenantId(), us.get().getUserId());
				}

			} else if (us.isPresent() && us.get().getRoleName().equals(StringIteration.USER)) {

				if (months != 0) {
					ob = claimRepository.returnsDetatilReportMonth(us.get().getTenantId(), us.get().getHierarachyId(), months);
				} else {
					ob = claimRepository.returnsDetatilReport(us.get().getTenantId(), us.get().getHierarachyId());
				}

			} else if (us.isPresent() && us.get().getRoleName().equals(StringIteration.SUPERADMIN)) {
				if (months != 0) {
					ob = claimRepository.returnsDetatilReportSuperMonth(us.get().getTenantId(), months);
				} else {
					ob = claimRepository.returnsDetatilReportSuper(us.get().getTenantId());
				}

			}

			List<ClaimsReturnsReportDTO> tr = new ArrayList<>();

			for (Object[] b : ob) {

				ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();

				td.setStockistName(String.valueOf(b[0]));
				td.setLocation(String.valueOf(b[1]));
				td.setSapId(String.valueOf(b[2]));
				td.setClaimNumber(String.valueOf(b[3]));
				if (b[4] != null) {

					td.setReceivedDate(String.valueOf(b[4]));
				} else {
					td.setReceivedDate("   -");
				}

				td.setClaimType(String.valueOf(b[6]));
				td.setStatus(String.valueOf(b[5]));
				td.setManufacturer(String.valueOf(b[7]));
				if (b[6] != null) {
					td.setClaimType(String.valueOf(b[6]));
				} else {

					td.setClaimType("   -");
				}
				if (b[8] != null) {
					td.setTransporter(String.valueOf(b[8]));
				} else {
					td.setTransporter("-");
				}

				if (b[9] != null) {
					td.setGrrnDate(String.valueOf(b[9]));
				} else {
					td.setGrrnDate("  -");
				}
				if (b[10] != null) {
					td.setCnDate(String.valueOf(b[10]));
				} else {
					td.setCnDate("  - ");
				}

				if (b[11] != null) {
					td.setGRRNamount(String.valueOf(b[11]));
				} else {
					td.setGRRNamount("-");
				}
				if (b[12] != null) {
					td.setCnAmount(String.valueOf(b[12]));
				} else {
					td.setCnAmount(" - ");
				}
				if (b[13] != null) {
					td.setGRRNNumber(String.valueOf(b[13]));
				} else {
					td.setGRRNNumber("  - ");
				}
				if (b[14] != null) {
					td.setCNNumber(String.valueOf(b[14]));
				} else {
					td.setCNNumber("   -");
				}
				if (b[15] != null) {
					td.setLocal(listvalue.findByCodeAndName(String.valueOf(b[15])));
				} else {
					td.setLocal(" -");
				}
				if (b[16] != null) {
			           td.setLrbookingDate(String.valueOf(b[16]));
			    } else {
			     td.setLrbookingDate(" -");
			    }
			    
			    if (b[17] != null) {
			            td.setLrNumber(String.valueOf(b[17]));
			     } else {
			      td.setLrNumber(" -");
			     }

			    if (b[18] != null) {
			            td.setNoOfBox(String.valueOf(b[18]));
			     } else {
			      td.setNoOfBox(" -");
			     }
			    
			    if (b[19] != null) {
			            td.setClaimDate(String.valueOf(b[19]));
			     } else {
			      td.setClaimDate(" -");
			     }

				Optional<String> city = cityRepository.findByCityCode(td.getLocation());
				if (city.isPresent()) {
					td.setLocation(city.get());
				}

				tr.add(td);

			}

			if (!stockistName.equals("")) {
				final String stokist = stockistName;
				tr = tr.stream().filter(obj -> obj.getStockistName().contains(stokist)).collect(Collectors.toList());

			}

			if (!status.equals("")) {
				final String city = status;
				tr = tr.stream().filter(obj -> obj.getStatus().equals(city)).collect(Collectors.toList());
			}

			if (manufacturer != null && !manufacturer.isEmpty()) {
				final String city = manufacturer;
				tr = tr.stream().filter(obj -> obj.getManufacturer().contains(city)).collect(Collectors.toList());
			}
			if (!sapId.equals("")) {
				final String stk = sapId.toLowerCase();
				tr = tr.stream().filter(obj -> obj.getSapId().toLowerCase().contains(stk)).collect(Collectors.toList());

			}
			if (!claimNumber.equals("")) {
				final String stk = claimNumber.toLowerCase();
				tr = tr.stream().filter(obj -> obj.getClaimNumber().toLowerCase().contains(stk))
						.collect(Collectors.toList());

			}

			if (!grrnNumber.equals("")) {
				final String stk = grrnNumber.toLowerCase();
				tr = tr.stream().filter(obj -> obj.getGRRNNumber().toLowerCase().contains(stk))
						.collect(Collectors.toList());

			}
			if (!cnNumber.equals("")) {
				final String stk = cnNumber.toLowerCase();
				tr = tr.stream().filter(obj -> obj.getCNNumber().toLowerCase().contains(stk))
						.collect(Collectors.toList());

			}

			if (!grrnDate.equals("")) {
				final String stk = grrnDate;
				tr = tr.stream().filter(obj -> obj.getGrrnDate().contains(stk)).collect(Collectors.toList());

			}
			if (!cnDate.equals("")) {
				final String stk = cnDate;
				tr = tr.stream().filter(obj -> obj.getCnDate().contains(stk)).collect(Collectors.toList());

			}

			if (!recieveDate.equals("")) {
				final String stk = recieveDate;
				tr = tr.stream().filter(obj -> obj.getReceivedDate().contains(stk)).collect(Collectors.toList());

			}

			if (!claimType.equals("")) {
				final String stk = claimType;
				tr = tr.stream().filter(obj -> obj.getClaimType().equals(stk)).collect(Collectors.toList());

			}
			  if (!location.equals("")) {
		         	
		         	final String city=location;
		             
		            tr = tr.stream()
		                     .filter(obj -> obj.getLocation().equalsIgnoreCase(city))
		                      .collect(Collectors.toList());
		             
		             }
			if (!locality.equals("")) {
				final String local = locality;

				tr = tr.stream().filter(obj -> obj.getLocal().equalsIgnoreCase(local)).collect(Collectors.toList());
			}

			return tr;

		} catch (Exception e) {

			logger.error("", e);

		}

		return new ArrayList<>();
	}

	public Result<Object> fileReader(MultipartFile file, String sheetIndex) throws IOException {

		Result<Object> result = new Result<>();
		List<ReturnNote> valid = new ArrayList<>();
		List<ReturnNote> invalid = new ArrayList<>();

		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
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
					ReturnNote rn = new ReturnNote();
					CommonUtil.setCreatedOn(rn);
					CnImport cnImport = new CnImport(true, row, rn);

					prepareRows(cnImport, row, rn);
					validation(rn, cnImport, u);

					if (cnImport.isFlag()) {
						valid.add(rn);
					} else {
						rn.setRemarks(cnImport.getMessage());
						invalid.add(rn);
					}
					rowIndex++;
				}

				saveExcel(valid, result, invalid, u);
				writeInvalidExcel(invalid, result);
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

	void prepareRows(CnImport cn, Row row, ReturnNote rn) {
		try {
			prepareManufacturer(cn, row, rn);
			prepareGrrnNumber(cn, row, rn);
			prepareCnNumber(cn, row, rn);
			prepareCnAmount(cn, row, rn);
			prepareCnDate(cn, row, rn);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prepareManufacturer(CnImport cn, Row row, ReturnNote rn) {
		try {
			Cell manf = row.getCell(0);
			if (manf == null || manf.getCellType() == CellType.BLANK) {
				cn.setFlag(false);
				cn.setMessage("Manufacturer can not be empty");
			} else {
				String name = manf.getStringCellValue().trim();
				rn.setManufacturer(name);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prepareGrrnNumber(CnImport cn, Row row, ReturnNote rn) {
		try {
			Cell grrn = row.getCell(1);
			if (grrn == null || grrn.getCellType() == CellType.BLANK) {
				cn.setFlag(false);
				cn.setMessage("Grrn number can not be empty");
			} else {
				if (grrn.getCellType() == CellType.NUMERIC) {
					double numericValue = grrn.getNumericCellValue();
					long longValue = (long) numericValue;
					rn.setGrrnNumber(String.valueOf(longValue).trim());
				} else if (grrn.getCellType() == CellType.STRING) {
					rn.setGrrnNumber(grrn.getStringCellValue().trim());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prepareCnNumber(CnImport cn, Row row, ReturnNote rn) {
		try {
			Cell cnNo = row.getCell(2);
			if (cnNo == null || cnNo.getCellType() == CellType.BLANK) {
				cn.setFlag(false);
				cn.setMessage("Cn number can not be empty");
			} else {
				if (cnNo.getCellType() == CellType.NUMERIC) {
					double numericValue = cnNo.getNumericCellValue();
					long longValue = (long) numericValue;
					rn.setNoteNumber(String.valueOf(longValue).trim());
				} else if (cnNo.getCellType() == CellType.STRING) {
					rn.setNoteNumber(cnNo.getStringCellValue().trim());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prepareCnAmount(CnImport cn, Row row, ReturnNote rn) {
		try {
			Cell cnAm = row.getCell(3);
			if (cnAm == null || cnAm.getCellType() == CellType.BLANK) {
				cn.setFlag(false);
				cn.setMessage("Cn amount can not be empty");
			} else {
				if (cnAm.getCellType() == CellType.NUMERIC) {
					double numericValue = cnAm.getNumericCellValue();
					long longValue = (long) numericValue;
					rn.setLineItem((int) longValue);
				} else if (cnAm.getCellType() == CellType.STRING) {
					String amount = cnAm.getStringCellValue();
					rn.setLineItem(Integer.parseInt(amount));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void prepareCnDate(CnImport cn, Row row, ReturnNote rn) {
		try {
			Cell date = row.getCell(4);
			if (date == null || date.getCellType() == CellType.BLANK) {
				cn.setFlag(false);
				cn.setMessage("Cn date can not be empty");
			} else {
				if (date.getCellType() == CellType.NUMERIC) {
					rn.setNoteDate(date.getDateCellValue());
				} else if (date.getCellType() == CellType.STRING) {
					checkDate(date.getStringCellValue().trim(), cn, rn);
					if (rn.getNoteDate() == null) {
						rn.setInvalidDate(date.getStringCellValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void checkDate(String dateStr, CnImport cn, ReturnNote rn) {
		try {
			if (dateStr.contains("/")) {
				dateStr = dateStr.replace("/", "-");
			}
			Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
			rn.setNoteDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			cn.setFlag(false);
			cn.setMessage("Date format should be in dd-mm-yyyy or dd/mm/yyyy");
		}
	}

	void validation(ReturnNote rn, CnImport cn, User u) {
		try {
			if (rn.getManufacturer() != null) {
				Optional<Manufacturer> manf = manufacturerRepository
						.findByManufacturerNameAndStatus(rn.getManufacturer().replace(" ", "").toLowerCase());
				if (manf.isPresent()) {
					Optional<TenantManufacture> tm = tenantManufactureRepository
							.findByManufactureIdAndTenantAndStatus(manf.get().getManufacturerId(), u.getTenantId());
					if (tm.isPresent()) {
						Optional<ReturnNote> n = returnNoteRepository.findGrrnNumberDuplicateByManufacturer(
								u.getTenantId(), manf.get().getManufacturerId(), rn.getGrrnNumber());
						if (n.isPresent()) {
							ReturnNote note = n.get();
							rn.setReturnId(note.getReturnId());
							rn.setSerialNumber(note.getSerialNumber());
							rn.setClaimType(note.getClaimType());
							rn.setNoteType("CN");

						} else {
							cn.setFlag(false);
							cn.setMessage(" Grrn number not found");
						}
					} else {
						cn.setFlag(false);
						cn.setMessage(
								" Manfacturer not mapped with this Tenant OR Manufacture is not activated for this tenant ");
					}
				} else {
					cn.setFlag(false);
					cn.setMessage(" Manufacturer not found");
				}
			} else {
				cn.setFlag(false);
				cn.setMessage("");
				;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	void saveExcel(List<ReturnNote> returnNote, Result<Object> result, List<ReturnNote> invalid, User u) {

		if (!returnNote.isEmpty()) {
			List<ReturnNote> saleable = new ArrayList<>();
			List<ReturnNote> nonSaleable = new ArrayList<>();

			for (ReturnNote rnote : returnNote) {
				Optional<ReturnNote> _rNote = returnNoteRepository.findCnDuplicate(u.getTenantId(), rnote.getReturnId(),
						rnote.getNoteNumber());
				if (_rNote.isPresent()) {
					rnote.setRemarks("Duplicate CN Number");
					invalid.add(rnote);
					continue;
				}

				if (rnote.getClaimType().equals(StringIteration.SALEABLE)) {
					saleable.add(rnote);
				} else {
					nonSaleable.add(rnote);
				}
			}

			if (!saleable.isEmpty()) {
				saveSalableCn(saleable);
			}

			if (!nonSaleable.isEmpty()) {
				saveNonSalableCn(nonSaleable);
			}

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage("saved successfully");
		} else {
			result.setCode(StringIteration.ERROR_CODE4);
			result.setMessage("no valid cn found");
		}
	}

	void saveSalableCn(List<ReturnNote> salable) {
		Map<String, List<ReturnNote>> list = salable.stream()
				.collect(Collectors.groupingBy(ReturnNote::getReturnId, LinkedHashMap::new, Collectors.toList()));

		for (String key : list.keySet()) {

			Optional<Return> rn = returnRepository.findById(list.get(key).get(0).getReturnId());
			if (rn.isPresent() && rn.get().getSalabletatus().equals(StringIteration.CN_CREATED)) {
				returnNoteService.create(list.get(key));
			} else {
				updateReturnSalable(list.get(key));
			}
		}
	}

	void saveNonSalableCn(List<ReturnNote> nonSalable) {
		Map<String, List<ReturnNote>> list = nonSalable.stream()
				.collect(Collectors.groupingBy(ReturnNote::getReturnId, LinkedHashMap::new, Collectors.toList()));
		for (String key : list.keySet()) {
			Optional<Return> rn = returnRepository.findById(list.get(key).get(0).getReturnId());
			if (rn.isPresent() && rn.get().getNonSalabletatus().equals(StringIteration.CN_CREATED)) {
				returnNoteService.create(list.get(key));
			} else {
				updateReturnNonSalable(list.get(key));
			}
		}
	}

	void writeInvalidExcel(List<ReturnNote> invalid, Result<Object> result) {

		try (XSSFWorkbook workbook1 = new XSSFWorkbook()) {
			if (!invalid.isEmpty()) {
				String[] headerList = { "Manufacturer name", "GRRN Number", "Cn Number", "Cn Amount ", "Cn Date",
						"Remarks" };
				XSSFSheet spreadsheet = workbook1.createSheet(" Invalid Cn Excel ");

				int rowId = 0;
				XSSFRow row1;
				row1 = spreadsheet.createRow(rowId++);
				int r = 0;
				for (String s : headerList) {
					row1.createCell(r).setCellValue(s);
					r++;
				}

				for (ReturnNote returnN : invalid) {
					row1 = spreadsheet.createRow(rowId++);
					row1.createCell(0).setCellValue(returnN.getManufacturer());
					row1.createCell(1).setCellValue(returnN.getGrrnNumber());
					row1.createCell(2).setCellValue(returnN.getNoteNumber());
					row1.createCell(3).setCellValue(returnN.getLineItem());
					SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
					String cellValue = "";
					if (returnN.getNoteDate() != null) {
						cellValue = date.format(returnN.getNoteDate());
					} else {
						cellValue = returnN.getInvalidDate();
					}
					row1.createCell(4).setCellValue(cellValue);
					row1.createCell(5).setCellValue(returnN.getRemarks());
				}

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				workbook1.write(byteArrayOutputStream);
				InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

				String fileId = UUID.randomUUID().toString();

				com.healthtraze.etraze.api.file.model.File invalidCheques = fileStorageService.uploadFileToAWS(fileId,
						fileId, byteArrayInputStream, "invalidCN.xlsx", "document", 0);

				result.setCode(StringIteration.ERROR_CODE1);
				result.setData(invalidCheques);
				result.setMessage("invalid data found");

				byteArrayOutputStream.flush();
				byteArrayOutputStream.close();
			} else {
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("No invalid found");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
