package com.healthtraze.etraze.api.masters.service;

import java.io.BufferedReader; 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.healthtraze.etraze.api.file.model.FileResponse;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.dto.InvoiceDetails;
import com.healthtraze.etraze.api.masters.dto.InvoicedItemsDto;
import com.healthtraze.etraze.api.masters.dto.ItemsDto;
import com.healthtraze.etraze.api.masters.dto.OrderDTO;
import com.healthtraze.etraze.api.masters.dto.OrderImport;
import com.healthtraze.etraze.api.masters.dto.OrderMobileDTO;
import com.healthtraze.etraze.api.masters.dto.OrderTransporterDTO;
import com.healthtraze.etraze.api.masters.dto.TicketDEMO;
import com.healthtraze.etraze.api.masters.dto.TicketInvoiceDTO;
import com.healthtraze.etraze.api.masters.dto.TicketOrderDTO;
import com.healthtraze.etraze.api.masters.dto.TicketReportsDTO;
import com.healthtraze.etraze.api.masters.model.InvoiceLineItems;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.TempInvoice;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketOrder;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.model.TicketStatusHistory;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.InvoiceLineItemsRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TempInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderRepository;
import com.healthtraze.etraze.api.masters.repository.TicketRepository;
import com.healthtraze.etraze.api.masters.repository.TicketStatusHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class TicketOrderService implements BaseService<TicketOrder, String> {

	private Logger logger = LogManager.getLogger(TicketOrderService.class);

	private final TicketOrderRepository orderRepository;

	private final TenantRepository tenantRepository;

	private final TicketRepository ticketRepository;

	private final TicketOrderInvoiceRepository invoiceRepository;

	private final TransportRepository transportRepository;

	private final UserRepository userRepository;

	private final StockistRepository stockistRepository;

	private final TicketStatusHistoryRepository ticketStatusHistoryRepository;

	private final FileStorageService fileStorageService;

	private final TenantManufactureRepository tenantManufactureRepository;

	private TicketService ticketService;
	
	private TempInvoiceRepository tempInvoiceRepository;
	
	private InvoiceLineItemsRepository invoiceLineItemsRepository;

	@Autowired
	public TicketOrderService(TicketOrderRepository orderRepository, CityRepository cityRepository,
			TenantRepository tenantRepository, TicketRepository ticketRepository,
			TicketOrderInvoiceRepository invoiceRepository, TransportRepository transportRepository,
			UserRepository userRepository, StockistRepository stockistRepository,
			TicketStatusHistoryRepository ticketStatusHistoryRepository, FileStorageService fileStorageService,
			TicketService ticketService, TenantManufactureRepository tenantManufactureRepository, 
			TempInvoiceRepository tempInvoiceRepository,InvoiceLineItemsRepository invoiceLineItemsRepository) {
		this.orderRepository = orderRepository;
		this.tenantRepository = tenantRepository;
		this.ticketRepository = ticketRepository;
		this.invoiceRepository = invoiceRepository;
		this.transportRepository = transportRepository;
		this.userRepository = userRepository;
		this.stockistRepository = stockistRepository;
		this.ticketStatusHistoryRepository = ticketStatusHistoryRepository;
		this.fileStorageService = fileStorageService;
		this.ticketService = ticketService;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.tempInvoiceRepository = tempInvoiceRepository ;
		this.invoiceLineItemsRepository = invoiceLineItemsRepository;
	}

	@Override
	public List<TicketOrder> findAll() {
		return Collections.emptyList();
	}

	public List<TicketOrderInvoice> findAllTicketOrderInvoice() {

		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				return invoiceRepository.getAllInvoie(us.get().getTenantId());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<OrderMobileDTO> allOrderMobile(String status) {
		List<OrderMobileDTO> list = new ArrayList<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				List<Object[]> ob = new ArrayList<>();
				if (u.getRoleName().equals(StringIteration.MANAGER)) {
					ob = orderRepository.allOrderMobileByManager(u.getUserId(), u.getTenantId(), status);
				} else if (u.getRoleName().equals(StringIteration.USER)) {
					ob = orderRepository.allOrderMobileByUser(u.getUserId(), u.getTenantId(), status);
				}
				for (Object[] b : ob) {
					OrderMobileDTO or = new OrderMobileDTO();

					or.setInvoiceNumber(String.valueOf(b[0]));
					or.setTicketId(String.valueOf(b[1]));
					or.setCustomerRefNumber(String.valueOf(b[2]));
					or.setStockistId(String.valueOf(b[3]));
					if (String.valueOf(b[3]) != null) {
						Optional<Stockist> s = stockistRepository.findById(or.getStockistId());
						if (s.isPresent()) {
							or.setStockistName(s.get().getStockistName());
						}
					}
					or.setEmailDate(String.valueOf(b[4]));
					or.setTicketstatus(String.valueOf(b[5]));
					or.setInvoiceStatus(String.valueOf(b[6]));
					or.setPriority(String.valueOf(b[7]));
					list.add(or);
				}
				return list;
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<TicketReportsDTO> findAllTicketOrder(String search) {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());

			if (u.isPresent()) {
				User us = u.get();
				List<Object[]> ob = new ArrayList<>();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					ob = orderRepository.getAllOrdersList(us.getUserId(), us.getTenantId(), "");
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					ob = orderRepository.getAllOrdersList(us.getHierarachyId(), us.getTenantId(), "");
				}

				Optional<Tenant> t = tenantRepository.findById(us.getTenantId());

				if (t.isPresent()) {
					Map<String, TicketReportsDTO> value = findAllTicketOrderValue(ob, t, us);

					List<TicketReportsDTO> tr = new ArrayList<>(value.values());

					if (search != null && !search.isEmpty()) {
						return tr.stream().filter(obj -> {
							return obj.getTicketNumber().toLowerCase().contains(search.toLowerCase())
									|| obj.getMergedInvoice().toLowerCase().contains(search.toLowerCase())
									|| obj.getStockist().toLowerCase().contains(search.toLowerCase())
									|| obj.getStatus().toLowerCase().contains(search.toLowerCase())
									|| obj.getLocation().toLowerCase().contains(search.toLowerCase());
						}).collect(Collectors.toList());
					}
					
					for(TicketReportsDTO dt : tr) {
						int cartons = 0 ;
						List<InvoiceDetails> li = new ArrayList<>();
						List<TicketOrderInvoice> toi = invoiceRepository.findMergeInvoice(dt.getPackId(), us.getTenantId());
			    			for(TicketOrderInvoice tt: toi) {
			    				InvoiceDetails inv = new InvoiceDetails();
			    				inv.setInvoice(tt.getInvoiceNumber());
			    				inv.setCartons(tt.getNumOfCases());
			    				inv.setInvoiceDate(tt.getCreatedOn());
			    				inv.setInvoicevalue(tt.getInvoiceValue());
//			    				inv.setManufacturer(invoiceRepository.findManfNameByTicketId(tt.getTicketId() ,us.getTenantId()));
			    				li.add(inv);
			    				cartons += Integer.parseInt(inv.getCartons());
			    			}
			    			dt.setCartonsDetails(li);
			    			dt.setTotalCartons(cartons);
			    			
			    			
			    			
					}
					return tr;

				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();

	}

	private Map<String, TicketReportsDTO> findAllTicketOrderValue(List<Object[]> ob, Optional<Tenant> t, User us) {

		Map<String, TicketReportsDTO> value = new HashMap<>();
		for (Object[] b : ob) {
			TicketReportsDTO td = new TicketReportsDTO();
			td.setTenantId(us.getTenantId());
			td.setTenantId(us.getTenantId());
			if (t.isPresent()) {
				td.setTenantName(t.get().getTenantName());
			}

			td.setTicketNumber(String.valueOf(b[0]));
			td.setInvoiceNumber(String.valueOf(b[1]));
			td.setMergedInvoice(String.valueOf(b[1]));
			td.setManufacturer(String.valueOf(b[2]));
			if (String.valueOf(b[3]) != null) {
				td.setNoOfCases(String.valueOf(b[3]));
			} else {
				td.setNoOfCases("0");
			}
			td.setStatus(String.valueOf(b[4]));
			td.setPinCode(String.valueOf(b[5]));
			td.setManufacturerLocation(String.valueOf(b[6]));
			td.setDistributionModel(String.valueOf(b[7]));
			td.setStockist(String.valueOf(b[8]));
			td.setLocation(String.valueOf(b[9]));
			td.setStockistId(String.valueOf(b[10]));
			td.setPriority(String.valueOf(b[11]));
			
			td.setPackId(String.valueOf(b[13]));
			if(b[14]!=null){
			    td.setTransporter(String.valueOf(b[14]));
			}else {
				td.setTransporter("");
			}
			td.setMultiQr((Boolean)b[15]);
			String key = td.getPackId() + td.getStatus();
			td.setStatus(String.valueOf(b[12]));
			if ("true".equals(String.valueOf(b[11]))) {
				value.put(key + 1, td);
			} else if (!value.containsKey(key)) {
				value.put(key, td);
			} else {
				TicketReportsDTO tdo = value.get(key);
				BigInteger cases = new BigInteger(tdo.getNoOfCases());
				cases = cases.add(new BigInteger(td.getNoOfCases()));
				StringBuilder str = new StringBuilder(tdo.getMergedInvoice());
				str.append(", ");
				str.append(td.getMergedInvoice());

				tdo.setMergedInvoice(str.toString());
				tdo.setNoOfCases(cases.toString());

				if (!tdo.getTicketNumber().equals(td.getTicketNumber())) {
					tdo.setTicketNumber(tdo.getTicketNumber() + ", " + td.getTicketNumber());
				}

				value.put(key, tdo);
			}

		}
		return value;

	}

	public List<OrderTransporterDTO> getAllTransporterAssignedOrders(String search) {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();
				List<Object[]> ob = new ArrayList<>();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					ob = orderRepository.getAllTransporterAssignedOrders(us.getUserId(), us.getTenantId(), "");
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					ob = orderRepository.getAllTransporterAssignedOrders(us.getHierarachyId(), us.getTenantId(), "");
				}
				List<OrderTransporterDTO> value = new ArrayList<>();
				prepareOrder(value, ob);

				if (search != null && !search.isEmpty()) {
					value = value.stream().filter(obj -> {
						return obj.getTransporterId().toLowerCase().contains(search.toLowerCase())
								|| obj.getVehicleNo().toLowerCase().contains(search.toLowerCase())
						        || obj.getStockistName().toLowerCase().contains(search.toLowerCase())
						        || obj.getInvoiceNumber().toLowerCase().contains(search.toLowerCase())
						        || obj.getLocation().toLowerCase().contains(search.toLowerCase());
					}).collect(Collectors.toList());
				}
				return value;
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public void prepareOrder(List<OrderTransporterDTO> st, List<Object[]> ob) {
		List<TicketReportsDTO> list = new ArrayList<>();
		for (Object[] b : ob) {
			TicketReportsDTO td = new TicketReportsDTO();
			td.setTicketNumber(String.valueOf(b[0]));
			td.setInvoiceNumber(String.valueOf(b[1]));
			td.setMergedInvoice(String.valueOf(b[1]));
			td.setManufacturer(String.valueOf(b[2]));
			if (String.valueOf(b[3]) != null) {
				td.setNoOfCases(String.valueOf(b[3]));
			} else {
				td.setNoOfCases("0");
			}
			td.setStatus(String.valueOf(b[4]));
			td.setPinCode(String.valueOf(b[5]));
			td.setManufacturerLocation(String.valueOf(b[6]));
			td.setDistributionModel(String.valueOf(b[7]));
			td.setStockist(String.valueOf(b[8]));
			td.setLocation(String.valueOf(b[9]));
			td.setStockistId(String.valueOf(b[10]));
			td.setPriority(String.valueOf(b[11]));
			td.setPackId(String.valueOf(b[13]));
			td.setStatus(String.valueOf(b[12]));
			td.setVehicaleNo(String.valueOf(b[14]));
			td.setTransporter(String.valueOf(b[15]));
			list.add(td);
		}
		Map<Object, List<TicketReportsDTO>> val = list.stream().collect(Collectors.groupingBy(
				dto -> dto.getTransporter() + "-" + dto.getVehicaleNo(), LinkedHashMap::new, Collectors.toList()));

		for (List<TicketReportsDTO> vu : val.values()) {
			OrderTransporterDTO td = new OrderTransporterDTO();
			td.setTransporterId(vu.get(0).getTransporter());
			td.setVehicleNo(vu.get(0).getVehicaleNo());
			td.setStockistName(vu.get(0).getStockist());
			td.setInvoiceNumber(vu.get(0).getInvoiceNumber());
			td.setLocation(vu.get(0).getLocation());

			Map<Object, List<TicketReportsDTO>> valee = vu.stream().collect(
					Collectors.groupingBy(TicketReportsDTO::getPackId, LinkedHashMap::new, Collectors.toList()));
			List<List<TicketReportsDTO>> all = new ArrayList<>();
			all.addAll(valee.values());
			td.setList(all);
			st.add(td);
		}

	}

	public HashMap<String, Object> findAllPackedOrder(String search, String location) {
		HashMap<String, Object> map = new HashMap<>();
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());

			if (u.isPresent()) {
				User us = u.get();
				List<Object[]> ob = getAllPackedOrdersList(us, location);
				Optional<Tenant> t = tenantRepository.findById(us.getTenantId());

				if (t.isPresent()) {
					Map<String, TicketReportsDTO> value = findAllPackedOrderValues(ob, us, t);
					List<TicketReportsDTO> tr = new ArrayList<>(value.values());

					if (search != null && !search.isEmpty()) {
						tr = tr.stream()
								.filter(obj -> obj.getTicketNumber().toLowerCase().contains(search.toLowerCase())
										|| obj.getMergedInvoice().toLowerCase().contains(search.toLowerCase())
										|| obj.getStockist().toLowerCase().contains(search.toLowerCase())
										|| obj.getStatus().toLowerCase().contains(search.toLowerCase())
										|| obj.getLocation().toLowerCase().contains(search.toLowerCase()))
								.collect(Collectors.toList());
					}

					int sumOfNoOfCases = 0;

					for (TicketReportsDTO dto : tr) {
						int noOfCases = Integer.parseInt(dto.getNoOfCases());
						sumOfNoOfCases += noOfCases;
					}

					map.put("list", tr);
					map.put("totalNumOfCases", sumOfNoOfCases);// orderRepository.getAllPackedOrdersTotalCases(us.getUserId(),us.getTenantId(),location)
					return map;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new HashMap<>();
	}

	private List<Object[]> getAllPackedOrdersList(User us, String location) {
		if (us.getRoleName().equals(StringIteration.MANAGER)) {
			return StringUtils.isNullOrEmpty(location)
					? orderRepository.getAllPackedOrdersList(us.getUserId(), us.getTenantId())
					: orderRepository.getAllPackedOrdersList(us.getUserId(), us.getTenantId(), location);
		} else if (us.getRoleName().equals(StringIteration.USER)) {
			return StringUtils.isNullOrEmpty(location)
					? orderRepository.getAllPackedOrdersList(us.getHierarachyId(), us.getTenantId())
					: orderRepository.getAllPackedOrdersList(us.getHierarachyId(), us.getTenantId(), location);
		}
		return Collections.emptyList();
	}

	private Map<String, TicketReportsDTO> findAllPackedOrderValues(List<Object[]> ob, User us, Optional<Tenant> t) {

		Map<String, TicketReportsDTO> value = new HashMap<>();
		for (Object[] b : ob) {
			TicketReportsDTO td = new TicketReportsDTO();
			td.setTenantId(us.getTenantId());
			td.setTenantName(t.get().getTenantName());
			td.setTicketNumber(String.valueOf(b[0]));
			td.setInvoiceNumber(String.valueOf(b[1]));
			td.setMergedInvoice(String.valueOf(b[1]));
			td.setManufacturer(String.valueOf(b[2]));
			if (String.valueOf(b[3]) != null) {
				td.setNoOfCases(String.valueOf(b[3]));
			} else {
				td.setNoOfCases("0");
			}
			td.setStatus(String.valueOf(b[4]));
			td.setPinCode(String.valueOf(b[5]));
			td.setManufacturerLocation(String.valueOf(b[6]));
			td.setDistributionModel(String.valueOf(b[7]));
			td.setStockist(String.valueOf(b[8]));
			td.setLocation(String.valueOf(b[9]));
			td.setStockistId(String.valueOf(b[10]));
			td.setPriority(String.valueOf(b[11]));
			td.setPackId(String.valueOf(b[13]));
			String key = td.getPackId() + td.getStatus();
			td.setStatus(String.valueOf(b[12]));
			if ("true".equals(String.valueOf(b[11]))) {
				value.put(key + 1, td);
			} else if (!value.containsKey(key)) {
				value.put(key, td);
			} else {
				TicketReportsDTO tdo = value.get(key);
				BigInteger cases = new BigInteger(tdo.getNoOfCases());
				cases = cases.add(new BigInteger(td.getNoOfCases()));
				StringBuilder str = new StringBuilder(tdo.getMergedInvoice());
				str.append(", ");
				str.append(td.getMergedInvoice());

				tdo.setMergedInvoice(str.toString());
				tdo.setNoOfCases(cases.toString());

				if (!tdo.getTicketNumber().equals(td.getTicketNumber())) {
					tdo.setTicketNumber(tdo.getTicketNumber() + ", " + td.getTicketNumber());
				}

				value.put(key, tdo);
			}

		}

		return value;
	}

	public Result<TicketReportsDTO> spliceMergedInvoice(TicketReportsDTO t) {
		Result<TicketReportsDTO> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				List<TicketOrderInvoice> tic = invoiceRepository.findByPickId(t.getPackId(), u.getTenantId());
				tic.forEach(li -> {
					Optional<TicketOrderInvoice> in = invoiceRepository.findById(li.getId());
					if (in.isPresent()) {
						TicketOrderInvoice inv = in.get();
						inv.setStatus(StringIteration.PACKED);
						inv.setTransporter("");
						inv.setVehicaleNo("");
						invoiceRepository.save(inv);

						auditStatus(inv.getTicketId(), inv.getStatus(), inv.getInvoiceNumber());
					}
				});
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("Removed...");
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.INVALID_USER);
			}

		} catch (Exception e) {
			logger.error("", e);
			result.setCode(StringIteration.ERROR_CODE3);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	public List<TicketReportsDTO> findAllTicketOrder1() {
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();

				List<TicketOrder> ticketOrder = orderRepository.getByOrderManager(u.getUserId(), u.getTenantId());
				for (TicketOrder tr : ticketOrder) {
					List<TicketOrderInvoice> invoices = invoiceRepository.findByTicketId(tr.getTicketId(),
							u.getTenantId());
					Map<String, List<TicketOrderInvoice>> filteredInvoiceByStatus = new HashMap<>();
					for (TicketOrderInvoice inv : invoices) {

						TicketReportsDTO td = new TicketReportsDTO();

					}
				}

			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();

	}

	public static List<TicketOrderInvoice> filterChequesByStatus(List<TicketOrderInvoice> invoices, String status) {
		return invoices.stream().filter(invoice -> status.equalsIgnoreCase(invoice.getSelectList()))
				.collect(Collectors.toList());
	}

	public HashMap<String, Object> findAllTicketOrderQr(String search, int page, String sortBy, String sortDir) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				List<Object[]> orders = new ArrayList<>();
				int totalItems = 0;

				sortBy = StringUtils.isNullOrEmpty(sortBy) ? "t.ticket_id" : sortBy;
				sortDir = StringUtils.isNullOrEmpty(sortDir) ? "DESC" : sortDir;

				Pageable paging = PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
		
				if (user.getRoleName().equals("MANAGER")) {
					if(!search.isEmpty()) {
						orders = orderRepository.getAllOrdersListInvoiceQR(user.getUserId(), user.getTenantId(), "");	
					}else {
					orders = orderRepository.getAllOrdersListInvoiceQR(user.getUserId(), user.getTenantId(), "",
							paging);
					totalItems = orderRepository.getAllOrdersListInvoiceQR(user.getUserId(), user.getTenantId(), "")
							.size();
				}
			   }else if (user.getRoleName().equals("USER")) {
					if(!search.isEmpty()) {
						orders = orderRepository.getAllOrdersListInvoiceQR(user.getHierarachyId(), user.getTenantId(), "");
					}else {
					orders = orderRepository.getAllOrdersListInvoiceQR(user.getHierarachyId(), user.getTenantId(), "",
							paging);
					totalItems = orderRepository
							.getAllOrdersListInvoiceQR(user.getHierarachyId(), user.getTenantId(), "").size();

				}}

				Optional<Tenant> tenantOptional = tenantRepository.findById(user.getTenantId());
				if (tenantOptional.isPresent()) {
					Tenant tenant = tenantOptional.get();
					List<TicketReportsDTO> ticketReports = populateTicketReports(orders, user, tenant);
					if (search != null && !search.isEmpty()) {
						ticketReports = filterTicketReports(ticketReports, search);
					}
					map.put("orders", ticketReports);
					map.put("totalCount", totalItems);
					return map;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	private List<TicketReportsDTO> populateTicketReports(List<Object[]> orders, User user, Tenant tenant) {
		List<TicketReportsDTO> ticketReports = new ArrayList<>();
		for (Object[] order : orders) {
			TicketReportsDTO ticketReport = new TicketReportsDTO();
			ticketReport.setTenantId(user.getTenantId());
			ticketReport.setTenantName(tenant.getTenantName());
			ticketReport.setTicketNumber(String.valueOf(order[0]));
			ticketReport.setInvoiceNumber(String.valueOf(order[1]));
			ticketReport.setManufacturer(String.valueOf(order[2]));
			ticketReport.setNoOfCases(String.valueOf(order[3] != null ? order[3] : "0"));
			ticketReport.setStatus(String.valueOf(order[4]));
			ticketReport.setPinCode(String.valueOf(order[5]));
			ticketReport.setManufacturerLocation(String.valueOf(order[6]));
			ticketReport.setDistributionModel(String.valueOf(order[7]));
			ticketReport.setStockist(String.valueOf(order[8]));
			ticketReport.setLocation(String.valueOf(order[9]));
			ticketReports.add(ticketReport);
		}
		return ticketReports;
	}

	private List<TicketReportsDTO> filterTicketReports(List<TicketReportsDTO> ticketReports, String search) {
		return ticketReports.stream()
				.filter(obj -> obj.getTicketNumber().toLowerCase().contains(search.toLowerCase())
						|| obj.getInvoiceNumber().toLowerCase().contains(search.toLowerCase())
						|| obj.getStockist().toLowerCase().contains(search.toLowerCase())
						|| obj.getStatus().toLowerCase().contains(search.toLowerCase())
						|| obj.getLocation().toLowerCase().contains(search.toLowerCase()))
				.collect(Collectors.toList());
	}

	public List<TicketOrderDTO> findAllTicketOrders() {
		try {
			return Collections.emptyList();

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();

	}

	@Override
	public TicketOrder findById(String id) {

		return null;
	}

	@Override
	public Result<TicketOrder> create(TicketOrder t) {

		return null;
	}

	@Override
	public Result<TicketOrder> update(TicketOrder t) {

		return null;
	}

	@Override
	public Result<TicketOrder> delete(String id) {

		return null;
	}

	public Result<TicketOrderDTO> findTicketOrders(String id) {
		Result<TicketOrderDTO> result = new Result<>();

		try {

			TicketOrderDTO ticketOrder = new TicketOrderDTO();
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				Optional<TicketOrder> tr = orderRepository.findByTicketId(id);

				if (tr.isPresent()) {
					TicketOrder trd = tr.get();
					if (trd.getTransporter() != null) {

						Optional<Transport> ts = transportRepository.findById(trd.getTransporter());
						if (ts.isPresent()) {
							trd.setTransportarName(ts.get().getTransportName());
						}
					}

					List<TicketOrderInvoice> invoices = invoiceRepository.findByTicketId(id, u.getTenantId());
					ticketOrder.setInvoices(invoices);
					ticketOrder.setTicketOrder(trd);
					result.setCode("0000");
					result.setData(ticketOrder);
					result.setMessage(StringIteration.SUCESS);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("");
			result.setData(new TicketOrderDTO());
			result.setCode(StringIteration.ERROR_CODE1);
		}

		return result;
	}

	private void auditStatus(String ticketId, String status, String invoice) {
		TicketStatusHistory history = new TicketStatusHistory();

		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (u.isPresent()) {
			history.setCreatedBy(u.get().getFirstName() + " " + u.get().getLastName());
			history.setTenantId(u.get().getTenantId());
		} else {
			history.setCreatedBy(SecurityUtil.getUserName());
		}
		history.setHistoryOn(new Date());
		history.setCreatedOn(new Date());
		history.setStatus(status);
		history.setInvoice(invoice);
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
	}

	public Map<String, Object> findTicketOrderInvoice(OrderMobileDTO td) {
		Map<String, Object> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());

			if (us.isPresent()) {
				User u = us.get();
				tenantName(map, u);
				setWmsAndFloor(map, u, td);
				TicketOrderInvoice to = invoiceRepository.getInvoie(td.getTicketId(), td.getInvoiceNumber());

				if (to.getStatus().equals("INVOICE CREATED") && Boolean.TRUE.equals(to.getIsSelected())) {
					List<TicketOrderInvoice> list = invoiceRepository.findInvoiceBySelectList(to.getSelectList(),
							us.get().getTenantId());
					map.put(StringIteration.MERGEDINVOICE,
							list.stream().map(TicketOrderInvoice::getInvoiceNumber).collect(Collectors.joining(",")));
				} else if (to.getStatus().equals("PACKED")
						|| to.getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
					List<TicketOrderInvoice> list = invoiceRepository.findMergeInvoice(to.getPackId(),
							us.get().getTenantId());
					map.put("mergedInvoice",
							list.stream().map(TicketOrderInvoice::getInvoiceNumber).collect(Collectors.joining(",")));
				} else {
					map.put("mergedInvoice", to.getInvoiceNumber());
				}

				map.put("result", to);

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return map;
	}

	private void tenantName(Map<String, Object> map, User us) {
		Optional<Tenant> t = tenantRepository.findById(us.getTenantId());
		if (t.isPresent()) {
			map.put("tenantName", t.get().getTenantName());
		}
	}

	private void setWmsAndFloor(Map<String, Object> map, User us, OrderMobileDTO dto) {
		Optional<Ticket> tk = ticketRepository.findById(dto.getTicketId());
		if (tk.isPresent()) {
			Optional<TenantManufacture> tm = tenantManufactureRepository
					.findByManufactureId(tk.get().getManufacturerId(), us.getTenantId());
			if (tm.isPresent()) {
				TenantManufacture t = tm.get();
				map.put("wms", t.isWmsed());
				map.put("floorWise", t.isFloorWise());
			}
		}
	}

	public Result<TicketOrderInvoice> updateTicketOrderInvoice(TicketOrderInvoice t) {
		Result<TicketOrderInvoice> result = new Result<>();
		try {
			Optional<User> user = getUserFromRepository();
			if (user.isPresent()) {
				User u = user.get();
				TicketOrderInvoice ti = getInvoice(t.getTicketId(), t.getInvoiceNumber());
				if (Boolean.TRUE.equals(Boolean.TRUE.equals(t.getIsSelected() && t.getSelectList() != null))
						&& t.getStatus().equals("PICKED")) {
					handleSelectedInvoices(t, u);
				} else {
					handleNonSelectedInvoice(t, ti, u);
				}
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);
			}
		} catch (Exception e) {
			result.setMessage("");
			result.setData(new TicketOrderInvoice());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private Optional<User> getUserFromRepository() {
		return userRepository.findByUserId(SecurityUtil.getUserName());
	}

	private TicketOrderInvoice getInvoice(String ticketId, String invoiceNumber) {
		return invoiceRepository.getInvoie(ticketId, invoiceNumber);
	}

	private void handleSelectedInvoices(TicketOrderInvoice t, User user) {
		List<TicketOrderInvoice> list = invoiceRepository.findInvoiceBySelectList(t.getSelectList(),
				user.getTenantId());
		for (TicketOrderInvoice i : list) {
			updateInvoiceStatusAndSave(i, t.getStatus());
		}
	}

	private void handleNonSelectedInvoice(TicketOrderInvoice t, TicketOrderInvoice ti, User user) {
		if (ti.getStatus().equals("CHECKED")) {
			ti.setNumOfCases(t.getNumOfCases().trim());
			ti.setPriority(t.getPriority());
			Optional<Ticket> ticketOptional = ticketRepository.findById(t.getTicketId());
			ticketOptional.ifPresent(ticket -> {
				Optional<TicketOrderInvoice> pickId = invoiceRepository
						.findByStockistAnsStatusNotpriority(ticket.getStockistId(), user.getTenantId());
				setPackId(pickId, ti, t);
			});
		}
		updateInvoiceStatusAndSave(ti, t.getStatus());
	}

	private void setPackId(Optional<TicketOrderInvoice> invoice, TicketOrderInvoice ti, TicketOrderInvoice t) {
		if (invoice.isPresent() && invoice.get().getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
			ti.setPackId(invoice.get().getPackId());
			ti.setTransporter(invoice.get().getTransporter());
			t.setStatus(StringIteration.TRANSPORTER_ASSIGNED);
		} else if (invoice.isPresent() && !ti.getPriority()) {
			ti.setPackId(invoice.get().getPackId());
		} else {
			ti.setPackId(String.valueOf(System.currentTimeMillis()));
		}
	}

	private void updateInvoiceStatusAndSave(TicketOrderInvoice invoice, String status) {
		invoice.setStatus(status);
		TicketOrderInvoice tn = invoiceRepository.save(invoice);
		auditStatus(tn.getTicketId(), tn.getStatus(), tn.getInvoiceNumber());
	}

	public Result<TicketOrderInvoice> packTicketOrderInvoice(TicketOrderInvoice t) {
		Result<TicketOrderInvoice> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				TicketOrderInvoice ti = invoiceRepository.getInvoie(t.getTicketId(), t.getInvoiceNumber());
				if (Boolean.TRUE.equals(t.getIsSelected()) && t.getSelectList() != null) {
					List<TicketOrderInvoice> list = invoiceRepository.findInvoiceBySelectList(t.getSelectList(),
							u.getTenantId());
					for (TicketOrderInvoice i : list) {

						i.setStatus(t.getStatus());
						TicketOrderInvoice tn = invoiceRepository.save(i);
						auditStatus(tn.getTicketId(), tn.getStatus(), tn.getInvoiceNumber());
						result.setCode("0000");
						result.setData(tn);
						result.setMessage(StringIteration.SUCESS);
					}
				} else {
					if (ti.getStatus().equals("CHECKED")) {
						ti.setNumOfCases(t.getNumOfCases());
					}
					ti.setStatus(t.getStatus());
					TicketOrderInvoice tn = invoiceRepository.save(ti);
					auditStatus(tn.getTicketId(), tn.getStatus(), tn.getInvoiceNumber());
					result.setCode("0000");
					result.setData(tn);
					result.setMessage("sucess");

				}

			}

		} catch (Exception e) {
			result.setMessage("");
			result.setData(new TicketOrderInvoice());
			result.setCode(StringIteration.ERROR_CODE1);

		}

		return result;
	}

	public List<TicketOrderInvoice> createPickedList(List<TicketOrderInvoice> ti) {
		List<TicketOrderInvoice> createdInvoices = new ArrayList<>();
		try {
			String l = invoiceRepository.getLastSequence();
			BigInteger sl;
			if (l != null) {
				BigInteger b = new BigInteger(l);
				sl = b.add(BigInteger.ONE);

			} else {
				sl = BigInteger.ONE;
			}

			for (TicketOrderInvoice tin : ti) {
				Optional<TicketOrderInvoice> optional = invoiceRepository.findById(tin.getId());
				if (optional.isPresent()) {
					TicketOrderInvoice invoice = optional.get();
					invoice.setIsSelected(true);
					invoice.setSelectList(sl.toString());
					TicketOrderInvoice createdInvoice = invoiceRepository.save(invoice);
					createdInvoices.add(createdInvoice);
				}
			}
			return createdInvoices;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Result<TicketOrderInvoice> editInvoice(TicketInvoiceDTO t) {
		Result<TicketOrderInvoice> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			Optional<TicketOrderInvoice> invoices = invoiceRepository.findById(t.getId());
			if (us.isPresent() && invoices.isPresent()) {
				User u = us.get();
				TicketOrderInvoice toi = invoices.get();

				Optional<TicketOrderInvoice> ini = invoiceRepository.findByInvoiceNumberAndTenantId(
						t.getInvoiceNumber().toLowerCase().replace(" ", ""), u.getTenantId());
				if (ini.isPresent() && !ini.get().getId().equals(t.getId())) {
					result.setCode(StringIteration.ERROR_CODE2);
					result.setMessage(t.getInvoiceNumber() + " Invoice Alredy Exist");
					return result;
				}
				Optional<TicketStatusHistory> th = ticketStatusHistoryRepository
						.findAByinvoiceNumber(toi.getInvoiceNumber());
				toi.setInvoiceNumber(t.getInvoiceNumber());
				toi.setLineItem(t.getLineItems());
				toi.setInvoiceValue(t.getInvoiceValue());
				toi.setDueDate(t.getDueDate());

				TicketOrderInvoice in = invoiceRepository.save(toi);

				if (th.isPresent()) {
					th.get().setInvoice(in.getInvoiceNumber());
					ticketStatusHistoryRepository.save(th.get());
				}

				Optional<TicketOrder> to = orderRepository.findById(t.getTicketId());
				if (to.isPresent()) {
					TicketOrder order = to.get();
					order.setCcdRequired(t.getCcdRequired());
					order.setPodRequired(t.getPodRequired());
					orderRepository.save(order);
				}
				result.setCode("0000");
				result.setData(in);
				result.setMessage("sucess");
			} else {
				result.setCode("1111");
				result.setMessage("Error");
			}

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;

	}

	public List<TicketOrderInvoice> setStockistNameAndLocation(List<TicketOrderInvoice> invoices) {
		invoices.forEach(in -> {
			Optional<TicketDEMO> ob = invoiceRepository.getStockistNameAndLocationByTicketId(in.getTicketId(),
					in.getTenantId());
			if (ob.isPresent()) {
				TicketDEMO td = ob.get();
				in.setStockistName(td.getStockistName());
				in.setLocation(td.getCity());
			} else {
				in.setStockistName("");
				in.setLocation("");
			}

		});
		return invoices;
	}

	public Result<List<TicketOrderInvoice>> findInvoicedList(String search) {
		Result<List<TicketOrderInvoice>> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();

				List<TicketOrderInvoice> invoices = findInvoicedListByUser(u, search);

				setStockistNameAndLocation(invoices);

				if (!invoices.isEmpty()) {
					result.setCode("0000");
					result.setData(invoices);
					result.setMessage("Success");

				} else {
					result.setCode("XXXX");
					result.setMessage("No invoices found");
				}
			}

		} catch (Exception e) {

			result.setCode("1111");
			result.setMessage("Error: " + e.getMessage());
		}

		return result;
	}
	
	private List<TicketOrderInvoice> findInvoicedListByUser(User us ,String search){
		
		if(us.getRoleName().equals(StringIteration.MANAGER)) {
			 return	invoiceRepository.getInvoieceList(us.getUserId(), us.getTenantId(),search);
		}
		else if(us.getRoleName().equals(StringIteration.USER)) {
			return	invoiceRepository.getInvoieceList(us.getHierarachyId(), us.getTenantId(),search);
		}
		
		return Collections.emptyList();
	}

	public List<List<TicketOrderInvoice>> findPickedList(String search) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			List<List<TicketOrderInvoice>> ls = new ArrayList<>();
			if (us.isPresent()) {
				User u = us.get();
				List<TicketOrderInvoice> picked = invoiceRepository.getPickedList(u.getTenantId(), u.getUserId(),
						search);

				setStockistNameAndLocation(picked);

				Map<String, List<TicketOrderInvoice>> list = picked.stream().collect(Collectors
						.groupingBy(TicketOrderInvoice::getSelectList, LinkedHashMap::new, Collectors.toList()));

				ls.addAll(list.values());

				return ls;

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return Collections.emptyList();
	}

	public List<List<TicketOrderInvoice>> findPodList(String search) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			List<List<TicketOrderInvoice>> ls = new ArrayList<>();
			if (us.isPresent()) {
				User u = us.get();
				List<TicketOrderInvoice> pod = invoiceRepository.getPodList(u.getTenantId(), search);
				Map<String, List<TicketOrderInvoice>> list = pod.stream().collect(Collectors
						.groupingBy(TicketOrderInvoice::getInvoiceNumber, LinkedHashMap::new, Collectors.toList()));
				ls.addAll(list.values());

				return ls;

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return Collections.emptyList();
	}

	public List<TicketOrderInvoice> getPodRequiredList(String search) {
		try {
			List<TicketOrderInvoice> list = new ArrayList<>();
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			if (us.isPresent()) {
				User u = us.get();
				List<Object[]> pod = invoiceRepository.getPodRequiredList(u.getTenantId(), search);
				preparPodRequiredList(list, pod);
				return list;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return Collections.emptyList();
	}

	public void preparPodRequiredList(List<TicketOrderInvoice> list, List<Object[]> pod) {

		try {
			pod.forEach(l -> {
				TicketOrderInvoice invoice = new TicketOrderInvoice();
				invoice.setTicketId(String.valueOf(l[0]));
				invoice.setInvoiceNumber(String.valueOf(l[1]));
				invoice.setNumOfCases(String.valueOf(l[2]));
				invoice.setStockistName(String.valueOf(l[3]));
				invoice.setLocation(String.valueOf(l[4]));
				invoice.setStatus(String.valueOf(l[5]));
				list.add(invoice);
			});
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	public Result<List<TicketOrderInvoice>> findDispatchedList(String search) {
		Result<List<TicketOrderInvoice>> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();

				List<TicketOrderInvoice> invoices = invoiceRepository.getDispatchedList(u.getTenantId(), u.getUserId(),
						search);
				setStockistNameAndLocation(invoices);
				if (!invoices.isEmpty()) {
					result.setCode("0000");
					result.setData(invoices);
					result.setMessage("Success");
				} else {
					result.setCode("XXXX");
					result.setMessage("No invoices found");
				}
			}

		} catch (Exception e) {

			result.setCode("1111");
			result.setMessage("Error: " + e.getMessage());
		}

		return result;
	}

	public List<TicketOrderInvoice> addPickedList(String id, List<TicketOrderInvoice> ti) {
		List<TicketOrderInvoice> createdInvoices = new ArrayList<>();
		try {

			for (TicketOrderInvoice tin : ti) {
				TicketOrderInvoice optional = invoiceRepository.getById(tin.getId());

				optional.setIsSelected(true);
				optional.setSelectList(id);
				TicketOrderInvoice createdInvoice = invoiceRepository.save(optional);
				createdInvoices.add(createdInvoice);

			}
			return createdInvoices;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Result<TicketOrderInvoice> removePickedList(String tno, String inv) {

		Result<TicketOrderInvoice> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<TicketOrderInvoice> optional = invoiceRepository.findByInvoice(inv, tno, u.getTenantId());
				if (optional.isPresent()) {
					TicketOrderInvoice toi = optional.get();
					toi.setIsSelected(false);
					toi.setSelectList("");
					invoiceRepository.save(toi);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage("successfully removed");
					result.setData(toi);
				}
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return null;
	}

	public List<TicketInvoiceDTO> getLrDocumets(String ticketId) {
		Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			List<Object[]> toi = invoiceRepository.findByTicketIdForLrDocument(ticketId, user.getTenantId());
			return mapToObject(toi);
		}
		return Collections.emptyList();
	}

	private List<TicketInvoiceDTO> mapToObject(List<Object[]> toi) {
		List<TicketInvoiceDTO> invoiceDTOs = new ArrayList<>();
		toi.forEach(b -> {
			TicketInvoiceDTO dto = createTicketInvoiceDTO(b);
			invoiceDTOs.add(dto);
		});
		return invoiceDTOs;
	}

	private TicketInvoiceDTO createTicketInvoiceDTO(Object[] b) {
		TicketInvoiceDTO dto = new TicketInvoiceDTO();
		dto.setTransporterName(getStringOrNull(b[0]));
		dto.setVehicaleNo(getStringOrNull(b[1]));
		dto.setInvoiceNumber(getStringOrNull(b[2]));
		dto.setLrNumber(getStringOrNull(b[3]));
		dto.setLrRecivedDate(getStringOrNull(b[4]));
		dto.setLrDocument(getStringOrNull(b[5]));
		dto.setAddress(getStringOrNull(b[6]));
		dto.setDeliveryDate(getStringOrNull(b[8]));
		return dto;
	}

	private String getStringOrNull(Object obj) {
		return obj != null ? String.valueOf(obj) : null;
	}

	public Result<Object> getNumOfCase(String packId) {
		Result<Object> result = new Result<>();

		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				List<String> list = invoiceRepository.findNoOfCases(packId, u.getTenantId());

				int sum = 0;
				for (String number : list) {

					int num = Integer.parseInt(number);
					sum += num;
				}

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(sum);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Result<Object> fileReader(MultipartFile file, String sheetIndex) throws IOException {
		
		Result<Object> result = new Result<>();
		List<OrderDTO> valid = new ArrayList<>();
		List<OrderDTO> invalid = new ArrayList<>();		
		
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		if (us.isPresent()) {
			User u = us.get();
			
			try(FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
					XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);){

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
				OrderDTO toi = new OrderDTO();
				OrderImport orderImport = new OrderImport(true, row,toi );
				
				prepareRows(orderImport, row , toi);
				validation(orderImport , toi , u );			

				if (orderImport.isFlag()) {
					valid.add(toi);					
				} else {
					invalid.add(toi);
					toi.setRemarks(orderImport.getMessage());
				}

				rowIndex++;
			}			
			
			saveExcel(valid , result, u);
			writeInvalidExcel(invalid , result);
			
			return result ;
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}

		
			return result ;
		}
		
		return result;
	}	
	
	private void prepareRows(OrderImport o , Row row , OrderDTO dto){
		prepareInvoiceNumber(o,row,dto);
		prepareInvoiceValue(o,row,dto);
		prepareDueDate(o,row,dto);
		prepareLrNumber(o,row,dto);
		prepareLrDate(o,row,dto);
		prepareDeliveryDate(o,row,dto);
	}
	
	private void prepareInvoiceNumber(OrderImport o , Row row , OrderDTO dto) {
		try {
			
			Cell invoiceNo = row.getCell(0);
			if (invoiceNo == null || invoiceNo.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("Invoice number can not be empty, "); 
			} else {
				if (invoiceNo.getCellType() == CellType.NUMERIC) {
					double numericValue = invoiceNo.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.setInvoice(String.valueOf(longValue));
				} else if (invoiceNo.getCellType() == CellType.STRING) {
					dto.setInvoice(invoiceNo.getStringCellValue());
				}			
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invalid invoice number ,");
		}
	}
	
	private void prepareInvoiceValue(OrderImport o , Row row , OrderDTO dto) {
		try {
			
			Cell invoiceValue = row.getCell(1);
			if (invoiceValue == null || invoiceValue.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("invoice value can not be empty, ");
			} else {
				if (invoiceValue.getCellType() == CellType.NUMERIC) {
					double numericValue = invoiceValue.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.setInvoiceValue(String.valueOf(longValue));
				} else if (invoiceValue.getCellType() == CellType.STRING) {
					dto.setInvoiceValue(invoiceValue.getStringCellValue());
				}
			}
		}catch(Exception e) {
				logger.error(e.getMessage());
				o.setFlag(false);
				o.setMessage("invalid invoice value ,");
			}	
	}
	
	private void prepareDueDate(OrderImport o , Row row , OrderDTO dto) {
		try {
			Cell dueDate = row.getCell(2);
			if (dueDate == null || dueDate.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("Due Date cannot be empty, ");
			} else {
				if (dueDate.getCellType() == CellType.NUMERIC) {
					dto.setDueDate(dueDate.getDateCellValue());
				} else if (dueDate.getCellType() == CellType.STRING) {
					try {
						Date date = new SimpleDateFormat(StringIteration.DDMMYY)
								.parse(dueDate.getStringCellValue().trim());
						dto.setDueDate(date);
						dto.setInvalidDueDate(dueDate.getStringCellValue());
	
					} catch (ParseException e) {
						e.printStackTrace();
						o.setFlag(false);
						dto.setInvalidDueDate(dueDate.getStringCellValue());							
						o.setMessage("Due Date should be in dd-mm-yyyy, ");
					}
				}
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invalid due date ,");
		}	
	}
	
	private void prepareLrNumber(OrderImport o , Row row , OrderDTO dto) {
		try {
			Cell lrNumber = row.getCell(3);
			if (lrNumber == null || lrNumber.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("LR number cannot be empty, ");
			} else {
				if (lrNumber.getCellType() == CellType.NUMERIC) {
					double numericValue = lrNumber.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.setLrNumber(String.valueOf(longValue));
				} else if (lrNumber.getCellType() == CellType.STRING) {
					dto.setLrNumber(lrNumber.getStringCellValue());
				}
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invalid Lr number ,");
		}	
	}
	
	private void prepareLrDate(OrderImport o , Row row , OrderDTO dto) {
		try {
			Cell lrReceived = row.getCell(4);
			if (lrReceived == null || lrReceived.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("LR received cannot be empty, ");
			} else {
				if (lrReceived.getCellType() == CellType.NUMERIC) {
					dto.setLrRecieveDate(lrReceived.getDateCellValue());
				} else if (lrReceived.getCellType() == CellType.STRING) {
					try {
						Date date = new SimpleDateFormat(StringIteration.DDMMYY)
								.parse(lrReceived.getStringCellValue().trim());
						dto.setLrRecieveDate(date);
						dto.setInvalidLrReceivedDate(lrReceived.getStringCellValue());		
	
					} catch (ParseException e) {
						e.printStackTrace();
						o.setFlag(false);
						dto.setInvalidLrReceivedDate(lrReceived.getStringCellValue());	
						o.setMessage("LR received should be in dd-mm-yyyy, ");
					}
				}
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invalid lr date ,");
		}	
	}
	
	private void prepareDeliveryDate(OrderImport o , Row row , OrderDTO dto) {
		try {
			Cell deliveryDate = row.getCell(5);
			if (deliveryDate == null || deliveryDate.getCellType() == CellType.BLANK) {
				o.setFlag(false);
				o.setMessage("Delivery Date cannot be empty, ");
			} else {
				if (deliveryDate.getCellType() == CellType.NUMERIC) {
					dto.setDeliveryDate(deliveryDate.getDateCellValue());
				} else if (deliveryDate.getCellType() == CellType.STRING) {
					try {
						Date date = new SimpleDateFormat(StringIteration.DDMMYY)
								.parse(deliveryDate.getStringCellValue().trim());
						dto.setDeliveryDate(date);
						dto.setInvalidDeliveryDate(deliveryDate.getStringCellValue());						
						
					} catch (ParseException e) {
						e.printStackTrace();
						o.setFlag(false);
						dto.setInvalidDeliveryDate(deliveryDate.getStringCellValue());	
						o.setMessage("Delivery Date should be in dd-mm-yyyy, ");
					}
				}
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invalid delivery date ,");
		}	
	}
	
	private void validation(OrderImport o , OrderDTO dto , User u) {
		try {
			if(dto.getInvoice() != null) {
				Optional<TicketOrderInvoice> invoice = invoiceRepository
						.findByInvoiceNumberAndTenantId(dto.getInvoice().toLowerCase().replace(" ", ""), u.getTenantId());
				if (invoice.isPresent()) {
					TicketOrderInvoice i = invoice.get();
					if ((i.getStatus().equals(StringIteration.DISPATCHED))|| (i.getStatus().equals(StringIteration.DELIVERED)
									&& !i.getAddress().equals( StringIteration.MANUALLY_DELIVERED) )) {
						dto.setPriority(i.getPriority());
						dto.setTicketId(i.getTicketId());
					} else if (i.getStatus().equals(StringIteration.DELIVERED)
							&& i.getAddress().equals(StringIteration.MANUALLY_DELIVERED)) {
						o.setFlag(false);
						o.setMessage("This order is already delivered, ");
					} else {
						o.setFlag(false);
						o.setMessage("This order is not able to delivered, ");
					}
				} else {
					o.setFlag(false);
					o.setMessage("Invoice not found, ");
				}
			}	
		}catch(Exception e) {
			logger.error(e.getMessage());
			o.setFlag(false);
			o.setMessage("invoice not found");
		}	
	}
	
	private void saveExcel(List<OrderDTO> valid , Result<Object> result, User u) {
		if (!valid.isEmpty()) {
			for (OrderDTO dto : valid) {
				if ( dto.getPriority() != null && Boolean.TRUE.equals(dto.getPriority())) {
					updateOrderProrities(dto, u);
				} else if( dto.getPriority() != null && Boolean.FALSE.equals(dto.getPriority())) {
					updateOrderNonProrities(dto,u);		
				}
			}
			result.setCode(StringIteration.SUCCESS_CODE);

		}
	}
	
	private void updateOrderProrities(OrderDTO dto , User u) {
		Optional<TicketOrderInvoice> toi = invoiceRepository.findByInvoice(dto.getInvoice(), dto.getTicketId(),
				u.getTenantId());
		if (toi.isPresent()) {
			TicketOrderInvoice inv = toi.get();
			 if (inv.getStatus().equals(StringIteration.DISPATCHED)) {
					inv.setStatus(StringIteration.DELIVERED);
					inv.setLrNumber(dto.getLrNumber());
					inv.setLrRecieveDate(dto.getLrRecieveDate());
					inv.setLrDocument(dto.getLrDocument());
					if(dto.getAddress() != null) {
						inv.setAddress(dto.getAddress());
					} else {
						inv.setAddress("Manually Delivered");
					}
					
					if(dto.getDeliveryDate() != null) {
						inv.setDeliveryDate(dto.getDeliveryDate());
					} else {
						inv.setDeliveryDate(new Date());
					}
					inv.setDueDate(dto.getDueDate());
					inv.setInvoiceValue(dto.getInvoiceValue());
					TicketOrderInvoice in = invoiceRepository.save(inv);
					ticketService.auditStatus(in.getTicketId(), in.getStatus(), dto.getLatitude(), dto.getLongitude(),
							in.getInvoiceNumber(), in.getDeliveryDate());
					Optional<Ticket> tk = ticketRepository.findById(inv.getTicketId());
					if(tk.isPresent()) {
						Ticket t = tk.get();
						dto.setStockistId(t.getStockistId());
						Optional<TicketOrder> or = orderRepository.findById(inv.getTicketId());
						if(or.isPresent() && or.get().getCcdRequired().contains(StringIteration.TRUE)) {
							ticketService.sendMailStockist(u, dto, StringIteration.CCD_ORDER_DELIVIRY);
						}
					}
					
				} else if (inv.getStatus().equals(StringIteration.DELIVERED) && !inv.getAddress().equals(StringIteration.MANUALLY_DELIVERED)) {
					inv.setStatus(StringIteration.DELIVERED);
					inv.setLrNumber(dto.getLrNumber());
					inv.setLrRecieveDate(dto.getLrRecieveDate());						
					if(dto.getDeliveryDate() != null) {
						inv.setDeliveryDate(dto.getDeliveryDate());
					} else {
						inv.setDeliveryDate(new Date());
					}
					inv.setDueDate(dto.getDueDate());
					inv.setInvoiceValue(dto.getInvoiceValue());
					TicketOrderInvoice in = invoiceRepository.save(inv);
					Optional<TicketStatusHistory> his = ticketStatusHistoryRepository.finddeliverytimeline(in.getInvoiceNumber() , in.getTenantId());
					if(his.isPresent()) {
						TicketStatusHistory history = his.get();
						history.setHistoryOn(in.getDeliveryDate());
						ticketStatusHistoryRepository.save(history);
					}								
				} 
		}
	}
	
	private void updateOrderNonProrities(OrderDTO dto, User u) {
		Optional<TicketOrderInvoice> toi = invoiceRepository.findByInvoice(dto.getInvoice(), dto.getTicketId(),
				u.getTenantId());
		
		if(toi.isPresent()) {
			TicketOrderInvoice inv = toi.get();					
		
			if (inv.getStatus().equals("DISPATCHED")) {
				inv.setStatus(StringIteration.DELIVERED);
				inv.setLrNumber(dto.getLrNumber());
				inv.setLrRecieveDate(dto.getLrRecieveDate());
				inv.setLrDocument(dto.getLrDocument());
				inv.setInvoiceValue(dto.getInvoiceValue());
				if(dto.getAddress() != null) {
				    inv.setAddress(dto.getAddress());
				} else {
					inv.setAddress("Manually Delivered");
				}
				if(dto.getDeliveryDate() != null) {
					inv.setDeliveryDate(dto.getDeliveryDate());
				} else {
					inv.setDeliveryDate(new Date());
				}
				inv.setDueDate(dto.getDueDate());
				TicketOrderInvoice in = invoiceRepository.save(inv);
				
				
				
				Optional<Ticket> tk = ticketRepository.findById(inv.getTicketId());
				if(tk.isPresent()) {
					Ticket t = tk.get();
					dto.setStockistId(t.getStockistId());
					Optional<TicketOrder> or = orderRepository.findById(inv.getTicketId());
					Optional<User> us = userRepository.findById(tk.get().getStockistId());
					if(or.isPresent() && us.isPresent() && or.get().getCcdRequired().equals(StringIteration.TRUE)) {									
						ticketService.sendMailStockist(u, dto, StringIteration.CCD_ORDER_DELIVIRY);					
					}
				}
				ticketService.auditStatus(in.getTicketId(), in.getStatus(), dto.getLatitude(), dto.getLongitude(),in.getInvoiceNumber(),in.getDeliveryDate());
			
			} else if (inv.getStatus().equals(StringIteration.DELIVERED) && inv.getAddress() != StringIteration.MANUALLY_DELIVERED) {
				inv.setStatus(StringIteration.DELIVERED);
				inv.setLrNumber(dto.getLrNumber());
				inv.setLrRecieveDate(dto.getLrRecieveDate());						
				if(dto.getDeliveryDate() != null) {
					inv.setDeliveryDate(dto.getDeliveryDate());
				} else {
					inv.setDeliveryDate(new Date());
				}
				inv.setDueDate(dto.getDueDate());
				inv.setInvoiceValue(dto.getInvoiceValue());
				TicketOrderInvoice in = invoiceRepository.save(inv);

				Optional<TicketStatusHistory> his = ticketStatusHistoryRepository.finddeliverytimeline(in.getInvoiceNumber() , in.getTenantId());
				if(his.isPresent()) {
					TicketStatusHistory history = his.get();
					history.setHistoryOn(in.getDeliveryDate());
					ticketStatusHistoryRepository.save(history);
				}
				
			} 
		}
	}

	private void writeInvalidExcel(List<OrderDTO> invalid , Result<Object> result) {
		try (XSSFWorkbook workbook1 = new XSSFWorkbook()){
			if (!invalid.isEmpty()) {
				String[] headerList = { "Invoice Number", "Invoice Value", "Due Date", "LR number", "LR Received Date",
						"Delivery Date", "Remarks" };
				XSSFSheet spreadsheet = workbook1.createSheet(" invalid order Excel ");

				int rowId = 0;
				XSSFRow row1;
				row1 = spreadsheet.createRow(rowId++);
				int r = 0;
				for (String s : headerList) {
					row1.createCell(r).setCellValue(s);
					r++;
				}

				for (OrderDTO i : invalid) {
					row1 = spreadsheet.createRow(rowId++);
					row1.createCell(0).setCellValue(i.getInvoice());
					row1.createCell(1).setCellValue(i.getInvoiceValue());
					row1.createCell(2).setCellValue(i.getInvalidDueDate());
					row1.createCell(3).setCellValue(i.getLrNumber());
					row1.createCell(4).setCellValue(i.getInvalidLrReceivedDate());
					row1.createCell(5).setCellValue(i.getInvalidDeliveryDate());
					row1.createCell(6).setCellValue(i.getRemarks());
				}

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				workbook1.write(byteArrayOutputStream);
				InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

				String fileId = UUID.randomUUID().toString();

				com.healthtraze.etraze.api.file.model.File invalidCheques = fileStorageService.uploadFileToAWS(fileId,
						fileId, byteArrayInputStream, "invalidInvoice.xlsx", "document", 0);

				result.setCode(StringIteration.ERROR_CODE1);
				result.setData(invalidCheques);
				result.setMessage("invalid data found");

				byteArrayOutputStream.flush();
				byteArrayOutputStream.close();

			} else {
				result.setCode(StringIteration.SUCCESS_CODE);
			}

		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public Result<Map<String, Object>> storeInvoiceFileInAwsXlxs(MultipartFile file) {
		Result<Map<String, Object>> result = new Result<>();

		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {
			Map<String, Object> map = new HashMap<>();
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Row row = sheet.getRow(3);
			Cell invoiceCell = row.getCell(3);
			if (invoiceCell != null) {
				map.put("invoice", invoiceCell.toString());
				fileStorageService.storeInvoiceFileInAws(file);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setData(map);
			} else {
				result.setCode(StringIteration.ERROR_CODE2);
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public Result<Map<String, Object>> storeInvoiceFileInAwsXlxsTenant(MultipartFile file, String manfId) {
		Result<Map<String, Object>> result = new Result<>();

		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {
			Map<String, Object> map = new HashMap<>();
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				
				TempInvoice temp = new TempInvoice();
				temp.setId(CommonUtil.generateUniqueIdLong());
			    temp.setManufacturerId(manfId);
			    temp.setTenantId(u.getTenantId());
			    CommonUtil.setCreatedOn(temp);		    
				
				Row row = sheet.getRow(3);
				Cell invoiceCell = row.getCell(3);
				if (invoiceCell != null) {
					map.put("invoice", invoiceCell.toString());
					temp.setInvoiceNumber(invoiceCell.toString());			
						Result<FileResponse> s3 = fileStorageService.storeInvoiceFileInAws(file);
						if(s3 != null) {
							temp.setS3Url(s3.getData().getUrl());
							temp.setFilePath(s3.getData().getName());
						}else {
							result.setCode(StringIteration.ERROR_CODE1);
							result.setMessage("error uploading file to s3");
						}
					Optional<TempInvoice> optional = tempInvoiceRepository.findByInvoiceNumber(invoiceCell.toString().toLowerCase().replace(" ", ""),us.get().getTenantId());
					if(optional.isPresent()) {
						TempInvoice tempInvoice = optional.get();
							CommonUtil.setModifiedOn(tempInvoice);
							tempInvoice.setS3Url(temp.getS3Url());
							tempInvoice.setFilePath(temp.getFilePath());
							tempInvoice.setManufacturerId(temp.getManufacturerId());
							tempInvoiceRepository.save(tempInvoice);
					}else {
						tempInvoiceRepository.save(temp);
					}					
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setData(map);
				} else {
					result.setCode(StringIteration.ERROR_CODE2);
				}  
			}else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("invalid user");
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
		} 	
		
		return result;
	}

	
	public Result<Map<String, Object>> storeInvoiceFileInAwsCsv(MultipartFile file) {
		Result<Map<String, Object>> result = new Result<>();

		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
				BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));) {
			Map<String, Object> map = new HashMap<>();
			
			String line;
			int rowNumber = 0;

			while ((line = reader.readLine()) != null) {
				rowNumber++;
				String[] columns = line.split(",");
				if (rowNumber == 3) {
					if (columns.length > 3) {
						String invoiceNumber = columns[3].trim();
						map.put("invoice",invoiceNumber);
						fileStorageService.storeInvoiceFileInAws(file);
						result.setCode(StringIteration.SUCCESS_CODE);
						result.setData(map);
						logger.info("Invoice Number: {}", invoiceNumber);
						break;
					} else {
						result.setCode(StringIteration.ERROR_CODE2);
						logger.warn("Invoice number missing at row {}", rowNumber);
					}
				} 
			}
			

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}
	
	public Result<Map<String, Object>> storeInvoiceFileInAwsCsvTenant(MultipartFile file, String manfId) {
		Result<Map<String, Object>> result = new Result<>();

		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
				BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));) {
			Map<String, Object> map = new HashMap<>();
			
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				String line;
				int rowNumber = 0;

				TempInvoice temp = new TempInvoice();
				temp.setId(CommonUtil.generateUniqueIdLong());
			    temp.setManufacturerId(manfId);
			    temp.setTenantId(us.get().getTenantId());
			    CommonUtil.setCreatedOn(temp);
			    
			    while ((line = reader.readLine()) != null) {
					rowNumber++;
					String[] columns = line.split(",");
					if (rowNumber == 3) {
						if (columns.length > 3) {
							String invoiceNumber = columns[3].trim();
							map.put("invoice",invoiceNumber);						
							temp.setInvoiceNumber(invoiceNumber);						

								Result<FileResponse> s3 = fileStorageService.storeInvoiceFileInAws(file);
								if(s3 != null) {
									temp.setS3Url(s3.getData().getUrl());
									temp.setFilePath(s3.getData().getName());
								}else {
									result.setCode(StringIteration.ERROR_CODE1);
									result.setMessage("error uploading file to s3");
								}
							Optional<TempInvoice> optional = tempInvoiceRepository.findByInvoiceNumber(invoiceNumber.toLowerCase().replace(" ", ""),us.get().getTenantId());
							if(optional.isPresent()) {
								TempInvoice tempInvoice = optional.get();
								CommonUtil.setModifiedOn(tempInvoice);
								tempInvoice.setS3Url(temp.getS3Url());
								tempInvoice.setFilePath(temp.getFilePath());
								tempInvoice.setManufacturerId(temp.getManufacturerId());
								tempInvoiceRepository.save(tempInvoice);
							}else {
								tempInvoiceRepository.save(temp);
							}
							
							result.setCode(StringIteration.SUCCESS_CODE);
							result.setData(map);
							logger.info("Invoice Number: {}", invoiceNumber);
							break;
						} else {
							result.setCode(StringIteration.ERROR_CODE2);
							logger.warn("Invoice number missing at row {}", rowNumber);
						}
					} 
				}
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}
	

	
    public List<InvoiceDetails> findCartonDetails(String id){
    	List<InvoiceDetails> list = new ArrayList<>();
    	try {
    		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
    		User u = us.get();
    		List<TicketOrderInvoice> toi = invoiceRepository.findMergeInvoice(id, u.getTenantId());
    		if(!toi.isEmpty()) {
    			for(TicketOrderInvoice t: toi) {
    				InvoiceDetails inv = new InvoiceDetails();
    				inv.setInvoice(t.getInvoiceNumber());
    				inv.setCartons(t.getNumOfCases());
    				inv.setInvoiceDate(t.getCreatedOn());
    				inv.setInvoicevalue(t.getInvoiceValue());
    				inv.setManufacturer(invoiceRepository.findManfNameByTicketId(t.getTicketId() ,u.getTenantId()));
    				list.add(inv);
    			}
    		}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	return list;
    }
    
    public Result<List<InvoicedItemsDto>> getInvoicesByTenant(){
    	Result<List<InvoicedItemsDto>> result  = new Result<>();
    	try {
    		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
    		if(us.isPresent()) {
    			User u = us.get();
//    			List<TicketOrderInvoice> list = invoiceRepository.getPickedByTenant(u.getTenantId());
//    			for(TicketOrderInvoice t : list) {
//    				List<InvoiceLineItems> li = invoiceLineItemsRepository.findByInvoiceNumber(t.getInvoiceNumber());
//    				t.setItems(li);
//    			}
    			
    			List<TicketOrderInvoice> list = invoiceRepository.getPickedByTenant(u.getTenantId());
    			List<InvoicedItemsDto> invoicedItemsList = new ArrayList<>();

    			for (TicketOrderInvoice t : list) {
    			    List<InvoiceLineItems> li = invoiceLineItemsRepository.findByInvoiceNumber(t.getInvoiceNumber());

    			    InvoicedItemsDto invoicedItem = new InvoicedItemsDto();
    			    invoicedItem.setInvoiceNo(t.getInvoiceNumber());
    			    invoicedItem.setTicketId(t.getTicketId());
    			    invoicedItem.setInvoiceValue(t.getInvoiceValue());
    			    invoicedItem.setLineItems(t.getLineItem());
    			    invoicedItem.setStatus(t.getStatus());

    			    List<ItemsDto> itemsDtoList = li.stream().map(l -> {
    			        ItemsDto itemDto = new ItemsDto();    			        
    			        itemDto.setProductName(l.getProductName());
    			        itemDto.setQuantity(l.getQuantity());
    			        itemDto.setProductCode(l.getProductCode());
    			        itemDto.setBatchNumber(l.getBatchNumber());
    			        itemDto.setPickItems(l.getPickItems());
    			        return itemDto;
    			    }).collect(Collectors.toList());

    			    invoicedItem.setItems(itemsDtoList);

    			    invoicedItemsList.add(invoicedItem);
    			}

    			result.setCode(StringIteration.SUCCESS_CODE);
    			result.setData(invoicedItemsList);
    		}else {
    			result.setCode(StringIteration.ERROR_CODE1);
    			result.setMessage("invalid user");
    		}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);		
		}
    	
    	return result;
    }
    
//    public List<OrderDTO> getDispatchedByPackId(String id){
//    	List<OrderDTO> list = new ArrayList<>();
//    	
//    	try {
//			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
//			if(us.isPresent()) {
//				User u = us.get();
//				
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//    	
//    	return list;
//    }
//    
//    public Result<List<TicketReportsDTO>> createDispatchedByPackId(List<TicketReportsDTO> dto){    	
//    	Result<List<TicketReportsDTO>> result = new Result<>();    	
//    	try {
//			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
//			if(us.isPresent()) {
//				User u = us.get();
//				String mergeId = String.valueOf(System.currentTimeMillis());
//				for(TicketReportsDTO t : dto) {
//					Optional<TicketOrderInvoice> optional = invoiceRepository.findByInvoiceAndTenantId(t.getInvoiceNumber(), u.getTenantId());
//					if(optional.isPresent()) {
//						TicketOrderInvoice toi = optional.get();
//						toi.setDeliveryId(mergeId);
//						invoiceRepository.save(toi);
//					}
//				}
//				result.setCode(StringIteration.SUCCESS_CODE);
//				result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
//				result.setData(dto);
//				return result;
//			}
//		} catch (Exception e) {
//			result.setCode(StringIteration.ERROR_CODE1);
//			result.setMessage(e.getMessage());
//			logger.error(e.getMessage());
//		}
//    	
//    	return result;
//    }
//    
//    public Result<List<TicketReportsDTO>> updateDispatchedByPackId(String id ,List<TicketReportsDTO> dto){    	
//    	Result<List<TicketReportsDTO>> result = new Result<>();    	
//    	try {
//			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
//			if(us.isPresent()) {
//				User u = us.get();
//				for(TicketReportsDTO t : dto) {
//					Optional<TicketOrderInvoice> optional = invoiceRepository.findByInvoiceAndTenantId(t.getInvoiceNumber(), u.getTenantId());
//					if(optional.isPresent()) {
//						TicketOrderInvoice toi = optional.get();
//						toi.setDeliveryId(id);
//						invoiceRepository.save(toi);
//					}
//				}
//				result.setCode(StringIteration.SUCCESS_CODE);
//				result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
//				result.setData(dto);
//				return result;
//			}
//		} catch (Exception e) {
//			result.setCode(StringIteration.ERROR_CODE1);
//			result.setMessage(e.getMessage());
//			logger.error(e.getMessage());
//		}    	
//    	return result;
//    }
//    
//    public Result<TicketReportsDTO> removeDispatchedByPackId(TicketReportsDTO dto){    	
//    	Result<TicketReportsDTO> result = new Result<>();    	
//    	try {
//			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
//			if(us.isPresent()) {
//				User u = us.get();
//					Optional<TicketOrderInvoice> optional = invoiceRepository.findByInvoiceAndTenantId(dto.getInvoiceNumber(), u.getTenantId());
//					if(optional.isPresent()) {
//						TicketOrderInvoice toi = optional.get();
//						toi.setDeliveryId(null);
//						invoiceRepository.save(toi);
//					}
//				result.setCode(StringIteration.SUCCESS_CODE);
//				result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
//				result.setData(dto);
//				return result;
//			}
//		} catch (Exception e) {
//			result.setCode(StringIteration.ERROR_CODE1);
//			result.setMessage(e.getMessage());
//			logger.error(e.getMessage());
//		}    	
//    	return result;
//    }

}
