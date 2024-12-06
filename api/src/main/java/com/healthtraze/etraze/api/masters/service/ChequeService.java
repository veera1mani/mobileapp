package com.healthtraze.etraze.api.masters.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.ChequeDTO;
import com.healthtraze.etraze.api.masters.dto.ChequeData;
import com.healthtraze.etraze.api.masters.dto.ChequeDetailDTO;
import com.healthtraze.etraze.api.masters.dto.ChequeDropDownDTO;
import com.healthtraze.etraze.api.masters.dto.ChequeImportDto;
import com.healthtraze.etraze.api.masters.dto.ChequeListDTO;
import com.healthtraze.etraze.api.masters.dto.PaginationDTO;
import com.healthtraze.etraze.api.masters.dto.StockistChequeDetail;
import com.healthtraze.etraze.api.masters.model.Bank;
import com.healthtraze.etraze.api.masters.model.Cheque;
import com.healthtraze.etraze.api.masters.model.ChequeCardView;
import com.healthtraze.etraze.api.masters.model.ChequeStatusHistory;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.StockistManufacture;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.repository.BankRepository;
import com.healthtraze.etraze.api.masters.repository.ChequeCardViewRepository;
import com.healthtraze.etraze.api.masters.repository.ChequeRepository;
import com.healthtraze.etraze.api.masters.repository.ChequeStatusHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManagerManufacturerMappingRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.StockistManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderInvoiceRepository;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.Role;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class ChequeService implements BaseService<Cheque, Long> {

	private Logger logger = LogManager.getLogger(ChequeService.class);
	
	private int limit=100;

	private final ChequeRepository chequeRepository;

	private final StockistRepository stockistRepository;

	private final BankRepository bankRepository;

	private final EmailTemplateService emailTemplateService;

	private final EmailService emailService;

	private final ChequeStatusHistoryRepository chequeStatusHistoryRepository;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ManufacturerRepository manufacturerRepository;

	private final StockistManufactureRepository stockistManufactureRepository;

	private final CityRepository cityRepository;

	private final TenantRepository tenantRepository;

	private final ListValueRepository listValueRepository;

	private final FileStorageService fileStorageService;

	private final TicketOrderInvoiceRepository orderInvoiceRepository;
	
	private final ChequeCardViewRepository cardViewRepository;
	
	private final ManagerManufacturerMappingRepository managerManufacturerMappingRepository;

	@Autowired
	public ChequeService(ChequeRepository chequeRepository, StockistRepository stockistRepository,
			BankRepository bankRepository, EmailTemplateService emailTemplateService, EmailService emailService,
			ChequeStatusHistoryRepository chequeStatusHistoryRepository, UserRepository userRepository,
			RoleRepository roleRepository, ManufacturerRepository manufacturerRepository,
			StockistManufactureRepository stockistManufactureRepository, CityRepository cityRepository,
			TenantRepository tenantRepository,
			com.healthtraze.etraze.api.masters.repository.ListValueRepository listValueRepository,
			FileStorageService fileStorageService, TicketOrderInvoiceRepository orderInvoiceRepository,
			ChequeCardViewRepository cardViewRepository, ManagerManufacturerMappingRepository managerManufacturerMappingRepository) {
		this.chequeRepository = chequeRepository;
		this.stockistRepository = stockistRepository;
		this.bankRepository = bankRepository;
		this.emailTemplateService = emailTemplateService;
		this.emailService = emailService;
		this.chequeStatusHistoryRepository = chequeStatusHistoryRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.stockistManufactureRepository = stockistManufactureRepository;
		this.cityRepository = cityRepository;
		this.tenantRepository = tenantRepository;
		this.listValueRepository = listValueRepository;
		this.fileStorageService = fileStorageService;
		this.orderInvoiceRepository = orderInvoiceRepository;
		this.cardViewRepository = cardViewRepository;
		this.managerManufacturerMappingRepository = managerManufacturerMappingRepository;
	}

	public StockistChequeDetail getStockistChequeDetail(String stockistId, String sapId) {
		StockistChequeDetail chequeDetail = new StockistChequeDetail();
		try {
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User currentUser = user.get();
				Optional<Stockist> stockistOption = getStockist(stockistId, currentUser.getTenantId());
				stockistOption.ifPresent(stockist -> {
					setStockistCityName(stockist);
					Optional<StockistManufacture> stockistManufactureOption = getStockistManufacture(sapId, stockistId,
							currentUser.getTenantId());
					stockistManufactureOption.ifPresent(stockistManufacture -> {
						setManufactureName(stockistManufacture);
						stockist.setStockistManufacture(stockistManufacture);
					});
					List<Cheque> cheques = getCheques(stockistId, sapId, currentUser.getTenantId());
					int inHandCount = getInHandChequeCount(sapId, currentUser.getTenantId());
					setChequeDetails(chequeDetail, cheques, inHandCount, stockist);
				});
				
				
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return chequeDetail;
	}
	
	public  Result<ChequeDTO>  getStockistsCheques(String stockistId,String sapId) {
		Result<ChequeDTO> result = new Result<>();
		ChequeDTO dto = new  ChequeDTO();		
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		try {
			if(us.isPresent()) {
				User u = us.get();
				Optional<StockistManufacture> sm = stockistManufactureRepository.findBySap(sapId, stockistId, u.getTenantId());
				if(sm.isPresent()) {
					StockistManufacture s = sm.get();
					
					Optional<Manufacturer> man = manufacturerRepository.findById(s.getManufacture());
					man.ifPresent(ma ->s.setManufactureName(ma.getManufacturerName()));
					dto.setManufacture(s.getManufactureName());
					dto.setThreshold(s.getThreshold());
					dto.setCategory(s.getChequeCategory());
					dto.setSapId(s.getSapId());				
				}
				Optional<Stockist> stockist = stockistRepository.findById(stockistId);
				if(stockist.isPresent()) {
					Stockist s = stockist.get();
					setStockistCityName(s);
					dto.setStockistName(s.getStockistName());
					dto.setLocation(s.getCityName());
					dto.setStatus(s.getStatus());
				}
				dto.setInhand(chequeRepository.getInHandChequeCount(stockistId,sapId));
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(dto);
				return result;
			}
		} catch(Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
		return result ;
	}
	
	
	
	public Result<Object> findAdvanceCheques(String stockistId , String sapId, int page, String search ){
		Result<Object> result = new Result<>();
		HashMap<String, Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Pageable pageable = PageRequest.of(page, 10);
			if(us.isPresent()) {
				User u = us.get();
				Page<Cheque> cheques = chequeRepository.findByStockistIdForAdvance(stockistId, sapId, u.getTenantId(),pageable, search);
				cheques.forEach(this::setChequeBankName);	
				map.put("cheques", cheques);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
			}
		}catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
				
		return result;
	}
	
	public Result<Object> findSecurityCheques(String stockistId , String sapId, int page, String search ){
		Result<Object> result = new Result<>();
		HashMap<String, Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Pageable pageable = PageRequest.of(page, 5);
			if(us.isPresent()) {
				User u = us.get();
				Page<Cheque> cheques = chequeRepository.findByStockistIdForSecurity(stockistId, sapId, u.getTenantId(),pageable,search);
				cheques.forEach(this::setChequeBankName);				
				
				map.put("cheques", cheques);
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
			}
		}catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
				
		return result;
	}
	
	public Result<Object> findCancelledCheques(String stockistId , String sapId, int page , String search){
		Result<Object> result = new Result<>();
		HashMap<String, Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Pageable pageable = PageRequest.of(page, 5);
			if(us.isPresent()) {
				User u = us.get();
				Page<Cheque> cheques = chequeRepository.findByStockistIdForCancel(stockistId, sapId, u.getTenantId(),pageable,search);
				cheques.forEach(this::setChequeBankName);				
				
				map.put("cheques", cheques);
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
			}
		}catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
				
		return result;
	}
	
	public Result<Object> findOutwardCheques(String stockistId , String sapId, int page, String search){
		Result<Object> result = new Result<>();
		HashMap<String, Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Pageable pageable = PageRequest.of(page, 5);
			if(us.isPresent()) {
				User u = us.get();
				Page<Cheque> cheques = chequeRepository.findByStockistIdForOutward(stockistId, sapId, u.getTenantId(),pageable,search);
				cheques.forEach(this::setChequeBankName);				
				
				map.put("cheques", cheques);
				
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
			}
		}catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
				
		return result;
	}
	
	

	private Optional<Stockist> getStockist(String stockistId, String tenantId) {
		return stockistRepository.findStockistByTenant(stockistId, tenantId);
	}

	private void setStockistCityName(Stockist stockist) {
		if (stockist.getCityId() != null && stockist.getStateId() != null) {
			Optional<City> city = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
					stockist.getStateId());
			city.ifPresent(ct -> stockist.setCityName(ct.getCityName()));
		}
	}

	private Optional<StockistManufacture> getStockistManufacture(String sapId, String stockistId, String tenantId) {
		return stockistManufactureRepository.findBySap(sapId, stockistId, tenantId);
	}

	private void setManufactureName(StockistManufacture stockistManufacture) {
		Optional<Manufacturer> manufacturer = manufacturerRepository.findById(stockistManufacture.getManufacture());
		manufacturer.ifPresent(man -> stockistManufacture.setManufactureName(man.getManufacturerName()));
	}

	private List<Cheque> getCheques(String stockistId, String sapId, String tenantId) {
		List<Cheque> cheques = chequeRepository.findByStockistId(stockistId, sapId, tenantId);
		cheques.forEach(this::setChequeBankName);
		return cheques;
	}

	private void setChequeBankName(Cheque cheque) {
		if (cheque.getBankId() != null) {
			Optional<Bank> bank = bankRepository.findById(cheque.getBankId());
			bank.ifPresent(b -> cheque.setBankName(b.getName()));
		}
	}

	private int getInHandChequeCount(String sapId, String tenantId) {
		return chequeRepository.chequeInHand(sapId, tenantId);
	}

	private void setChequeDetails(StockistChequeDetail chequeDetail, List<Cheque> cheques, int inHandCount,
			Stockist stockist) {
		Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
		user.ifPresent(u -> {
			Optional<Role> role = roleRepository.findById(u.getRoleId());
			role.ifPresent(r -> chequeDetail.setUser(r.getRoleName()));
			chequeDetail.setCheques(cheques);
			chequeDetail.setInHand(inHandCount);
			chequeDetail.setStockist(stockist);
		});
	}

	@Override
	public List<Cheque> findAll() {
		try {
			return chequeRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	public Map<String,Object> getAllChequeNumberByUser(String number) {
		try {
			Map<String,Object> map = new HashMap<>();
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();

				
				if (StringUtils.isNullOrEmpty(number)) {
					number="%%";
				}
							
				List<Object[]> list = getDropDownCheque(u, number);

				if(list.size()>getLimit()) {
					map.put("limit","false");
					map.put("value",Collections.emptyList());
				}else {
					map.put("limit","true");
					map.put("value",list);
				}
				
				return map;
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyMap();
	}
	
	public String getUser(User u) {
		try {
			if (u.getRoleName().equals(StringIteration.MANAGER)) {
				return u.getUserId();
			} else if (u.getRoleName().equals(StringIteration.USER)) {
				return u.getHierarachyId();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}
	
	public List<Object[]> getDropDownCheque(User u,String number) {
		try {
			if(u.getRoleName().equals(StringIteration.MANAGER) ||u.getRoleName().equals(StringIteration.USER)) {
				List<String> manList = managerManufacturerMappingRepository.findAllManufacturBYUserId(getUser(u),u.getTenantId());
				List<String> sm = stockistManufactureRepository.findAllStockistMan(u.getTenantId(), manList);	
			 return chequeRepository.getAllChequeNumberByUsers(sm,u.getTenantId(),number.trim());
			}
			 else if (u.getRoleName().equals(StringIteration.STOCKIST)) {
				return chequeRepository.getAllChequeNumberByStockist(u.getUserId(), u.getTenantId(),number);
			}	
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Collections.emptyList();
	}

	public List<Cheque> findChequeByChequeNumber(String chequeNumber) {
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Stockist s = stockistRepository.findByUserId(u.getUserId(), u.getTenantId());
				List<Cheque> ch = chequeRepository.findByChequeNumber(chequeNumber, s.getStockistId(), u.getTenantId());
				for (Cheque c : ch) {
					Optional<Bank> b = bankRepository.findById(c.getBankId());
					if (b.isPresent()) {
						c.setBankName(b.get().getName());
					}
					if (!ch.isEmpty()) {
						return ch;
					}

				}
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@Override
	public Cheque findById(Long id) {
		try {
			Optional<Cheque> option = chequeRepository.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public List<ChequeStatusHistory> findByChequeId(Long id) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				return chequeStatusHistoryRepository.findchequeStatusHistory(id, u.getTenantId());
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	public Result<ChequeDetailDTO> findChequDetails(Long id) {
		Result<ChequeDetailDTO> result = new Result<>();
		try {
			Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				ChequeDetailDTO chequeDetailDTO = createChequeDetailDTO(id, user);
				result.setData(chequeDetailDTO);
				result.setCode(chequeDetailDTO.getCheque() != null ? StringIteration.SUCCESS_CODE
						: StringIteration.ERROR_CODE1);
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	private ChequeDetailDTO createChequeDetailDTO(Long id, User user) {
		ChequeDetailDTO chequeDetailDTO = new ChequeDetailDTO();
		Cheque cheque = fetchCheque(id, user);
		if (cheque != null) {
			chequeDetailDTO.setCheque(cheque);
			chequeDetailDTO.setHistory(fetchChequeStatusHistory(id, user));
			chequeDetailDTO.setStockist(fetchStockist(cheque, user));
		}
		return chequeDetailDTO;
	}

	private Cheque fetchCheque(Long id, User user) {
		Optional<Cheque> chequeOptional = chequeRepository.findChequeByTenant(id, user.getTenantId());
		if (chequeOptional.isPresent()) {
			Cheque cheque = chequeOptional.get();
			if (cheque.getBankId() != null) {
				Optional<Bank> bankOptional = bankRepository.findById(cheque.getBankId());
				bankOptional.ifPresent(bank -> cheque.setBankName(bank.getName()));
			}
			if(cheque.getRecivedVia() != null) {
				Optional<String> value = listValueRepository.findByCodee(cheque.getRecivedVia());
				value.ifPresent(cheque::setRecivedVia);
			}	
			return cheque;
		}
		return null;
	}

	private List<ChequeStatusHistory> fetchChequeStatusHistory(Long id, User user) {
		
        List<ChequeStatusHistory> history = chequeStatusHistoryRepository.findchequeStatusHistory(id, user.getTenantId());
        for (ChequeStatusHistory his : history) {
        	if(his.getRecivedVia() != null) {
        		Optional<String> value = listValueRepository.findByCodee(his.getRecivedVia());
        		value.ifPresent(his::setRecivedVia);
        	}	
			if (his.getBankId() != null && his.getBankName() == null) {
				Optional<Bank> bank = bankRepository.findById(his.getBankId());
				if (bank.isPresent()) {
					his.setBankName(bank.get().getName());
					chequeStatusHistoryRepository.save(his);
				}
			}
		}
		return history ;
	}

	private Stockist fetchStockist(Cheque cheque, User user) {
		Optional<Stockist> stockistOptional = stockistRepository.findStockistByTenant(cheque.getStockistId(),
				user.getTenantId());
		return stockistOptional.map(stockist -> {
			if (stockist.getCityId() != null && stockist.getStateId() != null) {
				Optional<City> cityOptional = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
						stockist.getStateId());
				cityOptional.ifPresent(city -> stockist.setCityName(city.getCityName()));
			}
			Optional<StockistManufacture> stockistManufactureOptional = stockistManufactureRepository
					.findBySap(cheque.getSapId(), cheque.getStockistId(), user.getTenantId());
			stockistManufactureOptional.ifPresent(stockistManufacture -> {
				Optional<Manufacturer> manufacturerOptional = manufacturerRepository
						.findById(stockistManufacture.getManufacture());
				manufacturerOptional.ifPresent(
						manufacturer -> stockistManufacture.setManufactureName(manufacturer.getManufacturerName()));
				stockist.setStockistManufacture(stockistManufacture);
			});
			return stockist;
		}).orElse(null);
	}

	public List<ChequeStatusHistory> findByChequeNumber(String id) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				return chequeStatusHistoryRepository.findchequeStatusHistory(id, u.getTenantId());
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	@Override
	public Result<Cheque> create(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Cheque ch = chequeRepository.save(t);
			saveHistory(ch);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
			result.setData(ch);
			return result;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("error While Create Cheque");
		}
		return result;
	}

	public Result<Cheque> createOutward(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				if (isChequeUnique(t, user.getTenantId())) {
					Cheque cheque = saveCheque(t, user);
					updateInvoiceStatus(cheque, user);
					saveHistory(cheque);					
					sendMailStockist(user, cheque, "OUTWARD_CHEQUE_STOCKIST");
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
					result.setData(cheque);
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage(StringIteration.CHEQUENUMBERALREADYEXIST);
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("Error");
		}
		return result;
	}

	private boolean isChequeUnique(Cheque cheque, String tenantId) {
		return !chequeRepository.findChequeByNumberAndBankId(cheque.getChequeNumber(), cheque.getBankId(), tenantId)
				.isPresent();
	}

	private Cheque saveCheque(Cheque cheque, User user) {
		cheque.setIsChequeHold(false);
		cheque.setIsReturned(false);
		cheque.setTenantId(user.getTenantId());
		CommonUtil.setCreatedOn(cheque);
		CommonUtil.setModifiedOn(cheque);
		cheque.setStatus(StringIteration.OUTWARD);
		return chequeRepository.save(cheque);
	}

	private void updateInvoiceStatus(Cheque cheque, User user) {
		String[] invoiceNumbers = cheque.getInvoice().split(",");
		for (String invoiceNumber : invoiceNumbers) {
			Optional<TicketOrderInvoice> invoiceOptional = orderInvoiceRepository
					.findByInvoiceNumberAndTenantId(invoiceNumber.toLowerCase().replace(" ", ""), user.getTenantId());
			invoiceOptional.ifPresent(invoice -> {
				invoice.setChequeStatus(true);
				orderInvoiceRepository.save(invoice);
			});
		}
	}

	/**
	 * Create Security & Inward Cheque
	 * 
	 * @param t
	 * @return
	 */
	public Result<ChequeData> createCheques(ChequeData t) {
		Result<ChequeData> result = new Result<>();
		try {
			Cheque c = new Cheque();
			c.setStockistId(t.getStockistId());
			
			Optional<User> optionUser = userRepository.findById(SecurityUtil.getUserName());
			if (!optionUser.isPresent()) {
				result.setMessage("Invalid User");
				return result;
			}

			User user = optionUser.get();
			if (user.getTenantId() == null) {
				result.setMessage("Invalid User");
				return result;
			}

			int count = 0;
			List<String> invalidCheques = new ArrayList<>();
			List<String> cheques = prepareCheque(t.getChequeNumber());
			List<String> validCheques = new ArrayList<>();
			if(cheques.size() > 100) {
				result.setCode(StringIteration.ERROR_CODE3);
				result.setMessage("Kindly upload 100 cheques in a single row");
				return result;
			}
			
			long statusId = System.currentTimeMillis();
			
			for (String string : cheques) {
				Optional<Cheque> option = findChequeByNumberAndBankId(string, t.getBankId(), user.getTenantId(),t.getSapId(),t.getStockistId());
				if (!option.isPresent()) {
					Cheque cheque = createNewCheque(t, string, user.getTenantId());
					saveHistory(cheque,statusId);
					statusId++;
					count++;
					validCheques.add(string);
				}else {
					invalidCheques.add(string);
					
				}
			}
			if(t.getStatus().equals(StringIteration.SECURITYCHEQUE)) {
				c.setQuantity(count);
				c.setChequeNumber(validCheques.stream().collect(Collectors.joining(",")));
				sendMailManagerAndTenant(user, c, "NEW – SECURITY CHEQUE ADDED");
			}			
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(buildMessage(count));
			setResult(result, invalidCheques);
			
		} catch (Exception e) {
			handleException(e, result);
		}
		return result;
	}
	
	private void setResult(Result<ChequeData> data,List<String> s) {
		if(!s.isEmpty()) {
			data.setMessage(data.getMessage()+"-"+s.stream().collect(Collectors.joining(",")));
		}		
	}

	private Optional<Cheque> findChequeByNumberAndBankId(String chequeNumber, String bankId, String tenantId, String sapId, String stockistId) {
		return chequeRepository.findChequeByNumberAndBankId(chequeNumber, bankId, tenantId,sapId,stockistId);
	}

	private Cheque createNewCheque(ChequeData t, String chequeNumber, String tenantId) {
		Cheque cheque = getCheque(t, chequeNumber);
		cheque.setTenantId(tenantId);
		return chequeRepository.save(cheque);
	}

	private String buildMessage(int count) {
		if (count == 0) {
			return StringIteration.NO_CHEQUE_ADDED_SUCCESSFULLY;
		} else {
			return count + (count > 1 ? StringIteration.CHEQUES_ADDED_SUCCESSFULLY
					: StringIteration.CHEQUE_ADDED_SUCCESSFULLY);
		}
	}

	private void handleException(Exception e, Result<ChequeData> result) {
		logger.error(StringIteration.SPACE, e);
		result.setCode(StringIteration.ERROR_CODE1);
		result.setMessage(e.getMessage());
	}

	public Result<Cheque> chequeCancelAndReturn(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<Cheque> option = chequeRepository.findById(t.getChequeId());
			if (option.isPresent()) {
				Cheque che = option.get();
				String status = che.getStatus();
				Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
				if (user.isPresent()) {
					User u = user.get();
					if (t.getStatus().equals("CANCELLED")) {
						che.setStatus("CANCELLED");
						che.setRemarks(t.getRemarks());
						che.setChequeCancelDate(new Date());						
					} else {
						che.setRemarks(t.getRemarks());
						che.setStatus(t.getStatus());						
					}
					if(status.equals("SECURITY")) {
						sendMailManagerAndTenant(u,che ,"SECURITY CHEQUE RETURNED");
						sendMailStockist(u, che, "SECURITY CHEQUE RETURNED");
					}else if(status.equals("INWARD")) {
						sendMailManagerAndTenant(u,che ,"CANCEL_CHEQUE");
					}
				}

				Cheque chs = chequeRepository.save(che);
				saveHistory(che);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(chs);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("");
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);

		}
		return null;
	}

	public void saveHistory(Cheque cheque,long statusId) {
		Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
		if (us.isPresent()) {
			ChequeStatusHistory cs = new ChequeStatusHistory();
			cs.setTenantId(us.get().getTenantId());
			cs.setId(String.valueOf(statusId));
			cs.setChequeNumber(cheque.getChequeNumber());
			cs.setChequeId(cheque.getChequeId());
			cs.setBankId(cheque.getBankId());
			cs.setHistoryOn(cheque.getReciveDate());
			cs.setRemarks(cheque.getRemarks());
			cs.setStatus(cheque.getStatus());
			cs.setHistoryBy(us.get().getFirstName());
			cs.setRecivedVia(cheque.getRecivedVia());
			chequeStatusHistoryRepository.save(cs);
		}
	}
	
	public void saveHistory(Cheque cheque) {
		Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
		if (us.isPresent()) {
			ChequeStatusHistory cs = new ChequeStatusHistory();
			CommonUtil.setModifiedOn(cs);
			cs.setTenantId(us.get().getTenantId());
			cs.setId(String.valueOf(System.currentTimeMillis()));
			cs.setChequeNumber(cheque.getChequeNumber());
			cs.setChequeId(cheque.getChequeId());
			cs.setBankId(cheque.getBankId());
			cs.setHistoryOn(cheque.getReciveDate());
			cs.setRemarks(cheque.getRemarks());
			cs.setStatus(cheque.getStatus());
			cs.setHistoryBy(us.get().getFirstName());
			cs.setRecivedVia(cheque.getRecivedVia());
			chequeStatusHistoryRepository.save(cs);
		}
	}

	public Result<Cheque> chequeHold(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<Cheque> option = chequeRepository.findById(t.getChequeId());
			if (option.isPresent()) {
				Cheque che = option.get();
				che.setInvoiceHold(t.getInvoiceHold());
				che.setIsChequeHold(true);
				Cheque chs = chequeRepository.save(che);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(chs);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("");
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);

		}
		return null;
	}

	public Result<Cheque> unHold(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<Cheque> option = chequeRepository.findById(t.getChequeId());
			if (option.isPresent()) {
				Cheque che = option.get();
				che.setIsChequeHold(false);
				che.setInvoiceHold(null);
				Cheque chs = chequeRepository.save(che);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(chs);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				return result;
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("");
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);

		}
		return null;
	}

	@Override
	public Result<Cheque> update(Cheque t) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<Cheque> option = chequeRepository.findById(t.getChequeId());
				if (option.isPresent()) {
					Cheque che = option.get();
					if (che.getChequeId().equals(t.getChequeId())) {

						che.setStatus(t.getStatus());
						che.setChequeNumber(t.getChequeNumber());
						che.setStockistId(t.getStockistId());
						che.setInvoice(t.getInvoice());
						che.setSapId(t.getSapId());
						che.setReciveDate(t.getReciveDate());
						che.setQuantity(t.getQuantity());
						che.setBankId(t.getBankId());
						che.setAmount(t.getAmount());
						che.setDepositDate(t.getDepositDate());
						che.setRecivedVia(t.getRecivedVia());
						che.setChequeCancelDate(t.getChequeCancelDate());
						che.setDtsCnDn(t.getDtsCnDn());
						che.setDtsCnDnAmount(t.getDtsCnDnAmount());
						che.setAdjustedInvoices(t.getAdjustedInvoices());
						che.setIsChequeHold(t.getIsChequeHold());
						che.setInvoiceHold(t.getInvoiceHold());
						che.setRemarks(t.getRemarks());
						che.setInvoiceAmount(t.getInvoiceAmount());
						CommonUtil.setModifiedOn(che);
						Cheque chs = chequeRepository.save(che);
						updateInvoiceStatus(che,u);
						
//						Optional<TicketOrderInvoice> in = orderInvoiceRepository.findByInvoiceNumberAndTenantId(
//								che.getInvoice().toLowerCase().replace(" ", ""), us.get().getTenantId());
//						if (in.isPresent()) {
//							in.get().setChequeStatus(true);
//							orderInvoiceRepository.save(in.get());
//						}
						saveHistory(chs);
//						returMail(u, che, "OUTWARD_CHEQUE");
						sendMailStockist(u, che, "OUTWARD_CHEQUE_STOCKIST");
						
						Optional<StockistManufacture> st = stockistManufactureRepository.findStockistBySapAndTenant(che.getSapId(), u.getTenantId());
		                if (st.isPresent()) {
		                        String value = listValueRepository.findByNameAndCodeValue("200", st.get().getChequeCategory());
		                        BigInteger count = chequeRepository.getInHandChequeCount(che.getStockistId(),che.getSapId());
		                        if(count.intValue() <= Integer.parseInt(value)) {
		                        	sendMailStockist(u, che, "REPLENISH_CHEQUE");
		                        }                   
		                }                   
		                
						
						result.setCode(StringIteration.SUCCESS_CODE);
						result.setData(chs);
						result.setMessage(StringIteration.CHEQUE_ISSUED_SUCCESSFULLY);
						return result;
					}
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("");
					return result;
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	public Result<Cheque> editCheque(Cheque t) {

		Result<Cheque> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				Optional<Cheque> che = chequeRepository.findById(t.getChequeId());
				if (che.isPresent()) {
					Cheque c = che.get();
					Optional<Cheque> isExiset = chequeRepository.findChequeByNumberAndBankId(t.getChequeNumber(),
							t.getBankId(), u.getTenantId());
					if (isExiset.isPresent()) {
						result.setCode(StringIteration.ERROR_CODE1);
						result.setMessage("Cheque Number Already Exist");
						return result;
					} else {

						c.setBankId(t.getBankId());
						c.setChequeNumber(t.getChequeNumber());
						c.setModifiedOn(new Date());
						Cheque ch = chequeRepository.save(c);
						saveHistory(ch);
						result.setCode(StringIteration.SUCCESS_CODE);
						result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
						return result;
					}
				}
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	@Override
	public Result<Cheque> delete(Long id) {
		Result<Cheque> result = new Result<>();
		try {
			Optional<Cheque> option = chequeRepository.findById(id);
			if (option.isPresent()) {
				chequeRepository.delete(option.get());
				saveHistory(option.get());
				result.setMessage(StringIteration.DELETEDSUCCESSFULLY);
				result.setCode(StringIteration.SUCCESS_CODE);
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	public List<Cheque> getChequeByStatus(String stockistId, String sapId, String status) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User uu = user.get();
				List<Cheque> ch = chequeRepository.getChequeByStatus(stockistId, sapId, status, uu.getTenantId());

				if (!ch.isEmpty()) {
					setBankNames(ch);
				}
				return ch;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	private void setBankNames(List<Cheque> ch) {
		for (Cheque cheque : ch) {
			if (cheque.getBankId() != null) {
				Optional<Bank> bank = bankRepository.findById(cheque.getBankId());
				if (bank.isPresent()) {
					cheque.setBankName(bank.get().getName());
				}
			}
		}
	}

	public Bank getSecurituChequeBank(String stockistId, String sapId) {
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User uu = user.get();
				List<Cheque> checkList = chequeRepository.getChequeByStatus(stockistId, sapId, "SECURITY",
						uu.getTenantId());
				Map<String, Integer> secCountMap = createSecCountMap(checkList);

				String bankId = getMaxCountBankId(secCountMap, checkList);
				if (bankId != null) {
					return bankRepository.findById(bankId).orElse(null);
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	private Map<String, Integer> createSecCountMap(List<Cheque> checkList) {
		Map<String, Integer> secCountMap = new HashMap<>();
		checkList.forEach(ch -> secCountMap.merge(ch.getBankId(), 1, Integer::sum));
		return secCountMap;
	}

	private String getMaxCountBankId(Map<String, Integer> secCountMap, List<Cheque> checkList) {
		Integer maxCount = secCountMap.values().stream().max(Integer::compareTo).orElse(0);
		List<String> maxEntriesSecList = secCountMap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(maxCount)).map(Map.Entry::getKey).collect(Collectors.toList());

		if (maxEntriesSecList.size() > 1) {
			for (Cheque chk : checkList) {
				if (maxEntriesSecList.contains(chk.getBankId())) {
					return chk.getBankId();
				}
			}
		} else if (!maxEntriesSecList.isEmpty()) {
			return maxEntriesSecList.get(0);
		}
		return null;
	}

	void setSortByAndDir(PaginationDTO page) {
		if (StringUtils.isNullOrEmpty(page.getSortBy())) {
			page.setSortBy(StringIteration.STOCKISTID);
		}
		if (StringUtils.isNullOrEmpty(page.getSortDir())) {
			page.setSortDir(StringIteration.ASC);
		}
		if (StringUtils.isNullOrEmpty(page.getSearch())) {
			page.setSearch(StringIteration.EMPTY_STRING);
		}
	}

	public Result<HashMap<String, Object>> getCheque(PaginationDTO paging, String stockist, String location,
			String sapId) {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User user = u.get();
				if (user.getRoleName().equals("MANAGER")) {
					return getChequeByManager(paging, stockist, location, sapId, user);
				} else if (user.getRoleName().equals("USER")) {
					return getChequeByUser(paging, stockist, location, sapId, user);
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public Result<HashMap<String, Object>> getChequeByManager(PaginationDTO page, String stockist, String location,
			String sapId, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			setSortByAndDir(page);
			if (stockist == null) {
				stockist = "";
			}
			if (location == null) {
				location = "";
			}
			if (sapId == null) {
				sapId = "";
			}
			BigInteger totalItems;
			Pageable paging = PageRequest.of(page.getPage(), page.getSize(),
					Sort.by(Sort.Direction.fromString(page.getSortDir()), page.getSortBy()));
//			totalItems = chequeRepository.findChequeCountByManager(us.getTenantId(), us.getUserId(), stockist, location,
//					page.getSearch(), sapId, page.getStatus());
			Page<ChequeListDTO> objects = chequeRepository.findChequeByManager(us.getTenantId(), us.getUserId(), stockist,
					location, page.getSearch(), sapId, page.getStatus(), paging);

//			List<ChequeDTO> chequeDTOs = objects.stream().map(objects2 -> {
//				ChequeDTO chequeDTO = new ChequeDTO();
//				chequeDTO.setStockistId(String.valueOf(objects2[0]));
//				chequeDTO.setStockistName(String.valueOf(objects2[1]));
//				chequeDTO.setSapId(String.valueOf(objects2[2]));
//				chequeDTO.setManufacture(String.valueOf(objects2[3]));
//				chequeDTO.setCategory(String.valueOf(objects2[4]));
//				chequeDTO.setThreshold(String.valueOf(objects2[5]));
//				chequeDTO.setTotal((BigInteger) objects2[6]);
//				chequeDTO.setHold((BigInteger) objects2[7]);
//				chequeDTO.setSecurity((BigInteger) objects2[8]);
//				chequeDTO.setBalance((BigInteger) objects2[9]);
//				chequeDTO.setLocation(String.valueOf(objects2[10]));
//				chequeDTO.setStatus(String.valueOf(objects2[11]));
//				return chequeDTO;
//			}).collect(Collectors.toList());

			map.put(StringIteration.CHEQUES, objects);
//			map.put(StringIteration.TOTALCOUNT, totalItems);
			result.setData(map);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCESS);

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);

		}
		return result;

	}

	public ChequeCardView getChequeCardView(String status) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				if (u.getRoleName().equals("MANAGER")) {
					return getChequeCardView(u.getTenantId(),u.getUserId());
				}
				else if (u.getRoleName().equals("USER")) {
					return getChequeCardView(u.getTenantId(),u.getHierarachyId());
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ChequeCardView();
	}
	
	private ChequeCardView getChequeCardView(String tenantId,String userId) {
		try {
			
			Optional<ChequeCardView> view = cardViewRepository.getByUser(tenantId, userId);
				if(view.isPresent()) {
					return view.get();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new ChequeCardView();
	}
	
	

	public Result<HashMap<String, Object>> getChequeByUser(PaginationDTO page, String stockist, String location,
			String sapId, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			setSortByAndDir(page);
			if (StringUtils.isNullOrEmpty(stockist)) {
				stockist = "";
			}
			if (StringUtils.isNullOrEmpty(location)) {
				location = "";
			}
			if (StringUtils.isNullOrEmpty(sapId)) {
				sapId = "";
			}
			Pageable paging = PageRequest.of(page.getPage(), page.getSize(),
					Sort.by(Sort.Direction.fromString(page.getSortDir()), page.getSortBy()));

			Page<ChequeListDTO> objects = chequeRepository.findChequeByManager(us.getTenantId(), us.getHierarachyId(),
					stockist, location, page.getSearch(), sapId, page.getStatus(), paging);

			map.put(StringIteration.CHEQUES, objects);
			result.setData(map);
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCESS);

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);

		}
		return result;

	}

	public Result<String> checkChequeNumberAlerdyExist(String chequeNumber) {

		Result<String> result = new Result<>();

		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				Optional<Cheque> optional = chequeRepository.findByChequeNumber(chequeNumber, u.getTenantId());
				if (optional.isPresent()) {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Cheque Number Already Exist");
				} else {
					result.setCode(StringIteration.SUCCESS_CODE);
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

		return result;
	}

	 public void updateKey(Map<String, BiConsumer<Cheque, Cell>> columnPropertyMapping){

	        columnPropertyMapping.put("SAPID", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                double numericValue = cell.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setSapId(String.valueOf(longValue));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setSapId(cell.getStringCellValue());
	            }
	        });

	        columnPropertyMapping.put("StockistId", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                cheque.setStockistId(String.valueOf(cell.getNumericCellValue()));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setStockistId(cell.getStringCellValue());
	            }
	        });
	        
	        columnPropertyMapping.put("ReceivedDate",
	                 
	                (cheque, cell) -> {
	 
	                    if (cell.getCellType() == CellType.NUMERIC) {
	                        cheque.setReciveDate(cell.getDateCellValue());
	 
	                    } else if (cell.getCellType() == CellType.STRING) {
	 
	                        try {
	                            Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(cell.getStringCellValue().trim());
	                            cheque.setReciveDate(date);
	                        } catch (ParseException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });
	        
	        columnPropertyMapping.put("ReceivedVia", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                double numericValue = cell.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setRecivedVia(String.valueOf(longValue));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setRecivedVia(cell.getStringCellValue());
	            }
	        });

	        columnPropertyMapping.put("Bank", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                cheque.setBankId(String.valueOf(cell.getNumericCellValue()));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setBankId(cell.getStringCellValue());
	            }
	        });
	        columnPropertyMapping.put("ChequeNumber", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                double numericValue = cell.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setChequeNumber(String.valueOf(longValue));
	            } else if (cell.getCellType() == CellType.STRING) {
	                String stringValue = cell.getStringCellValue();
	                cheque.setChequeNumber(stringValue);
	            }
	        });
	        columnPropertyMapping.put("ChequeAmount", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                cheque.setAmount(BigDecimal.valueOf(cell.getNumericCellValue()));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setAmount(new BigDecimal(cell.getStringCellValue()));
	            }

	        });

	        columnPropertyMapping.put("ChequeDepositDate",
	                (cheque, cell) -> {
	                    if (cell.getCellType() == CellType.NUMERIC) {
	                        cheque.setDepositDate(cell.getDateCellValue());
	                    } else if (cell.getCellType() == CellType.STRING) {
	                        try {
	                            Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(cell.getStringCellValue().trim());
	                            cheque.setDepositDate(date);
	                        } catch (ParseException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });

	        columnPropertyMapping.put("Invoices", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                double numericValue = cell.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setInvoice(String.valueOf(longValue));
	            } else if (cell.getCellType() == CellType.STRING) {
	                cheque.setInvoice(cell.getStringCellValue());
	            }
	        });
	        columnPropertyMapping.put("InvoicesAmount",
	                (cheque, cell) -> cheque.setInvoiceAmount(BigDecimal.valueOf(cell.getNumericCellValue())));
	        columnPropertyMapping.put("AdjustedInvoice", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                 long d= (long)cell.getNumericCellValue();
	                cheque.setAdjustedInvoices(String.valueOf(d));
	            } else if (cell.getCellType() == CellType.STRING) {
	                String stringValue = cell.getStringCellValue();
	                cheque.setAdjustedInvoices(stringValue);
	            }
	        });
	        columnPropertyMapping.put("TDS/CN/DN", (cheque, cell) -> {
	            if (cell.getCellType() == CellType.NUMERIC) {
	                cheque.setDtsCnDn(String.valueOf(cell.getNumericCellValue()));
	            } else if (cell.getCellType() == CellType.STRING) {
	                String stringValue = cell.getStringCellValue();
	                cheque.setDtsCnDn(stringValue);
	            }
	        });
	        columnPropertyMapping.put("TDS/CN/DNAmount",
	                (cheque, cell) -> cheque.setDtsCnDnAmount(BigDecimal.valueOf(cell.getNumericCellValue())));
	        columnPropertyMapping.put("Type", (cheque, cell) -> cheque.setStatus(cell.getStringCellValue()));

	    }

	    
	    
	    public Result<Object> fileReader(MultipartFile file, String sheetIndex) throws IOException {

	        Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
	        Result<Object> result = new Result<>();

	        if (us.isPresent()) {
	            User u = us.get();
	            try(FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
	    	            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);){
	            	List<Cheque> cheques = new ArrayList<>();
	 	            List<Cheque> invalidData = new ArrayList<>();
	 	            
	 	           XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));
	 	           
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
		                
		            	
	            	Cheque chequeExcel = new Cheque();
	            	ChequeImportDto cheque = new ChequeImportDto(true, row, chequeExcel, false);
		            
		            prepareRows(row , cheque,chequeExcel,u);
		            
	                if (cheque.isFlag()) {
	                    cheques.add(chequeExcel);                    
	                } else {
	                	chequeExcel.setInvalid(cheque.getMessage());
	                	chequeExcel.setInvalidchequeNumber(cheque.getInvalidChequeNo());
	                    invalidData.add(chequeExcel);   
	                }
	                rowIndex++;
	            }
		            
		        saveExcel(cheques, result);  
		        writeInvalidExcel(invalidData, result);    
		       
		        
		        return result;
	 	            
	            }catch(Exception e) {
	            	logger.error(e.getMessage());
	            	e.printStackTrace();
	            }  
	        }
	       return result;
	    }
	    
	private void prepareRows(Row row , ChequeImportDto dto , Cheque cheque, User u) {
		prepareType(row,dto,cheque);
		prepareSapId(row,dto,cheque,u);
		prepareBankId(row,dto,cheque);
		if(cheque.getStatus().equals("INWARD") || cheque.getStatus().equals("SECURITY")) {		
			prepareInwardChequeNo(row,dto,cheque,u);
			prepareInwardReceivedDate(row,dto,cheque);
			prepareInwardReceivedVia(row,dto,cheque);
		} else {			
			prepareOutwardChequeNo(row,dto,cheque ,u );
			prepareOutwardChequeAmount(row,dto,cheque);
			prepareOutwardDepositDate(row, dto, cheque);
			prepareOutwardInvoice(row, dto,cheque);
			prepareOutwardInvoiceAmount(row,dto,cheque);
			prepareOutwardAdjInvoices(row,dto,cheque);
			prepareOutwardTds(row,dto,cheque);
			prepareOutwardTdsAmount(row,dto,cheque);
		}	
	}
	
	private void prepareType(Row row,ChequeImportDto dto,Cheque cheque) {
		try {
			Cell type = row.getCell(0);
	        if (type == null || type.getCellType() == CellType.BLANK) {                                 
	            dto.setFlag(false); 
	            dto.setMessage("Type is empty , ");
	        } else {
	        	cheque.setStatus(type.getStringCellValue().toUpperCase().trim());                  
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid type , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareSapId(Row row , ChequeImportDto dto, Cheque cheque, User u) {
		try {
			Cell sapId = row.getCell(1);
	        if (sapId == null || sapId.getCellType() == CellType.BLANK) {                                   
	            dto.setFlag(false);
	            dto.setMessage(" Sap Id is Empty , "); 
	        } else{
	            if (sapId.getCellType() == CellType.NUMERIC) {
	                double numericValue = sapId.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setSapId(String.valueOf(longValue));
	            } else if (sapId.getCellType() == CellType.STRING) {
	            	cheque.setSapId(sapId.getStringCellValue());
	            }
	            if(cheque.getSapId() != null) {
	    			Optional<StockistManufacture> st = stockistManufactureRepository.findStockistBySapAndTenant(cheque.getSapId(), u.getTenantId());
	                if (st.isPresent()) {
	                	cheque.setStockistId(st.get().getStockistId());
	                    if(cheque.getStatus().equals(StringIteration.INWARDCHEQUE)) {
	                        String value = listValueRepository.findByNameAndCodeValue("200", st.get().getChequeCategory());
	                        if("0".equals(value)) {
	                        	dto.setFlag(false);
	                        	dto.setMessage("This sap id has threshold limit 0 , ");
	                        } else {
	                        	dto.setThreshold(true);
	                        }                   
	                    }                   
	                }else {
	                	dto.setFlag(false);
	                	dto.setMessage(" Sap Id is Invalid , ");
	                } 
	    		}
	                              
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid sap id , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareBankId(Row row,ChequeImportDto dto,Cheque cheque) {
		try {
			Cell bankId = row.getCell(2);
	        if (bankId == null || bankId.getCellType() == CellType.BLANK) {                                 
	            dto.setFlag(false);
	            dto.setMessage(" bank is Empty , ");
	        } else {
	            if (bankId.getCellType() == CellType.NUMERIC) {
	            	cheque.setBankId(String.valueOf(bankId.getNumericCellValue()));
	            } else if (bankId.getCellType() == CellType.STRING) {
	            	cheque.setBankId(bankId.getStringCellValue());
	            	cheque.setBankName(bankId.getStringCellValue());
	            }  
	            
	            Optional<Bank> bank = bankRepository.findByBankId(cheque.getBankId().replace(" ", "").toLowerCase());
	            if (bank.isPresent()) {
	            	cheque.setBankName(cheque.getBankId());
	            	cheque.setBankId(bank.get().getCode());                     
	            }else {
	                dto.setFlag(false);
	                dto.setMessage(" Invalid bank name , ");
	            } 
	                              
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid bank name , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareInwardChequeNo(Row row, ChequeImportDto dto , Cheque cheque, User u) {
		try {
			Cell chequeNo = row.getCell(3);
	        if (chequeNo == null || chequeNo.getCellType() == CellType.BLANK) {                                  
	            dto.setFlag(false);
	            dto.setMessage(" Cheque Number is Empty ,");
	        } else {
	            if (chequeNo.getCellType() == CellType.NUMERIC) {
	                double numericValue = chequeNo.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setChequeNumber(String.valueOf(longValue));
	            } else if (chequeNo.getCellType() == CellType.STRING) {
	                String stringValue = chequeNo.getStringCellValue();
	                cheque.setChequeNumber(stringValue);
	            } 
	            
	            if( dto.isFlag() &&  cheque.getBankId() != null && cheque.getSapId()!= null) {                        
	                List<String> che = prepareCheque(cheque.getChequeNumber());
	                if(che.size() > 100) {
	                	dto.setFlag(false);
	                	dto.setMessage("Kindly upload 100 cheques in a single row ,");
	                }
	                for(String chequeNumber: che) {
	                    Optional<Cheque> option = chequeRepository.findChequeByNumberAndBankId(chequeNumber, cheque.getBankId(),u.getTenantId(),cheque.getSapId(),cheque.getStockistId());   
	                    if (option.isPresent()) {
	                    	dto.setFlag(false);
	                        dto.setInvalidChequeNo(chequeNumber + ",");
	                    } else {
	                        if (chequeNo.getCellType() == CellType.NUMERIC) {
	                            double numericValue = chequeNo.getNumericCellValue();
	                            long longValue = (long) numericValue;   
	                            cheque.setChequeNumber(String.valueOf(longValue));
	                        } else if (chequeNo.getCellType() == CellType.STRING) {
	                            String stringValue = chequeNo.getStringCellValue();
	                            cheque.setChequeNumber(stringValue);
	                        }
	                    }
	                }                       
	            }
	            
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid cheque number , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareInwardReceivedDate(Row row, ChequeImportDto dto , Cheque cheque) {
		try {
			Cell receivedDate = row.getCell(4);
	        if (receivedDate == null || receivedDate.getCellType() == CellType.BLANK) {                                 
	        	dto.setFlag(false);
	        	dto.setMessage(" Received Date is Empty ,");
	        } else {
	            if (receivedDate.getCellType() == CellType.NUMERIC) {
	            	cheque.setReciveDate(receivedDate.getDateCellValue()); 
	            } else if (receivedDate.getCellType() == CellType.STRING) { 
	                try {
	                    Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(receivedDate.getStringCellValue().trim());
	                    cheque.setReciveDate(date);
	                    cheque.setInvalidReceivedDate(receivedDate.getStringCellValue());
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                    dto.setFlag(false);                    
	                    dto.setMessage("received Date should be in dd-mm-yyyy, ");
	                    cheque.setInvalidReceivedDate(receivedDate.getStringCellValue());
	                }
	            }                   
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid received date , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareInwardReceivedVia(Row row, ChequeImportDto dto, Cheque cheque) {
		try {
			Cell receivedVia = row.getCell(5);
	        if (receivedVia == null || receivedVia.getCellType() == CellType.BLANK) {                                   
	        	dto.setFlag(false);
	        	dto.setMessage(" Received Via is Empty ,");
	        } else {
	            if (receivedVia.getCellType() == CellType.NUMERIC) {
	                double numericValue = receivedVia.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setRecivedVia(String.valueOf(longValue));
	                cheque.setInvalidReceivedVia(cheque.getRecivedVia());
	            } else if (receivedVia.getCellType() == CellType.STRING) {
	            	cheque.setRecivedVia(receivedVia.getStringCellValue());
	            	cheque.setInvalidReceivedVia(cheque.getRecivedVia());
	            }
	            Optional<String> category = listValueRepository.findByNameAndCode("800",cheque.getRecivedVia().replace(" ","").toLowerCase());  
	            if(category.isPresent()) {
	            	cheque.setRecivedVia(category.get());
	            }else {
	                dto.setFlag(false);
	                dto.setMessage(" Received via is invalid , ");
	            }
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid received via , ");
			logger.error(e.getMessage());
		}
		  
	}
	
	private void prepareOutwardChequeNo(Row row , ChequeImportDto dto , Cheque cheque, User u) {
		try {
			Cell chequeNo = row.getCell(3);
	        if (chequeNo == null || chequeNo.getCellType() == CellType.BLANK) {  
	        	dto.setFlag(false);
	            dto.setMessage(" Cheque Number is Empty ," );
	        } else {
	            if (chequeNo.getCellType() == CellType.NUMERIC) {
	                double numericValue = chequeNo.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setChequeNumber(String.valueOf(longValue));
	            } else if (chequeNo.getCellType() == CellType.STRING) {
	                String stringValue = chequeNo.getStringCellValue();
	                cheque.setChequeNumber(stringValue);
	            }  
	            
	            if (Boolean.TRUE.equals(dto.isThreshold()) && cheque.getBankId() != null) {
	                Optional<Cheque> inward = chequeRepository.findByChequeNumberAndBank(cheque.getChequeNumber(), cheque.getBankId(), u.getTenantId());
	                if (!inward.isPresent()) {
	                	dto.setFlag(false);
	                    dto.setMessage(cheque.getChequeNumber()+ " is not belongs to this bank");                                    
	                }
	            } else if(Boolean.FALSE.equals(dto.isThreshold())&&cheque.getBankId() != null) {
	            	Optional<Cheque> optional = chequeRepository.findChequeByNumberAndBankId(cheque.getChequeNumber(), cheque.getBankId(), u.getTenantId(),cheque.getSapId(),cheque.getStockistId());
	            	if(optional.isPresent()) {
	            		Cheque c = optional.get();
	            		if(c.getStatus().equals(StringIteration.OUTWARD)) {
	            			dto.setFlag(false);
	            			dto.setMessage(cheque.getChequeNumber()+ " is already available , ");
	            		}	
	            	}
	            }
	        }
		} catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid cheque number , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareOutwardChequeAmount(Row row,ChequeImportDto dto,Cheque cheque ) {
		try {
			Cell chequeAmount = row.getCell(4);
	        if (chequeAmount == null || chequeAmount.getCellType() == CellType.BLANK) {                                 
	        	dto.setFlag(false);
	            dto.setMessage(" Cheque Amount is Empty ,");
	        } else {
	            if (chequeAmount.getCellType() == CellType.NUMERIC) {
	            	cheque.setAmount(BigDecimal.valueOf(chequeAmount.getNumericCellValue()));
	            } else if (chequeAmount.getCellType() == CellType.STRING) {
	            	cheque.setAmount(new BigDecimal(chequeAmount.getStringCellValue()));
	            }                   
	        }
		}catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid cheque amount ,");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareOutwardDepositDate(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell depositDate = row.getCell(5);
	        if (depositDate == null || depositDate.getCellType() == CellType.BLANK) {                                   
	        	dto.setFlag(false);
	            dto.setMessage(" depositDate is Empty, ");
	        } else {
	            if (depositDate.getCellType() == CellType.NUMERIC) {
	            	cheque.setDepositDate(depositDate.getDateCellValue());
	            	cheque.setInvaliddepositDate(String.valueOf(depositDate.getNumericCellValue()));
	            } else if (depositDate.getCellType() == CellType.STRING) {
	            	try {
	                    Date date = new SimpleDateFormat(StringIteration.DDMMYY).parse(depositDate.getStringCellValue().trim());
	                    cheque.setDepositDate(date);
	                    cheque.setInvaliddepositDate(depositDate.getStringCellValue());
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                    dto.setFlag(false);                    
	                    dto.setMessage("Deposit Date should be in dd-mm-yyyy, ");
	                    cheque.setInvaliddepositDate(depositDate.getStringCellValue());
	                }
	            }                   
	        }
		}catch (Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid deposit date , ");
			logger.error(e.getMessage());
		}		
	}
	
	
	private void prepareOutwardInvoice(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell invoices = row.getCell(6);
	        if (invoices == null || invoices.getCellType() == CellType.BLANK) {                                 
	        	dto.setFlag(false);
	            dto.setMessage(" Invoices is Empty, ");
	        } else {
	            if (invoices.getCellType() == CellType.NUMERIC) {
	                double numericValue = invoices.getNumericCellValue();
	                long longValue = (long) numericValue;
	                cheque.setInvoice(String.valueOf(longValue));
	            } else if (invoices.getCellType() == CellType.STRING) {
	            	cheque.setInvoice(invoices.getStringCellValue());
	            }                   
	        }
		}catch(Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid invoices , ");
			logger.error(e.getMessage());
		}		
	}
	
	private void prepareOutwardInvoiceAmount(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell invAmount = row.getCell(7);
	        if (invAmount == null || invAmount.getCellType() == CellType.BLANK) {                                   
	        	dto.setFlag(false);
	            dto.setMessage(" Invoice Amount is Empty, ");
	        } else {
	        	cheque.setInvoiceAmount(BigDecimal.valueOf(invAmount.getNumericCellValue()));                
	        }
		}catch(Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid invoices amount , ");
			logger.error(e.getMessage());
		}		
	}
	
	private void prepareOutwardAdjInvoices(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell adjInv = row.getCell(8);
	        if (adjInv == null || adjInv.getCellType() == CellType.BLANK) {                                 
	        	cheque.setAdjustedInvoices("");
	        } else {
	            if (adjInv.getCellType() == CellType.NUMERIC) {
	             long d= (long)adjInv.getNumericCellValue();
	             cheque.setAdjustedInvoices(String.valueOf(d));
	            } else if (adjInv.getCellType() == CellType.STRING) {
	             String stringValue = adjInv.getStringCellValue();
	             cheque.setAdjustedInvoices(stringValue);
	            }               
	        }			
		}catch(Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid adjacent invoices , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareOutwardTds(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell tds = row.getCell(9);
	        if (tds == null || tds.getCellType() == CellType.BLANK) {                                   
	        	cheque.setDtsCnDn("");
	        } else {
	            if (tds.getCellType() == CellType.NUMERIC) {
	            	cheque.setDtsCnDn(String.valueOf(tds.getNumericCellValue()));
	            } else if (tds.getCellType() == CellType.STRING) {
	                String stringValue = tds.getStringCellValue();
	                cheque.setDtsCnDn(stringValue);
	            }
	        }
		}catch(Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid tds/cn/dn , ");
			logger.error(e.getMessage());
		}
		
	}
	
	private void prepareOutwardTdsAmount(Row row , ChequeImportDto dto , Cheque cheque) {
		try {
			Cell tdsAmount = row.getCell(10);
	        if (tdsAmount == null || tdsAmount.getCellType() == CellType.BLANK) {                                   
	        	cheque.setDtsCnDnAmount(BigDecimal.ZERO);
	        } else {
	        	cheque.setDtsCnDnAmount(BigDecimal.valueOf(tdsAmount.getNumericCellValue()));
	        }
		}catch(Exception e) {
			dto.setFlag(false);
			dto.setMessage("invalid tds amount , ");
			logger.error(e.getMessage());
		}		
	}
	 
	private void saveExcel(List<Cheque> cheques, Result<Object> result) {
		if (!cheques.isEmpty()) {
            cheques.forEach(cheque -> {
                if (cheque.getStatus().equals(StringIteration.OUTWARD)) {
                    Cheque outwardCheque = new Cheque();
                    outwardCheque.setBankId(cheque.getBankId());
                    outwardCheque.setQuantity(1);
                    outwardCheque.setChequeNumber(cheque.getChequeNumber());
                    outwardCheque.setStatus(StringIteration.OUTWARD);
                    outwardCheque.setStockistId(cheque.getStockistId());
                    outwardCheque.setSapId(cheque.getSapId());
                    outwardCheque.setInvoice(cheque.getInvoice());
                    outwardCheque.setInvoiceAmount(cheque.getInvoiceAmount());
                    outwardCheque.setAdjustedInvoices(cheque.getAdjustedInvoices());
                    outwardCheque.setAmount(cheque.getAmount());
                    outwardCheque.setDtsCnDn(cheque.getDtsCnDn());
                    outwardCheque.setDtsCnDnAmount(cheque.getDtsCnDnAmount());
                    outwardCheque.setDepositDate(cheque.getDepositDate());
                    List<Cheque> inwardCheque = getChequeByStatus(cheque.getStockistId(), cheque.getSapId(),
                            "INWARD");
                    if (inwardCheque != null && !inwardCheque.isEmpty()) {
                        for (Cheque inwardCheques : inwardCheque) {
                            if (inwardCheques.getChequeNumber().equals(cheque.getChequeNumber())) {
                            	outwardCheque.setChequeId(inwardCheques.getChequeId());
                                break;
                            }
                        }
                    }
                    if (inwardCheque.isEmpty()) {
                        createOutward(outwardCheque);                         
                    } else {
                        update(outwardCheque);
                    }

                } else {

                    ChequeData chequeData = new ChequeData();
                    chequeData.setBankId(cheque.getBankId());
                    chequeData.setRecivedVia(cheque.getRecivedVia());
                    chequeData.setReciveDate(cheque.getReciveDate());
                    chequeData.setChequeNumber(cheque.getChequeNumber());
                    chequeData.setStatus(cheque.getStatus());
                    chequeData.setStockistId(cheque.getStockistId());
                    chequeData.setSapId(cheque.getSapId());
                    createCheques(chequeData);

                }
            });
        }
	}
	    
    private void writeInvalidExcel(List<Cheque> invalidData , Result<Object> result) {
    	if(!invalidData.isEmpty()) {
            try(XSSFWorkbook workbook1 = new XSSFWorkbook();){
            

	            List<String>  headerList = new ArrayList<>();
	            if(!invalidData.get(0).getStatus().isEmpty()) {
	                if(invalidData.get(0).getStatus().equals(StringIteration.OUTWARD)) {
	                    String[] headerList2 = {"Sap Id","Bank", "Cheque Number","Cheque Amount" , "Cheque Deposit Date", "Invoices", "Invoice Amount", "AdjustedInvoices","TDS/CN/DN","TDS/CN/DNAmount","Type", "Remarks"};
	                    headerList.addAll(Arrays.asList(headerList2));
	                }else {
	                    String[]headerList1 = {"Sap Id","Bank", "Cheque Number", "Received Date", "Received Via", "Type" , "Remarks"};
	                    headerList.addAll(Arrays.asList(headerList1));
	                }
	            }   
	
	         
	            XSSFSheet spreadsheet = workbook1.createSheet(" invalid Cheque Excel ");
	            int rowId = 0;
	           
	            XSSFRow row1;
	            row1 = spreadsheet.createRow(rowId++);
	            int r=0;
	            for (String s : headerList) {
	                row1.createCell(r).setCellValue(s);
	                r++;
	            }
	            if(invalidData.get(0).getStatus().equals("OUTWARD")) {
	                for(Cheque i : invalidData) {
	                     row1 = spreadsheet.createRow(rowId++);
	                     row1.createCell(0).setCellValue(i.getSapId());
	                     row1.createCell(1).setCellValue(i.getBankName());
	                     row1.createCell(2).setCellValue(i.getChequeNumber());
	                     row1.createCell(3).setCellValue((String.valueOf(i.getAmount())));
	                     row1.createCell(4).setCellValue(i.getInvaliddepositDate());
	                     row1.createCell(5).setCellValue(i.getInvoice());
	                     row1.createCell(6).setCellValue((String.valueOf(i.getInvoiceAmount())));  
	                     row1.createCell(7).setCellValue(i.getAdjustedInvoices());
	                     row1.createCell(8).setCellValue(i.getDtsCnDn());
	                     row1.createCell(9).setCellValue(String.valueOf(i.getDtsCnDnAmount()));
	                     row1.createCell(10).setCellValue(i.getStatus());
	                     row1.createCell(11).setCellValue(i.getInvalid());
	                }
	            }else {
	                for(Cheque i : invalidData) {
	                     row1 = spreadsheet.createRow(rowId++);
	                     row1.createCell(0).setCellValue(i.getSapId());
	                     row1.createCell(1).setCellValue(i.getBankName());
	                     row1.createCell(2).setCellValue(i.getChequeNumber());
	                     row1.createCell(3).setCellValue(i.getInvalidReceivedDate());
	                     row1.createCell(4).setCellValue(i.getInvalidReceivedVia());
	                     row1.createCell(5).setCellValue(i.getStatus());
	                     
	                     if (i.getInvalidchequeNumber() != null && i.getInvalidchequeNumber().length() > 0) {
	                       row1.createCell(6).setCellValue(i.getInvalid()+i.getInvalidchequeNumber() +" cheque(s) already available");
	                     } else {
	                       row1.createCell(6).setCellValue(i.getInvalid());
	                     }
	
	               }
	            }
	            
	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            workbook1.write(byteArrayOutputStream);
	            InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	
	            String fileId = UUID.randomUUID().toString();
	
	            com.healthtraze.etraze.api.file.model.File invalidCheques = fileStorageService.uploadFileToAWS(fileId,
	                    fileId, byteArrayInputStream, "InvalidCheques.xlsx", "document", 0);
	
	            result.setCode(StringIteration.ERROR_CODE1); 
	            result.setData(invalidCheques);
	            result.setMessage("invalid data found");
	
	            byteArrayOutputStream.flush();
	            byteArrayOutputStream.close();
            }catch (Exception e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
			}    
            
        } else {
            result.setCode(StringIteration.SUCCESS_CODE);
        }
	}    


	public Result<Object> differentInward() {
		Result<Object> result = new Result<>();
		try {

			
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				securityChequeMail(us.get(), "CHEQUE");
				result.setCode("0000");
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}
	
	

	public void securityChequeMail(User user, String template, Cheque ch) {
		EmailTemplate emailTemplate = emailTemplateService.findById(template);
		if (emailTemplate != null) {
			List<String> name=new ArrayList<>();
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Optional<Tenant> tenant = tenantRepository.findById(us.get().getTenantId());
			if (tenant.isPresent()) {
				
				name.add(tenant.get().getTenantName());
				if(us.get().getRoleName().equals(StringIteration.USER)) {
				Optional<User> u = userRepository.findById(us.get().getHierarachyId());
				if (u.isPresent()) {
		
					name.add(u.get().getFirstName()+" "+u.get().getLastName());
					
				}}else if(us.get().getRoleName().equals(StringIteration.MANAGER)) {
					Optional<User> u = userRepository.findById(us.get().getUserId());
					if (u.isPresent()) {
			
						name.add(u.get().getFirstName()+" "+u.get().getLastName());
					}
				}
			}

			
			String[] s = { user.getEmail() };
			VelocityContext context = new VelocityContext();

			context.put("name", ConfigUtil.getAppLink());
			context.put(StringIteration.LANGUAGE, "en");
			context.put(StringIteration.FULLNAME, user.getFirstName() + " " + user.getLastName());
			context.put("chequenumber", ch.getChequeNumber());
			context.put("chequeCancelDate", ch.getChequeCancelDate());
			context.put("reason", ch.getRemarks());
			context.put("amount", ch.getAmount());
			context.put("depositDate", ch.getDepositDate());
			context.put("invoice", ch.getInvoice());
			StringWriter writer = new StringWriter();
			String templateStr = emailTemplate.getMailTemplate();
			Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
			emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),name);
		}
	}

	public void securityChequeMail(User user, String template) {
		EmailTemplate emailTemplate = emailTemplateService.findById(template);
		if (emailTemplate != null) {
			List<String> name=new ArrayList<>();
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			Optional<Tenant> tenant = tenantRepository.findById(us.get().getTenantId());
			if (tenant.isPresent()) {
				
				name.add(tenant.get().getTenantName());
				if(us.get().getRoleName().equals(StringIteration.USER)) {
					Optional<User> u = userRepository.findById(us.get().getHierarachyId());
					if (u.isPresent()) {
			
						name.add(u.get().getFirstName()+" "+u.get().getLastName());
						
					}}else if(us.get().getRoleName().equals(StringIteration.MANAGER)) {
						Optional<User> u = userRepository.findById(us.get().getUserId());
						if (u.isPresent()) {
				
							name.add(u.get().getFirstName()+" "+u.get().getLastName());
						}
					}			}

			
			
			
			String[] s = { user.getEmail() };
			VelocityContext context = new VelocityContext();

			context.put("name", ConfigUtil.getAppLink());
			context.put("language", "en");
			context.put("fullname", user.getFirstName() + " " + user.getLastName());
			StringWriter writer = new StringWriter();
			String templateStr = emailTemplate.getMailTemplate();
			Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
			emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),name);
		}
	}

	public static boolean isNumber(String numberText) {
		try {
			Integer.parseInt(numberText);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static List<String> prepareCheque(String chequeText) {
		List<String> cheques = new ArrayList<>();
		try {
			String[] strings = chequeText.split(",");

			for (String string : strings) {
				String[] parts = string.split("-");
				if (parts.length > 2) {
					throw new RuntimeException(Constants.CHEQUE_FORMAT_EXCEPTION);
				} else if (parts.length == 2) {
					String from = parts[0];
					String to = parts[1];

					if (isNumber(from) && isNumber(to)) {
						int digit = from.length();
						for (int i = Integer.parseInt(from); i <= Integer.parseInt(to); i++) {
							cheques.add(String.format("%0" + digit + "d", i));
						}
					} else {
						throw new RuntimeException(Constants.CHEQUE_FORMAT_EXCEPTION);
					}
				} else {
					if (isNumber(string)) {
						cheques.add(string);
					} else {
						throw new RuntimeException(Constants.CHEQUE_FORMAT_EXCEPTION);
					}

				}

			}
		} catch (Exception e) {
			cheques.clear();
			e.printStackTrace();
		}
		return cheques;
	}

	/**
	 * 
	 * @param data
	 * @param chequeNumber
	 * @return
	 */
	private Cheque getCheque(ChequeData data, String chequeNumber) {
		Cheque cheque = new Cheque();
		cheque.setStatus(data.getStatus());
		cheque.setSapId(data.getSapId());
		cheque.setStockistId(data.getStockistId());
		cheque.setBankId(data.getBankId());
		cheque.setReciveDate(data.getReciveDate());
		cheque.setRecivedVia(data.getRecivedVia());
		cheque.setChequeNumber(chequeNumber);
		cheque.setIsChequeHold(false);
		cheque.setIsReturned(false);

		return cheque;
	}

	public void returMail(User user, Cheque che, String temp) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				EmailTemplate emailTemplate = emailTemplateService.findById(temp);
				List<String> list = new ArrayList<>();
				List<String> name = new ArrayList<>();
				if (emailTemplate != null) {
					Optional<Tenant> tenant = tenantRepository.findById(us.get().getTenantId());
					if (tenant.isPresent()) {
						list.add(tenant.get().getEmailId());
						name.add(tenant.get().getTenantName());
						 if(us.get().getRoleName().equals(StringIteration.USER)) {
						Optional<User> u = userRepository.findById(us.get().getHierarachyId());
						if (u.isPresent()) {
							list.add(u.get().getEmail());
							name.add(u.get().getFirstName()+" "+u.get().getLastName());
						}
						}else if(us.get().getRoleName().equals(StringIteration.MANAGER)) {
							Optional<User> u = userRepository.findById(us.get().getUserId());
						
								list.add(u.get().getEmail());
								name.add(u.get().getFirstName()+" "+u.get().getLastName());
							
						}
					}

					VelocityContext context = new VelocityContext();
					context.put("origin", ConfigUtil.getAppLink());
					context.put("language", "en");
					context.put("fullname", user.getFirstName() + " " + user.getLastName());
					context.put("chequenumber", che.getChequeNumber());
					context.put("chequeCancelDate", che.getChequeCancelDate());
					context.put("reason", che.getRemarks());
					context.put("amount", che.getAmount());
					context.put("depositDate", che.getDepositDate());
					context.put("invoice", che.getInvoice());

					StringWriter writer = new StringWriter();
					String templateStr = emailTemplate.getMailTemplate();
					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
					String[] s = list.toArray(new String[0]);
					emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),name);
				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

	}

	public int getLimit() {
		return limit;
	}

	public int setLimit(int limit) {
		this.limit = limit;
		return this.limit;
	}
	
	public void sendMailManagerAndTenant(User user, Cheque che, String temp) {
		try {
			String name = new String();
				EmailTemplate emailTemplate = emailTemplateService.findById(temp);
				List<String> list = new ArrayList<>();
				String stockist ="";
				String location = "";
				if (emailTemplate != null) {
					Optional<Tenant> tenant = tenantRepository.findById(user.getTenantId());
					if (tenant.isPresent()) {
						Tenant tn = tenant.get();
						list.add(tn.getEmailId());
						name = tn.getFirstName();
						 if(user.getRoleName().equals(StringIteration.USER)) {
						Optional<User> u = userRepository.findById(user.getHierarachyId());
						if (u.isPresent()) {
							User us = u.get();
							list.add(us.getEmail());
							name = name.concat(", "+us.getFirstName());
						}
						}else if(user.getRoleName().equals(StringIteration.MANAGER)) {
								list.add(user.getEmail());
								name = name.concat(", "+user.getFirstName());
						}
						 
						 Optional<Stockist> st = stockistRepository.findById(che.getStockistId());
						 if(st.isPresent()) {
							 Stockist s =st.get();
							 stockist = s.getStockistName();
							 Optional<String> c = cityRepository.findByCityCode(s.getCityId());
							 if(c.isPresent()) {
								 location = c.get();
							 }
						 }
					}

					VelocityContext context = new VelocityContext();
					context.put("origin", ConfigUtil.getAppLink());
					context.put("language", "en");
					context.put("fullname", user.getFirstName() + " " + user.getLastName());
					context.put("chequeNumber", che.getChequeNumber());
					context.put("stockist",stockist);
					context.put("chequeCancelDate",dateFormatter( che.getChequeCancelDate()));
					context.put("reason", che.getRemarks());
					context.put("amount", che.getAmount());
					context.put("depositDate",dateFormatter( che.getDepositDate()));
					context.put("invoice", che.getInvoice());
					context.put("invoiceHold", che.getInvoiceHold());
					context.put("tenantName",tenant != null ? tenant.get().getTenantName() : "");
					context.put("location",location);
					context.put("quantity", che.getQuantity());

					StringWriter writer = new StringWriter();
					String templateStr = emailTemplate.getMailTemplate();
					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
					String[] s = list.toArray(new String[0]);
					emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),name,user);
				}
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

	}
	
	private String dateFormatter(Date d) {
	    if (d == null) {
	        return null;
	    } else {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Format for "day month date"
	            return sdf.format(d);
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error(e.getMessage());
	        }
	    }
	    return null;
	}
	
	public void sendMailStockist(User user, Cheque che, String temp) {
		try {
			String location = "";

				EmailTemplate emailTemplate = emailTemplateService.findById(temp);
					if (emailTemplate != null) {
						Optional<Stockist> stk = stockistRepository.findById(che.getStockistId());
						if (stk.isPresent()) {
							Stockist st = stk.get();
							     Optional<String> c = cityRepository.findByCityCode(st.getCityId());
								 if(c.isPresent()) {
									 location = c.get();
								 }
							 	
						Optional<Tenant> tenant = tenantRepository.findById(user.getTenantId());	
						
						VelocityContext context = new VelocityContext();
						context.put("origin", ConfigUtil.getAppLink());
						context.put("language", "en");
						context.put("fullname", user.getFirstName() + " " + user.getLastName());
						context.put("chequeNumber", che.getChequeNumber());
						context.put("stockist",st.getStockistName());
						context.put("chequeCancelDate",dateFormatter( che.getChequeCancelDate()));
						context.put("reason", che.getRemarks());
						context.put("amount", che.getAmount());
						context.put("depositDate",dateFormatter( che.getDepositDate()));
						context.put("invoice", che.getInvoice());
						context.put("invoiceHold", che.getInvoiceHold());
						context.put("tenantName",tenant != null ? tenant.get().getTenantName() : "");
						context.put("location",location);
						
						StringWriter writer = new StringWriter();
						String templateStr = emailTemplate.getMailTemplate();
						Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
						String[] s = {st.getEmail()};
						emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(),st.getStockistName(),user);
					}
				}
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

	}
	
	

}