package com.healthtraze.etraze.api.masters.service;

import java.io.File;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.service.NotificationService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.base.util.ConfigUtil;
import com.healthtraze.etraze.api.masters.documents.City;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.OrderDTO;
import com.healthtraze.etraze.api.masters.dto.OrderSummary;
import com.healthtraze.etraze.api.masters.dto.TicketDetails;
import com.healthtraze.etraze.api.masters.dto.TicketInvoiceDTO;
import com.healthtraze.etraze.api.masters.dto.TicketOrderDTO;
import com.healthtraze.etraze.api.masters.dto.TicketReportsDTO;
import com.healthtraze.etraze.api.masters.dto.TicketStatus;
import com.healthtraze.etraze.api.masters.dto.TicketSummary;
import com.healthtraze.etraze.api.masters.model.Invoice;
import com.healthtraze.etraze.api.masters.model.InvoiceLineItems;
import com.healthtraze.etraze.api.masters.model.Items;
import com.healthtraze.etraze.api.masters.model.ManagerManufacturerMapping;
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
import com.healthtraze.etraze.api.masters.repository.ItemsRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManagerManufacturerMappingRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnAttachmentRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnNoteRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TempInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderRepository;
import com.healthtraze.etraze.api.masters.repository.TicketRepository;
import com.healthtraze.etraze.api.masters.repository.TicketStatusHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.security.model.EmailTemplate;
import com.healthtraze.etraze.api.security.model.NotificationTemplate;
import com.healthtraze.etraze.api.security.model.Role;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.EmailService;
import com.healthtraze.etraze.api.security.service.EmailTemplateService;
import com.healthtraze.etraze.api.security.service.NotificationTemplateService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

import io.netty.util.internal.StringUtil;

@Component
public class TicketService implements BaseService<Ticket, String> {

	private Logger logger = LogManager.getLogger(TicketService.class);
	
	@Value("${aws.access_key_id}")
	private String awsAccessKey;

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;
	
	private final TransportRepository transportRepository;

	private TicketRepository ticketRepository;

	private final UserRepository userRepository;

	private final TicketOrderRepository orderRepository;

	private final TicketOrderInvoiceRepository invoiceRepository;

	private final EmailTemplateService emailTemplateService;

	private final EmailService emailService;

	private final NotificationTemplateService notificationTemplateService;

	private final NotificationService notificationService;

	private final StockistRepository stockistRepository;

	private final TenantRepository tenantRepository;

	private final InvoiceLineItemsRepository invoiceLineItemsRepository;

	private final ItemsRepository itemsRepository;

	private final RoleRepository roleRepository;

	private final TicketOrderInvoiceRepository ticketOrderInvoiceRepository;

	private final ManagerManufacturerMappingRepository managerManufacturerMappingRepository;

	private final TicketStatusHistoryRepository ticketStatusHistoryRepository;

	private final ManufacturerRepository manufacturerRepository;

	private final CityRepository cityRepository;

	private final ListValueRepository listvalue;

	private final TenantManufactureRepository tenantManufactureRepository;

	private final RestTemplate restTemplate;
	
	private final TempInvoiceRepository tempInvoiceRepository;

	@Autowired
	public TicketService(TransportRepository transportRepository, TicketRepository ticketRepository,
			UserRepository userRepository, TicketOrderRepository orderRepository,
			TicketOrderInvoiceRepository invoiceRepository, EmailTemplateService emailTemplateService,
			EmailService emailService, NotificationTemplateService notificationTemplateService,
			NotificationService notificationService, ReturnNoteRepository returnNoteRepository,
			ReturnAttachmentRepository returnAttachmentRepository, StockistRepository stockistRepository,
			TenantRepository tenantRepository, InvoiceLineItemsRepository invoiceLineItemsRepository,
			ItemsRepository itemsRepository, RoleRepository roleRepository,
			TicketOrderInvoiceRepository ticketOrderInvoiceRepository,
			TicketStatusHistoryRepository ticketStatusHistoryRepository, ManufacturerRepository manufacturerRepository,
			ManagerManufacturerMappingRepository managerManufacturerMappingRepository, CityRepository cityRepository,
			ListValueRepository listvalue, TenantManufactureRepository tenantManufactureRepository,
			RestTemplate restTemplate,TempInvoiceRepository tempInvoiceRepository) {

		this.transportRepository = transportRepository;
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
		this.invoiceRepository = invoiceRepository;
		this.emailTemplateService = emailTemplateService;
		this.emailService = emailService;
		this.notificationTemplateService = notificationTemplateService;
		this.notificationService = notificationService;
		this.stockistRepository = stockistRepository;
		this.tenantRepository = tenantRepository;
		this.invoiceLineItemsRepository = invoiceLineItemsRepository;
		this.itemsRepository = itemsRepository;
		this.roleRepository = roleRepository;
		this.managerManufacturerMappingRepository = managerManufacturerMappingRepository;
		this.ticketStatusHistoryRepository = ticketStatusHistoryRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.ticketOrderInvoiceRepository = ticketOrderInvoiceRepository;
		this.cityRepository = cityRepository;
		this.listvalue = listvalue;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.restTemplate = restTemplate;
		this.tempInvoiceRepository = tempInvoiceRepository;
	}

	@Override
	public List<Ticket> findAll() {
		try {
			return ticketRepository.findAll();

		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	public List<String> findAllTicketId() {
		try {
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();

				if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					return ticketRepository.findAllTicketId(us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.MANAGER)) {
					return ticketRepository.getTicketIdByManager(us.getUserId(), us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					return ticketRepository.getTicketIdByManager(us.getHierarachyId(), us.getTenantId());
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	public List<Map<String, Object>> findAllOrderId() {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();
				List<Object[]> ob = new ArrayList<>();
				if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					ob = ticketRepository.findAllOrderId(us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.MANAGER)) {
					ob = ticketRepository.getOrderIdByManager(us.getUserId(), us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					ob = ticketRepository.getOrderIdByManager(us.getHierarachyId(), us.getTenantId());
				} else if (us.getRoleName().equals(StringIteration.STOCKIST)) {
					ob = ticketRepository.getOrderIdByStockist(us.getUserId(), us.getTenantId());
				}

				for (Object[] b : ob) {
					Map<String, Object> map = new HashMap<>();
					map.put("ticketId", b[0]);
					map.put("invoiceNumber", b[1]);
					list.add(map);
				}
				return list;
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	@Override
	public Ticket findById(String id) {
		try {
			Optional<Ticket> optional = ticketRepository.findById(id);
			if (optional.isPresent()) {
				Ticket ticket = optional.get();

				if (!StringUtils.isNullOrEmpty(ticket.getStockistId())) {
					Optional<Stockist> st = stockistRepository.findById(ticket.getStockistId());
					if (st.isPresent()) {
						ticket.setStockistName(st.get().getStockistName());
						ticket.setLocation(st.get().getCityId());
					}
				}
				if (ticket.getAssignedTo() != null) {
					Optional<User> us = userRepository.findByUserId(ticket.getAssignedTo());
					if (us.isPresent()) {
						ticket.setUserName(us.get().getFirstName());
					}
				}
				return ticket;

			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public Boolean getManagerManufacturerOrder(User user, Ticket ticket) {
		try {

			if (user.getRoleName().equals(StringIteration.MANAGER)) {
				Optional<ManagerManufacturerMapping> mm = managerManufacturerMappingRepository
						.findManufacturBYUserId(user.getUserId(), ticket.getManufacturerId(), user.getTenantId());
				return mm.isPresent();

			} else if (user.getRoleName().equals(StringIteration.USER)) {
				Optional<ManagerManufacturerMapping> mm = managerManufacturerMappingRepository
						.findManufacturBYUserId(user.getHierarachyId(), ticket.getManufacturerId(), user.getTenantId());
				return mm.isPresent();
			} else if (user.getRoleName().equals(StringIteration.TRANSPORT)) {
				return Boolean.TRUE;
			} else if (user.getRoleName().equals(StringIteration.STOCKIST)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return Boolean.FALSE;
	}

	public Result<TicketDetails> findTicketById(String id) {
		Result<TicketDetails> result = new Result<>();
		TicketDetails details = new TicketDetails();

		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			if (us.isPresent()) {
				User user = us.get();
				Optional<Ticket> optional = ticketRepository.getTicketsByTenant(id, user.getTenantId());
				if (optional.isPresent()) {
					Ticket ticket = optional.get();
					if (Boolean.FALSE.equals(getManagerManufacturerOrder(user, ticket))) {
						result.setCode(StringIteration.ERROR_CODE3);
						result.setMessage(StringIteration.INVALIDUSER);
						return result;
					}

					List<TicketStatusHistory> list = ticketStatusHistoryRepository.findAllByTicketId(id);
					Date date = null;
					Date date1 = null;
					Date date2 = null;
					Date date3 = null;

					int count = durationcount(list, date, date1, date2, date3);

					ticket = findTicketByIdT(ticket);

					TicketOrderDTO ticketOrder = new TicketOrderDTO();
					ticketOrder = findTicketByIdTicketOrder(user, ticketOrder, id);

					ticket.setDurationDate(String.valueOf(count));

					Optional<TenantManufacture> tm = tenantManufactureRepository
							.findByManufactureId(ticket.getManufacturerId(), user.getTenantId());
					if (tm.isPresent()) {
						details.setWms(tm.get().isWmsed());
					}

					details.setOrderDTO(ticketOrder);
					details.setHistory(list);
					details.setTicket(ticket);
					result.setData(details);
					result.setCode(StringIteration.SUCCESS_CODE);
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage(StringIteration.INVALIDUSER);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return result;

	}

	private int durationcount(List<TicketStatusHistory> list, Date date, Date date1, Date date2, Date date3) {
		int count = 0;
		for (TicketStatusHistory ticketStatusHistory : list) {
			if (ticketStatusHistory.getStatus().equalsIgnoreCase(StringIteration.CREATED)) {

				date = ticketStatusHistory.getCreatedOn();

			}
			if (ticketStatusHistory.getStatus().equalsIgnoreCase(StringIteration.ASSIGNED)) {

				date1 = ticketStatusHistory.getCreatedOn();
			}
			if (ticketStatusHistory.getStatus().equalsIgnoreCase(StringIteration.COMPLETED)) {

				date2 = ticketStatusHistory.getCreatedOn();
			}
			if (ticketStatusHistory.getStatus().equalsIgnoreCase(StringIteration.INPROCESS)) {

				date3 = ticketStatusHistory.getCreatedOn();
			}

		}

		if (date != null && date1 == null && date2 == null) {
			count = daysBetweenTwoDates(new Date(), date);

		} else if (date1 != null && date2 == null && date3 == null) {
			count = daysBetweenTwoDates(date1, date);
		} else if (date2 != null && date1 == null && date3 == null) {
			count = daysBetweenTwoDates(date2, date);
		} else if (date3 != null && date2 == null) {
			count = daysBetweenTwoDates(date3, date);
		} else if (date3 != null) {
			count = daysBetweenTwoDates(date3, date);
		} else {
			count = 0;
		}

		return count;
	}

	private Ticket findTicketByIdT(Ticket ticket) {
		if (ticket.getManufacturerId() != null) {
			setManufacturerName(ticket);
		}
		if (ticket.getAssignedTo() != null) {
			setUserName(ticket);
		}
		if (!StringUtils.isNullOrEmpty(ticket.getStockistId())) {
			setStockistDetails(ticket);
		}
		return ticket;
	}

	private String getLastSequenceByManufacturer(String tenantId, String manufacturerId) {

		String lastNum = ticketRepository.getLastSequenceByManufacturer(manufacturerId);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime now = ZonedDateTime.now(zoneId);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(StringIteration.dateTimeFormatter, Locale.ENGLISH);
		String strToday = dtf.format(now);
		String manufacturerName = manufacturerRepository.findManufacturName(manufacturerId);

		if (lastNum != null) {
			return manufacturerName + "TKT" + strToday + new BigInteger(lastNum).add(BigInteger.ONE);
		} else {
			return manufacturerName + "TKT" + strToday + "1";
		}
	}

	private void setManufacturerName(Ticket ticket) {
		Optional<Manufacturer> mn = manufacturerRepository.findById(ticket.getManufacturerId());
		mn.ifPresent(manufacturer -> ticket.setManufacturer(manufacturer.getManufacturerName()));

	}

	private void setUserName(Ticket ticket) {
		Optional<User> u = userRepository.findById(ticket.getAssignedTo());
		u.ifPresent(user -> ticket.setUserName(user.getFirstName() + user.getLastName()));
	}

	private void setStockistDetails(Ticket ticket) {
		Optional<Stockist> st = stockistRepository.findById(ticket.getStockistId());
		st.ifPresent(stockist -> {
			ticket.setStockistName(stockist.getStockistName());
			if (stockist.getCityId() != null && stockist.getStateId() != null) {
				Optional<City> ct = cityRepository.findByCityCodeAndStateCode(stockist.getCityId(),
						stockist.getStateId());
				ct.ifPresent(city -> ticket.setLocation(city.getCityName()));
			}
		});
	}

	private TicketOrderDTO findTicketByIdTicketOrder(User user, TicketOrderDTO ticketOrder, String id) {
		Optional<TicketOrder> tr = orderRepository.findByTicketId(id);
		if (tr.isPresent()) {
			TicketOrder trd = tr.get();
			if (trd.getTransporter() != null) {
				Optional<Transport> ts = transportRepository.findById(trd.getTransporter());
				if (ts.isPresent()) {
					trd.setTransportarName(ts.get().getTransportName());
				}
			}
			List<TicketOrderInvoice> invoices = invoiceRepository.findByTicketId(id, user.getTenantId());
			ticketOrder.setInvoices(invoices);
			ticketOrder.setTicketOrder(trd);

		}
		return ticketOrder;
	}

	private int daysBetweenTwoDates(Date startdate, Date enddate) {

		int days = 0;
		try {
			days = (int) ((startdate.getTime() - enddate.getTime()) / (1000 * 60 * 60 * 24));
		} catch (Exception ex) {
			logger.error(ex);
		}
		return days;

	}

	@Override
	public Result<Ticket> create(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			CommonUtil.setCreatedOn(t);
			t.setStatus(StringIteration.CREATED);
			String id = getLastSequenceByManufacturer(t.getTenantId(), t.getManufacturerId());
			t.setTicketId(id);
			t.setCreatedDate(new Date());
			ticketRepository.save(t);
			if (!(t.getStatus().equals(StringIteration.INPROCESS) || t.getStatus().equals(StringIteration.COMPLETED))) {
				auditStatusMail(t);
			}
			result.setCode("0000");
			result.setMessage(StringIteration.SUCCESSFULCREATED);
		} catch (Exception e) {
			result.setCode("1111");
			result.setMessage(e.getMessage());		
			logger.error(e);
		}
		return result;
	}
	
	void check(Ticket t){
		
	}

	public Result<Ticket> createManual(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				t.setTicketId(getLastSequenceByManufacturer(u.getTenantId(), t.getManufacturerId()));
				CommonUtil.setCreatedOn(t);
				t.setTenantId(u.getTenantId());
				t.setStatus(StringIteration.CREATED);
				t.setSentDate(CommonUtil.getCurrentDate());
				t.setCreatedDate(new Date());
				ticketRepository.save(t);
				if (!(t.getStatus().equals(StringIteration.INPROCESS)
						|| t.getStatus().equals(StringIteration.COMPLETED))) {
					auditStatus(t.getTicketId(), t.getStatus());
				}
				result.setCode("0000");
				result.setMessage(StringIteration.SUCESS);
			}

		} catch (Exception e) {
			result.setCode("1111");
//			logger.error("", e);
			e.printStackTrace();
		}
		return result;
	}

	public Result<OrderDTO> creatOrder(OrderDTO t) {
		Result<OrderDTO> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			Optional<Ticket> list = ticketRepository.findByTicketId(t.getTicketId());
			List<TempInvoice> tempList = new ArrayList<>();
			List<String> invoices = new ArrayList<>();
			if (us.isPresent() && list.isPresent()) {
				User u = us.get();
				for (TicketOrderInvoice tin : t.getInvoices()) {
					Optional<TicketOrderInvoice> in = invoiceRepository.findByInvoiceNumberAndTenantId(
							tin.getInvoiceNumber().toLowerCase().replace(" ", ""), u.getTenantId());
					if (in.isPresent()) {
						result.setCode(StringIteration.ERROR_CODE2);
						result.setMessage(tin.getInvoiceNumber() + " Invoice Alredy Exist");
						return result;
					}else {
						Optional<TempInvoice> temp = tempInvoiceRepository.findByInvoiceNumber(tin.getInvoiceNumber().toLowerCase().replace(" ", ""),u.getTenantId());
						if(temp.isPresent()) {
							tempList.add(temp.get());
						}
					}
				}
				moveFilesFromTempFolder(tempList,u);
				Ticket tk = list.get();
				tk.setStockistId(t.getStockistId());
				tk.setStatus(StringIteration.INVOICECREATED);
				ticketRepository.save(tk);

				TicketOrder tr = new TicketOrder();
				tr.setCreatedBy(SecurityUtil.getUserName());
				tr.setCreatedOn(new Date());
				tr.setTenantId(tk.getTenantId());
				tr.setCustomerRefNumber(t.getCustomerRefNumber());
				tr.setPodRequired(t.getPodRequired());
				tr.setCcdRequired(t.getCcdRequired());
				tr.setTicketId(t.getTicketId());
				tr.setCreated(true);
				orderRepository.save(tr);
				for (TicketOrderInvoice tin : t.getInvoices()) {
					TicketOrderInvoice toi = new TicketOrderInvoice();
					toi.setId(System.currentTimeMillis() + "");
					toi.setInvoiceNumber(tin.getInvoiceNumber());
					toi.setLineItem(tin.getLineItem());
					toi.setTicketId(t.getTicketId());
					toi.setStatus(StringIteration.INVOICECREATED);
					toi.setInvoiceValue(tin.getInvoiceValue());
					toi.setDueDate(tin.getDueDate());
					toi.setIsSelected(false);
					toi.setPriority(false);
					toi.setChequeStatus(false);

					toi.setTenantId(list.get().getTenantId());

					CommonUtil.setCreatedOn(toi);
					invoiceRepository.save(toi);
					invoices.add(toi.getInvoiceNumber());
					auditStatus(tk.getTicketId(), tk.getStatus(), tin.getInvoiceNumber());
					invoiceLineItems(tin.getInvoiceNumber());
				}
				
				t.setInvoice(invoices.stream().collect(Collectors.joining(",")));
				sendMailStockist(u, t, "INVOICE GENERATED");

				result.setCode("0000");
				result.setMessage(StringIteration.SUCESS);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}
	
	public void  moveFilesFromTempFolder(List<TempInvoice> inv,User u) {
		Optional<Tenant> tenant = tenantRepository.findById(u.getTenantId());
		if(tenant.isPresent()) {
			Tenant t = tenant.get();
			String tenantFolderPath = "SourceFolderForTenants/"+ t.getTenantCode()+ "_" + t.getTenantName()+ "/";
			logger.info("bucket name {} ",ConfigUtil.getS3Bucket());
			String bucketName = ConfigUtil.getS3Bucket();
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
			Regions region = Regions.AP_SOUTH_1;
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
					.withRegion(region).build();
	
			if (s3Client.doesObjectExist(bucketName, tenantFolderPath)) {
				logger.info("Tenant folder already exists:{}", tenantFolderPath);
			} else {
				s3Client.putObject(bucketName, tenantFolderPath, "");
				logger.info("Tenant folder created: {}", tenantFolderPath);
			}
	
//			ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName)
//					.withPrefix("TemporaryInvoiceForTenants/" + t.getTenantCode()+ "_" + t.getTenantName() + "/");
//			ListObjectsV2Result result = s3Client.listObjectsV2(request);
//			List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
			for (TempInvoice objectSummary : inv) {
				String sourceKey = objectSummary.getFilePath();
				String destinationKey = tenantFolderPath + sourceKey.substring(sourceKey.lastIndexOf("/") + 1);
				moveFile(sourceKey, destinationKey, s3Client, bucketName);
			}
		}
	}
	
	private void moveFile(String sourceKey, String destinationKey, AmazonS3 s3Client, String bucketName) {
		s3Client.copyObject(bucketName, sourceKey, bucketName, destinationKey);
		s3Client.deleteObject(bucketName, sourceKey);
		logger.info("File moved from{} to {}",bucketName+sourceKey, destinationKey);
		System.err.println("File moved from{} to {}"+" "+ bucketName +"  "+ sourceKey+"   "+ destinationKey);
	}
	
	public Result<OrderDTO> addingInvoice(OrderDTO t) {
		Result<OrderDTO> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			Optional<Ticket> list = ticketRepository.findByTicketId(t.getTicketId());
			List<TempInvoice> tempList = new ArrayList<>();
			List<String> invoices = new ArrayList<>();
			
			if (us.isPresent() && list.isPresent()) {
				User u = us.get();
				if (t.getInvoices() == null) {
					result.setCode(StringIteration.ERROR_CODE2);
					result.setMessage("invoices are empty");
					return result;
				}
				for (TicketOrderInvoice tin : t.getInvoices()) {
					Optional<TicketOrderInvoice> in = invoiceRepository.findByInvoiceNumberAndTenantId(
							tin.getInvoiceNumber().toLowerCase().replace(" ", ""), u.getTenantId());
					if (in.isPresent()) {
						result.setCode(StringIteration.ERROR_CODE2);
						result.setMessage(tin.getInvoiceNumber() + " Invoice Alredy Exist");
						return result;
					}else {
						Optional<TempInvoice> temp = tempInvoiceRepository.findByInvoiceNumber(tin.getInvoiceNumber().toLowerCase().replace(" ", ""),u.getTenantId());
						if(temp.isPresent()) {
							tempList.add(temp.get());
						}
					}
				}
				
				moveFilesFromTempFolder(tempList,u);
				
				Ticket tk = list.get();

				for (TicketOrderInvoice tin : t.getInvoices()) {
					TicketOrderInvoice toi = new TicketOrderInvoice();
					toi.setId(System.currentTimeMillis() + "");
					toi.setInvoiceNumber(tin.getInvoiceNumber());
					toi.setLineItem(tin.getLineItem());
					toi.setTicketId(t.getTicketId());
					toi.setStatus(StringIteration.INVOICECREATED);
					toi.setInvoiceValue(tin.getInvoiceValue());
					toi.setDueDate(tin.getDueDate());
					toi.setIsSelected(false);
					toi.setPriority(false);
					toi.setChequeStatus(false);

					toi.setTenantId(list.get().getTenantId());

					CommonUtil.setCreatedOn(toi);
					invoiceRepository.save(toi);
					invoices.add(toi.getInvoiceNumber());
					auditStatus(tk.getTicketId(), tk.getStatus(), tin.getInvoiceNumber());
					invoiceLineItems(tin.getInvoiceNumber());
				}
				t.setInvoice(invoices.stream().collect(Collectors.joining(",")));
				sendMailStockist(u, t, "INVOICE GENERATED");

				result.setCode("0000");
				result.setMessage(StringIteration.SUCESS);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}
	
	public Result<Object> uploadTenant(String inv) {
		Result<Object> result = new Result();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
				User u = us.get();
				List<TempInvoice> tempList = new ArrayList();
				Optional<TempInvoice> temp = tempInvoiceRepository.findByInvoiceNumber(inv.toLowerCase().replace(" ", ""),u.getTenantId());
				if(temp.isPresent()) {
					tempList.add(temp.get());
					moveFilesFromTempFolder(tempList,u);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.SAVEDSUCCESSFULLY);
					return result;
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
			return result;
		}
		
		return result;
	}

	@Override
	public Result<Ticket> update(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> list = ticketRepository.findById(t.getTicketId());
			if (!list.isPresent()) {
				CommonUtil.setModifiedOn(t);
				ticketRepository.save(t);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCESS);
				result.setData(t);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> updateStatus(String ticketId, TicketStatus t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(ticketId);
			if (option.isPresent()) {
				Ticket ticket = option.get();
				ticket.setStatus(t.getStatus());
				Ticket updatedticket = ticketRepository.save(ticket);
				if (!(t.getStatus().equals(StringIteration.INPROCESS)
						|| t.getStatus().equals(StringIteration.COMPLETED))) {
					auditStatus(ticketId, t.getStatus());
				}
				result.setCode("0000");
				result.setMessage("Successful updated");
				result.setData(updatedticket);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	@Override
	public Result<Ticket> delete(String id) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> optional = ticketRepository.findById(id);
			if (optional.isPresent()) {
				ticketRepository.deleteById(optional.get().getTicketId());
				result.setCode("0000");
				result.setMessage("Successful deleted");
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	private void auditStatus(String ticketId, String status) {
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
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
	}

	private void auditStatusMail(Ticket t) {
		TicketStatusHistory history = new TicketStatusHistory();
		history.setCreatedBy("anonymousUser");
		history.setTenantId(t.getTenantId());
		history.setCreatedBy(SecurityUtil.getUserName());
		history.setHistoryOn(t.getSentDate());
		history.setCreatedOn(t.getSentDate());
		history.setStatus(t.getStatus());
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(t.getTicketId());
		ticketStatusHistoryRepository.save(history);
	}

	private void auditStatuss(String ticketId, String status, String remarks) {
		TicketStatusHistory history = new TicketStatusHistory();

		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (u.isPresent()) {
			history.setCreatedBy(u.get().getFirstName());
			history.setTenantId(u.get().getTenantId());
		} else {
			history.setCreatedBy(SecurityUtil.getUserName());
		}
		history.setRemarks(remarks);
		history.setHistoryOn(new Date());
		history.setCreatedOn(new Date());
		history.setStatus(status);
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
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

	public void invoiceLineItems(String invoiceNumber) {
		List<Items> ilt = itemsRepository.findByInvoiceNumber(invoiceNumber);
		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (!ilt.isEmpty() && u.isPresent()) {
			User us = u.get();
			for (Items i : ilt) {
				InvoiceLineItems il = new InvoiceLineItems();
				il.setId(i.getId());
				il.setInvoiceId(i.getInvoiceId());
				il.setProductCode(i.getProductCode());
				il.setProductName(i.getProductName());
				il.setQuantity(i.getQuantity());
				il.setBatchNumber(i.getBatchNumber());
				il.setExpiryDate(i.getExpiryDate());
				il.setMrp(i.getMrp());
				il.setPickItems("");
				il.setStatus("");
				il.setChecked(false);
				il.setPicked(false);
				il.setTenantId(u.get().getTenantId());
				Optional<InvoiceLineItems> optional = invoiceLineItemsRepository.findByInvoiceAndCode(i.getInvoiceId(), i.getProductCode(), us.getTenantId());
				if(optional.isPresent()) {
					InvoiceLineItems ili = optional.get();
					int existQuantity = Integer.parseInt(ili.getQuantity());
					int newQuantity = Integer.parseInt(i.getQuantity());
					ili.setQuantity(String.valueOf(existQuantity + newQuantity));
					invoiceLineItemsRepository.save(ili);
				}else {
					invoiceLineItemsRepository.save(il);
				}

			}
		}
	}

	public void invoiceLineItemEntry(Invoice inv) {
		List<TicketOrderInvoice> ilt = ticketOrderInvoiceRepository.findByInvoiceNumber(inv.getInvoiceNumber(),
				inv.getTenantId());
		if (!ilt.isEmpty()) {
			List<Items> it = itemsRepository.findByInvoiceNumber(inv.getInvoiceNumber());
			for (Items i : it) {
				InvoiceLineItems il = new InvoiceLineItems();
				il.setId(i.getId());
				il.setInvoiceId(i.getInvoiceId());
				il.setProductCode(i.getProductCode());
				il.setProductName(i.getProductName());
				il.setQuantity(i.getQuantity());
				il.setBatchNumber(i.getBatchNumber());
				il.setExpiryDate(i.getExpiryDate());
				il.setMrp(i.getMrp());
				il.setPickItems("");
				il.setStatus("");
				il.setChecked(false);
				il.setPicked(false);
				il.setTenantId(inv.getTenantId());
				Optional<InvoiceLineItems> optional = invoiceLineItemsRepository.findByInvoiceAndCode(i.getInvoiceId(), i.getProductCode(), inv.getTenantId());
				if(optional.isPresent()) {
					InvoiceLineItems ili = optional.get();
					int existQuantity = Integer.parseInt(ili.getQuantity());
					int newQuantity = Integer.parseInt(i.getQuantity());
					ili.setQuantity(String.valueOf(existQuantity + newQuantity));
					invoiceLineItemsRepository.save(ili);
				}else {
					invoiceLineItemsRepository.save(il);
				}
				
			}
		}

	}

	public void invoiceLineItemEntry(String invoiceNumber) {
		Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
		if (us.isPresent()) {
			User u = us.get();
			List<TicketOrderInvoice> ilt = ticketOrderInvoiceRepository.findByInvoiceNumber(invoiceNumber,
					u.getTenantId());
			if (!ilt.isEmpty()) {
				List<Items> it = itemsRepository.findByInvoiceNumber(invoiceNumber);
				for (Items i : it) {
					InvoiceLineItems il = new InvoiceLineItems();
					il.setId(i.getId());
					il.setInvoiceId(i.getInvoiceId());
					il.setProductCode(i.getProductCode());
					il.setProductName(i.getProductName());
					il.setQuantity(i.getQuantity());
					il.setBatchNumber(i.getBatchNumber());
					il.setExpiryDate(i.getExpiryDate());
					il.setMrp(i.getMrp());
					il.setPickItems("");
					il.setStatus("");
					il.setChecked(false);
					il.setPicked(false);

					il.setTenantId(u.getTenantId());

					invoiceLineItemsRepository.save(il);
				}
			}
		}
	}

	private void auditStatus(String ticketId, String status, String latitude, String longitude) {
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

		history.setLatitude(latitude);
		history.setLongitude(longitude);
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
	}

	private void auditStatusDispatch(String ticketId, String status, String invoiceNumber) {
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

		history.setInvoice(invoiceNumber);
		history.setPriority("true");
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
	}

	public void auditStatus(String ticketId, String status, String latitude, String longitude, String invoiceNumber,
			Date deliveryDate) {
		TicketStatusHistory history = new TicketStatusHistory();

		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (u.isPresent()) {
			history.setCreatedBy(u.get().getFirstName() + " " + u.get().getLastName());
			history.setTenantId(u.get().getTenantId());
		} else {
			history.setCreatedBy(SecurityUtil.getUserName());
		}

		if (deliveryDate != null) {
			history.setHistoryOn(deliveryDate);
		} else {
			history.setHistoryOn(new Date());
		}

		history.setCreatedOn(new Date());
		history.setStatus(status);
		history.setLatitude(latitude);
		history.setLongitude(longitude);
		history.setInvoice(invoiceNumber);
		history.setPriority("true");
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		ticketStatusHistoryRepository.save(history);
	}

	public List<TicketStatusHistory> auditRemarks(String ticketId, String status, String remarks) {
		TicketStatusHistory history = new TicketStatusHistory();
		Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
		if (u.isPresent()) {
			history.setCreatedBy(u.get().getFirstName() + " " + u.get().getLastName());
			history.setTenantId(u.get().getTenantId());
		} else {
			history.setCreatedBy(SecurityUtil.getUserName());
		}
		history.setCreatedOn(new Date());
		history.setHistoryOn(new Date());
		history.setStatus(status);
		history.setId(System.currentTimeMillis() + "");
		history.setTicketId(ticketId);
		history.setRemarks(remarks);
		ticketStatusHistoryRepository.save(history);
		return Collections.emptyList();
	}

	public Result<Ticket> updateTicket(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
			if (option.isPresent()) {
				Ticket returnObject = option.get();
				returnObject.setStatus(t.getStatus());
				if (t.getStockistId() != null) {
					returnObject.setStockistId(t.getStockistId());
				}
				if (t.getType() != null) {
					returnObject.setType(t.getType());
				}
				CommonUtil.setModifiedOn(returnObject);
				ticketRepository.save(returnObject);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);
				result.setData(t);
				if (!(returnObject.getStatus().equals(StringIteration.INPROCESS)
						|| returnObject.getStatus().equals(StringIteration.COMPLETED))) {
					auditStatus(returnObject.getTicketId(), returnObject.getStatus());
				}

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	public Result<Ticket> updateTicketStatus(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
			if (option.isPresent() && !option.get().getStatus().equals(t.getStatus())) {
				Ticket returnObject = option.get();
				returnObject.setStatus(t.getStatus());
				if (t.getStockistId() != null) {
					returnObject.setStockistId(t.getStockistId());
				}
				CommonUtil.setModifiedOn(returnObject);
				ticketRepository.save(returnObject);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);
				result.setData(t);

				auditStatuss(returnObject.getTicketId(), returnObject.getStatus(), t.getRemarks());

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	public Result<Ticket> updateTicketMultiple(List<Ticket> t) {
		Result<Ticket> result = new Result<>();
		try {
			if (!t.isEmpty()) {
				for (Ticket ticket : t) {
					Optional<Ticket> option = ticketRepository.findById(ticket.getTicketId());
					if (option.isPresent()) {
						Ticket returnObject = option.get();
						returnObject.setStatus(StringIteration.COMPLETED1);
						returnObject.setType("NOA");
						CommonUtil.setModifiedOn(returnObject);
						ticketRepository.save(returnObject);
						auditStatus(returnObject.getTicketId(), returnObject.getStatus());

					}
				}
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	public Result<Ticket> updateOrderMultiple(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			String ticketId = t.getTicketId();
			if (ticketId.contains(",")) {
				String[] ticketIds = ticketId.split(",");
				for (String tid : ticketIds) {
					t.setTicketId(tid);
					updateOrder(t);
				}
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESSFULLYCREATED);
			} else {
				return updateOrder(t);
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(StringIteration.SUCCESSFULLYCREATED);
			logger.error(e.getMessage());
		}
		return result;
	}

	public Result<Ticket> updateOrder(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				updateOrd(u, t, result);

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	private Result<Ticket> updateOrd(User u, OrderDTO t, Result<Ticket> result) {

		Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
		if (option.isPresent()) {
			Ticket rt = option.get();
			if (rt.getStatus().equals(StringIteration.INVOICECREATED)) {

				List<TicketOrderInvoice> in = invoiceRepository.findByTicketId(rt.getTicketId(), u.getTenantId());
				for (TicketOrderInvoice tn : in) {

					if (!tn.getStatus().equals(StringIteration.PACKED) && !Boolean.TRUE.equals(tn.getPriority())) {
						result.setCode("1111");
						return result;
					}

				}

				TicketOrder trd = orderRepository.getByTicketId(rt.getTicketId());
				trd.setTransporter(t.getTransporter());
				trd.setVehicaleNo(t.getVehicaleNo());
				orderRepository.save(trd);

				rt.setStatus(t.getStatus());
				ticketRepository.save(rt);
			}

			else if (rt.getStatus().equals(StringIteration.DISPATCHED)) {
				TicketOrder trd = orderRepository.getByTicketId(rt.getTicketId());
				trd.setLrNumber(t.getLrNumber());
				trd.setLrRecieveDate(t.getLrRecieveDate());
				trd.setLrDocument(t.getLrDocument());
				orderRepository.save(trd);

				rt.setStatus(StringIteration.DELIVERED);
				ticketRepository.save(rt);
			}
			result.setCode("0000");
			result.setMessage(StringIteration.SUCCESSFULLYCREATED);
			result.setData(rt);
			auditStatus(rt.getTicketId(), rt.getStatus(), t.getLatitude(), t.getLongitude());

		}

		return result;
	}

	public Result<Ticket> updateOrderProrities(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				Optional<TicketOrderInvoice> toi = invoiceRepository.findByInvoice(t.getInvoice(), t.getTicketId(),
						u.getTenantId());

				if (toi.isPresent()) {
					TicketOrderInvoice inv = toi.get();
					if (inv.getStatus().equals(StringIteration.PACKED)
							|| inv.getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
						inv.setStatus(StringIteration.DISPATCHED);
						inv.setTransporter(t.getTransporter());
						inv.setVehicaleNo(t.getVehicaleNo());
						inv.setAssignTransport(new Date());
						TicketOrderInvoice in = invoiceRepository.save(inv);
						auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
					} else if (inv.getStatus().equals(StringIteration.DISPATCHED)) {
						inv.setStatus(StringIteration.DELIVERED);
						inv.setLrNumber(t.getLrNumber());
						inv.setLrRecieveDate(t.getLrRecieveDate());
						inv.setLrDocument(t.getLrDocument());
						if (t.getAddress() != null) {
							inv.setAddress(t.getAddress());
						} else {
							inv.setAddress("Manually Delivered");
						}

						if (t.getDeliveryDate() != null) {
							inv.setDeliveryDate(t.getDeliveryDate());
						} else {
							inv.setDeliveryDate(new Date());
						}
						inv.setDueDate(t.getDueDate());
						inv.setInvoiceValue(t.getInvoiceValue());
						TicketOrderInvoice in = invoiceRepository.save(inv);

						auditStatus(in.getTicketId(), in.getStatus(), t.getLatitude(), t.getLongitude(),
								in.getInvoiceNumber(), in.getDeliveryDate());
						Optional<Ticket> tk = ticketRepository.findById(inv.getTicketId());
						if(tk.isPresent()) {
							Ticket ticket = tk.get();
							t.setStockistId(ticket.getStockistId());
							t.setDeliveryDate(inv.getDeliveryDate());
							Optional<TicketOrder> or = orderRepository.findById(inv.getTicketId());
							if(or.isPresent() && or.get().getCcdRequired().contains(StringIteration.TRUE)) {
								sendMailStockist(u, t, StringIteration.CCD_ORDER_DELIVIRY);
							}
						}
						
					} else if (inv.getStatus().equals(StringIteration.DELIVERED)
							&& inv.getAddress() != StringIteration.MANUALLY_DELIVERED) {
						inv.setStatus(StringIteration.DELIVERED);
						inv.setLrNumber(t.getLrNumber());
						inv.setLrRecieveDate(t.getLrRecieveDate());
						if (t.getDeliveryDate() != null) {
							inv.setDeliveryDate(t.getDeliveryDate());
						} else {
							inv.setDeliveryDate(new Date());
						}
						inv.setDueDate(t.getDueDate());
						inv.setInvoiceValue(t.getInvoiceValue());
						TicketOrderInvoice in = invoiceRepository.save(inv);

						Optional<TicketStatusHistory> his = ticketStatusHistoryRepository
								.finddeliverytimeline(in.getInvoiceNumber(), in.getTenantId());
						if (his.isPresent()) {
							TicketStatusHistory history = his.get();
							history.setHistoryOn(in.getDeliveryDate());
							ticketStatusHistoryRepository.save(history);
						}

					} else if (inv.getStatus().equals(StringIteration.DELIVERED)) {
						inv.setStatus(StringIteration.PODRECEIVED);
						TicketOrderInvoice in = invoiceRepository.save(inv);
						TicketStatusHistory his = ticketStatusHistoryRepository.getById(t.getHistoryId());
						his.setPriority("false");
						ticketStatusHistoryRepository.save(his);
						auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
					}

					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESS);

				}

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> updateOrderDispatch(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				List<String> invoices = new ArrayList<>();
				
				Optional<TicketOrderInvoice> invs = invoiceRepository.findByInvoiceNumbeForPicklist(t.getInvoice(),
						u.getTenantId());
				if (invs.isPresent() && invs.get().getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
					List<TicketOrderInvoice> ins = invoiceRepository
							.findByTransporterAndVehicaleNo(invs.get().getTransporter(), u.getTenantId());
					ins.forEach(inv -> {
						if (inv.getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
							inv.setStatus(StringIteration.DISPATCHED);
							inv.setTransporter(t.getTransporter());
							inv.setVehicaleNo(t.getVehicaleNo());
							inv.setAssignTransport(new Date());
							invoices.add(inv.getInvoiceNumber());
							TicketOrderInvoice in = invoiceRepository.save(inv);
							auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
							result.setCode(StringIteration.SUCCESS_CODE);
							result.setMessage(StringIteration.SUCCESSFULCREATED);
						} else {
							result.setCode(StringIteration.ERROR_CODE3);
						}
					});
					
					Optional<Ticket> optional = ticketRepository.findById(t.getTicketId());
					if(optional.isPresent()) {
						Ticket ticket = optional.get();
						t.setInvoice(invoices.stream().collect(Collectors.joining(", ")));
						t.setStockistId(ticket.getStockistId());
						sendMailStockist(u, t,"ORDER_DISPATCHED");
					}
				} else {
					result.setCode(StringIteration.ERROR_CODE3);
				}

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public void ccdMail(User user, String stkMail) {

		try {
			EmailTemplate emailTemplateStockist = emailTemplateService.findById(stkMail);
			List<String> name = new ArrayList<>();
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			if (us.isPresent()) {
				Optional<Tenant> tenat = tenantRepository.findById(SecurityUtil.getUserName());
				if (tenat.isPresent()) {
					name.add(tenat.get().getTenantName());

				}
				if (us.get().getRoleName().equals(StringIteration.USER)) {
					Optional<User> u = userRepository.findById(us.get().getHierarachyId());
					name.add(u.get().getFirstName() + " " + u.get().getLastName());

				} else if (us.get().getRoleName().equals(StringIteration.MANAGER)) {
					Optional<User> u = userRepository.findById(us.get().getUserId());
					name.add(u.get().getFirstName() + " " + u.get().getLastName());

				}
				String[] str = { user.getEmail() };
				if (emailTemplateStockist != null) {
					VelocityContext context = new VelocityContext();
					context.put(StringIteration.ORIGIN, ConfigUtil.getAppLink());
					context.put(StringIteration.LANGUAGE, "en");
					context.put("fullname", user.getFirstName());
					StringWriter writer = new StringWriter();
					String templateStr = emailTemplateStockist.getMailTemplate();
					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
					emailService.sendEmails(str, writer.toString(), emailTemplateStockist.getSubject(), name);

				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

	}

	public Result<Ticket> updateOrderNonProrities(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User u = user.get();
				Optional<TicketOrderInvoice> toi = invoiceRepository.findByInvoice(t.getInvoice(), t.getTicketId(),
						u.getTenantId());
				if (toi.isPresent()) {
					TicketOrderInvoice invoice = toi.get();
					List<TicketOrderInvoice> ins = invoiceRepository.findByPickId(invoice.getPackId(), u.getTenantId());

					ins.stream().forEach(inv -> {
						if (inv.getStatus().equals(StringIteration.PACKED)
								|| inv.getStatus().equals(StringIteration.TRANSPORTER_ASSIGNED)) {
							inv.setStatus(StringIteration.DISPATCHED);
							inv.setTransporter(t.getTransporter());
							inv.setVehicaleNo(t.getVehicaleNo());
							inv.setAssignTransport(new Date());
							TicketOrderInvoice in = invoiceRepository.save(inv);
							auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
						} else if (inv.getStatus().equals("DISPATCHED")) {
							inv.setStatus(StringIteration.DELIVERED);
							inv.setLrNumber(t.getLrNumber());
							inv.setLrRecieveDate(t.getLrRecieveDate());
							inv.setLrDocument(t.getLrDocument());
							if (t.getAddress() != null) {
								inv.setAddress(t.getAddress());
							} else {
								inv.setAddress("Manually Delivered");
							}
							if (t.getDeliveryDate() != null) {
								inv.setDeliveryDate(t.getDeliveryDate());
							} else {
								inv.setDeliveryDate(new Date());
							}
							inv.setDueDate(t.getDueDate());
							TicketOrderInvoice in = invoiceRepository.save(inv);

							Optional<Ticket> tk = ticketRepository.findById(inv.getTicketId());
							if (tk.isPresent()) {
								Optional<TicketOrder> or = orderRepository.findById(inv.getTicketId());
								Optional<User> us = userRepository.findById(tk.get().getStockistId());
								t.setStockistId(tk.get().getStockistId());
								t.setDeliveryDate(inv.getDeliveryDate());
								if (or.isPresent() && us.isPresent()
										&& or.get().getCcdRequired().equals(StringIteration.TRUE)) {
									sendMailStockist(u, t, StringIteration.CCD_ORDER_DELIVIRY);
								}
							}
							auditStatus(in.getTicketId(), in.getStatus(), t.getLatitude(), t.getLongitude(),
									in.getInvoiceNumber(), in.getDeliveryDate());
						}
//						 else if (inv.getStatus().equals(StringIteration.DELIVERED) && inv.getAddress() != StringIteration.MANUALLY_DELIVERED) {
//							inv.setStatus(StringIteration.DELIVERED);
//							inv.setLrNumber(t.getLrNumber());
//							inv.setLrRecieveDate(t.getLrRecieveDate());						
//							if(t.getDeliveryDate() != null) {
//								inv.setDeliveryDate(t.getDeliveryDate());
//							} else {
//								inv.setDeliveryDate(new Date());
//							}
//							inv.setDueDate(t.getDueDate());
//							inv.setInvoiceValue(t.getInvoiceValue());
//							TicketOrderInvoice in = invoiceRepository.save(inv);
//
//							Optional<TicketStatusHistory> his = ticketStatusHistoryRepository.finddeliverytimeline(in.getInvoiceNumber() , in.getTenantId());
//							if(his.isPresent()) {
//								TicketStatusHistory history = his.get();
//								history.setHistoryOn(in.getDeliveryDate());
//								ticketStatusHistoryRepository.save(history);}
//							}

						else if (inv.getStatus().equals("DELIVERED")) {
							inv.setStatus("POD RECEIVED");
							TicketOrderInvoice in = invoiceRepository.save(inv);
							auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
						}
					});
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESSFULCREATED);

				}

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> assignTransporter(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				t.getPackIds().forEach(pickId -> {
					List<TicketOrderInvoice> ins = invoiceRepository.findByPickId(pickId, u.getTenantId());
					ins.forEach(inv -> {
						if (inv.getStatus().equals("PACKED")) {
							inv.setStatus(StringIteration.TRANSPORTER_ASSIGNED);
							inv.setTransporter(t.getTransporter());
//							inv.setVehicaleNo(t.getVehicaleNo());
							inv.setAssignTransport(new Date());
							TicketOrderInvoice in = invoiceRepository.save(inv);
							auditStatusDispatch(in.getTicketId(), in.getStatus(), in.getInvoiceNumber());
						}
					});
				});
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESS_MESSAGE);
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> updateOrderInvoice(OrderDTO t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
			if (option.isPresent()) {
				Ticket rt = option.get();
				if (rt.getStatus().equals("INVOICE CREATED")) {
					TicketOrderInvoice tin = invoiceRepository.getByTicketId(rt.getTicketId());
					tin.setStatus("PICKED");
				}

				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);
				result.setData(rt);
				auditStatus(rt.getTicketId(), rt.getStatus());

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> cancelTicket(String ticketId, String remarks) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(ticketId);
			if (option.isPresent()) {
				Ticket returnObject = option.get();

				returnObject.setStatus("CANCELLED");

				auditRemarks(ticketId, returnObject.getStatus(), remarks);

				ticketRepository.save(returnObject);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	public Result<Ticket> assignTicket(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
			if (option.isPresent()) {
				Ticket returnObject = option.get();
				returnObject.setAssignedTo(t.getAssignedTo());
				returnObject.setAssignedOn(t.getAssignedOn());

				returnObject.setType(t.getType());
				returnObject.setStatus(t.getStatus());
				CommonUtil.setModifiedOn(returnObject);
				ticketRepository.save(returnObject);
				result.setCode("0000");
				result.setMessage(StringIteration.SUCCESS);
				result.setData(t);
				if (!(returnObject.getStatus().equals(StringIteration.INPROCESS)
						|| returnObject.getStatus().equals(StringIteration.COMPLETED))) {
					auditStatus(returnObject.getTicketId(), returnObject.getStatus());
				}
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> reassignTicket(Ticket t) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(t.getTicketId());
			if (option.isPresent()) {
				Ticket returnObject = option.get();

				if (returnObject.getStatus().equals(StringIteration.ASSIGNED)) {
					returnObject.setAssignedTo(t.getAssignedTo());
					returnObject.setType(t.getType());
					CommonUtil.setModifiedOn(returnObject);

					ticketRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage(StringIteration.SUCCESS);
					result.setData(t);
					if (!(returnObject.getStatus().equals(StringIteration.INPROCESS)
							|| returnObject.getStatus().equals(StringIteration.COMPLETED))) {
						auditStatus(returnObject.getTicketId(), returnObject.getStatus());
					}

				} else {
					result.setCode("1111");
					result.setMessage("error");

				}

			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error(e);
		}
		return result;
	}

	public Result<Ticket> addTickets(String ticketId, int count) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			Optional<Ticket> option = ticketRepository.findById(ticketId);

			if (us.isPresent() && option.isPresent()) {
				User u = us.get();
				Ticket t = option.get();
				List<Ticket> tic = new ArrayList<>();
				for (int i = count; i >= 1; i--) {
					CommonUtil.setCreatedOn(t);
					t.setTicketId(getLastSequenceByManufacturer(u.getTenantId(), t.getManufacturerId()));
					tic.add(ticketRepository.save(t));
					auditStatus(t.getTicketId(), t.getStatus());
				}
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
			}
		} catch (Exception e) {
			result.setCode("1111");
			logger.error("", e);
		}
		return result;
	}

	public Result<Ticket> blockTicket(String ticketId, String remarks, String stockistId) {
		Result<Ticket> result = new Result<>();
		try {
			Optional<Ticket> option = ticketRepository.findById(ticketId);
			if (option.isPresent()) {
				Ticket returnObject = option.get();
				if (returnObject.getStatus().equals(StringIteration.ASSIGNED)) {
					returnObject.setStatus(StringIteration.BLOCKED);
					returnObject.setStockistId(stockistId);
					auditRemarks(ticketId, returnObject.getStatus(), remarks);
					ticketRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage("Successful Created");

				} else if (returnObject.getStatus().equals("BLOCKED")) {
					returnObject.setStatus(StringIteration.ASSIGNED);
					auditRemarks(ticketId, returnObject.getStatus(), remarks);

					ticketRepository.save(returnObject);
					result.setCode("0000");
					result.setMessage("Successful Created");
				}

				else {
					result.setCode("1111");
					result.setMessage("error");

				}

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
	public void returMail(Ticket ticket, String adminMail, String stockistId) {
		try {
			EmailTemplate emailTemplate = emailTemplateService.findById(adminMail);
			List<String> list = new ArrayList<>();
			List<String> name = new ArrayList<>();
			if (emailTemplate != null) {
				Optional<Tenant> tenant = tenantRepository.findById(ticket.getTenantId());
				if (tenant.isPresent()) {
					list.add(tenant.get().getEmailId());
					name.add(tenant.get().getTenantName());

					returnNotification(tenant.get().getTenantId(), adminMail);
					Optional<Role> role = roleRepository.findByRoleName(StringIteration.MANAGER);
					if (role.isPresent()) {
						Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
								role.get().getRoleId());
						if (user.isPresent()) {
							list.add(user.get().getEmail());
							name.add(user.get().getFirstName() + " " + user.get().getLastName());
							returnNotification(user.get().getUserId(), adminMail);
						}
					}
					Optional<Role> roleUser = roleRepository.findByRoleName(StringIteration.USER);

					if (roleUser.isPresent()) {
						Optional<User> user = userRepository.findUserByRoleAndTenant(tenant.get().getTenantId(),
								roleUser.get().getRoleId());
						if (user.isPresent()) {
							list.add(user.get().getEmail());
							name.add(user.get().getFirstName() + " " + user.get().getLastName());
							returnNotification(user.get().getUserId(), adminMail);
						}
					}

				}
				VelocityContext context = new VelocityContext();
				context.put("origin", ConfigUtil.getAppLink());
				context.put("language", "en");
				StringWriter writer = new StringWriter();
				String templateStr = emailTemplate.getMailTemplate();
				Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);
				String[] s = list.toArray(new String[0]);
				emailService.sendEmails(s, writer.toString(), emailTemplate.getSubject(), name);
			}
			if (!StringUtil.isNullOrEmpty(stockistId)) {
				EmailTemplate emailTemplateStockist = emailTemplateService.findById("");

				if (emailTemplateStockist != null) {
					VelocityContext context = new VelocityContext();
					context.put("origin", ConfigUtil.getAppLink());
					context.put("language", "en");

					StringWriter writer = new StringWriter();
					String templateStr = emailTemplateStockist.getMailTemplate();
					Velocity.evaluate(context, writer, Constants.LOGTAGNAME, templateStr);

				}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}

	}

	public void returnNotification(String userId, String temp) {
		NotificationTemplate template = notificationTemplateService.findById(temp);
		Optional<User> uid = userRepository.findByUserId(SecurityUtil.getUserName());
		String tid = null;
		if (uid.isPresent()) {
			tid = uid.get().getTenantId();
		}
		if (template != null) {
			notificationService.notifications(userId, template.getSubject(), template.getNotificationTemplate(), null,
					tid);
		}
	}

	public Result<HashMap<String, Object>> getAllTicket(int page, String value, String status, String sortBy,
			String sortDir) {
		Result<HashMap<String, Object>> result = new Result<>();

		try {

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					return getAllTicketByManager(page, value, status, sortBy, sortDir, us);
				} else if (us.getRoleName().equals(StringIteration.USER)) {
					return getAllTicketByUser(page, value, status, sortBy, sortDir, us);
				}

				else if (us.getRoleName().equals(StringIteration.STOCKIST)) {
					return getAllTicketByStockist(page, value, status, sortBy, sortDir, us);
				} else {
					result.setMessage("Invalid User");
					result.setCode(StringIteration.ERROR_CODE1);
				}
			}

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.SUCCESS_CODE);

		}

		return result;
	}

	public Result<HashMap<String, Object>> getAllTicketByStockist(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {

			HashMap<String, Object> map = new HashMap<>();
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User uu = user.get();
				int size = 10;

				if (StringUtils.isNullOrEmpty(sortBy)) {
					sortBy = StringIteration.CREATED_ON;
				}
				if (StringUtils.isNullOrEmpty(sortDir)) {
					sortDir = "DESC";
				}
				Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
				Stockist st = stockistRepository.findByUserId(us.getUserId(), us.getTenantId());

				List<Ticket> tickets = ticketRepository.getTicketsByAssignedTo(st.getStockistId(), status, paging,
						uu.getTenantId());
				tickets = getAllTicketByStockistValue(tickets, value);
				int totalItems = ticketRepository.getTicketsByAssignedTo(st.getStockistId(), status, uu.getTenantId())
						.size();
				for (Ticket t : tickets) {
					if (t.getAssignedTo() != null) {
						Optional<User> u = userRepository.findById(t.getAssignedTo());
						if (u.isPresent()) {
							t.setUserName(u.get().getFirstName() + u.get().getLastName());
						}
					}

				}
				TicketSummary summary = new TicketSummary();
				summary.setTotal(
						ticketRepository.getTicketsByAssignedTo(st.getStockistId(), "", uu.getTenantId()).size());
				summary.setUnAssigned(ticketRepository
						.getTicketsByAssignedTo(st.getStockistId(), StringIteration.CREATED, uu.getTenantId()).size());
				summary.setInProcess(ticketRepository
						.getTicketsByAssignedTo(st.getStockistId(), StringIteration.INPROCESS, uu.getTenantId())
						.size());
				summary.setCompleted(ticketRepository
						.getTicketsByAssignedTo(st.getStockistId(), StringIteration.COMPLETED, uu.getTenantId())
						.size());
				summary.setAssigned(ticketRepository
						.getTicketsByAssignedTo(st.getStockistId(), StringIteration.ASSIGNED, uu.getTenantId()).size());

				map.put(StringIteration.TICKETS, tickets);
				map.put(StringIteration.TOTALCOUNT, totalItems);
				map.put(StringIteration.SUMMARY, summary);
				map.put(StringIteration.USER, StringIteration.STOCKIST);

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULCREATED);
				result.setData(map);

			}

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}

		return result;
	}

	private List<Ticket> getAllTicketByStockistValue(List<Ticket> tickets, String value) {
		if (value != null && !value.isEmpty()) {
			tickets = tickets.stream().filter(obj -> {
				String ticketId = obj.getTicketId();
				String type = obj.getType();
				String subject = obj.getSubject();
				String formattedCreatedOn = formatDate(obj.getCreatedOn());
				String assignedTo = obj.getAssignedTo();

				String emailedBy = obj.getEmailedBy();

				return (ticketId != null && ticketId.toLowerCase().contains(value.toLowerCase()))
						|| (type != null && type.toLowerCase().contains(value.toLowerCase()))
						|| (subject != null && subject.toLowerCase().contains(value.toLowerCase()))
						|| (formattedCreatedOn != null
								&& formattedCreatedOn.toLowerCase().contains(value.toLowerCase()))
						|| (assignedTo != null && assignedTo.toLowerCase().contains(value.toLowerCase()))

						|| (emailedBy != null && emailedBy.toLowerCase().contains(value.toLowerCase()));
			}).collect(Collectors.toList());
		}

		return tickets;
	}

	public Result<HashMap<String, Object>> getAllTicketByUser(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User currentUser = user.get();
				int size = 10;
				Pageable paging = createPageableTicket(page, size, sortBy, sortDir);
				Page<Ticket> tickets = getTicketsByStatus(status, currentUser, us, paging, value);
				updateTicketsWithUserInfo(tickets);

//				for(Ticket t:tickets) {
//					if(t.getType()!=null) {
//					t.setType(listvalue.findByCodeAndName(t.getType()));;
//					}
//					else {
//						t.setType("");
//					}
//				}

				TicketSummary summary = generateTicketSummary(currentUser, us);

				map.put(StringIteration.TICKETS, tickets);

				map.put(StringIteration.SUMMARY, summary);
				map.put(StringIteration.USERS, StringIteration.USER);

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYCREATED);
				result.setData(map);
			}
		} catch (Exception e) {
			logger.error(e);
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}

		return result;
	}

	private Page<Ticket> getTicketsByStatus(String status, User currentUser, User us, Pageable paging, String value) {

		Page<Ticket> tickets = null;
		if (status.equalsIgnoreCase(StringIteration.INPROCESS)) {

			tickets = ticketRepository.getTicketsByUser(us.getUserId(), StringIteration.INPROCESS, paging,
					currentUser.getTenantId(), value);
		} else if (status.equalsIgnoreCase(StringIteration.COMPLETED)) {

			tickets = ticketRepository.getTicketsByUser(us.getUserId(), StringIteration.COMPLETED, paging,
					currentUser.getTenantId(), value);
		}

		else if (status.equalsIgnoreCase("ALLUSER")) {

			tickets = ticketRepository.getTicketsByUser(us.getUserId(), "", paging, currentUser.getTenantId(), value);

		} else {
			tickets = ticketRepository.getTicketsByUser(us.getUserId(), StringIteration.ASSIGNED, paging,
					currentUser.getTenantId(), value);
		}
		return tickets;
	}

	private TicketSummary generateTicketSummary(User currentUser, User us) {
		TicketSummary summary = new TicketSummary();
		summary.setTotal(ticketRepository.getTicketsByUser(us.getUserId(), "", currentUser.getTenantId()).size());
		summary.setUnAssigned(ticketRepository
				.getTicketsByUser(us.getUserId(), StringIteration.CREATED, currentUser.getTenantId()).size());
		summary.setInProcess(ticketRepository
				.getTicketsByUser(us.getUserId(), StringIteration.INPROCESS, currentUser.getTenantId()).size());
		summary.setCompleted(ticketRepository
				.getTicketsByUser(us.getUserId(), StringIteration.COMPLETED, currentUser.getTenantId()).size());
		summary.setAssigned(ticketRepository
				.getTicketsByUser(us.getUserId(), StringIteration.ASSIGNED, currentUser.getTenantId()).size());
		return summary;
	}

	public Result<HashMap<String, Object>> getAllTicketByManager(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			int size = 10;
			HashMap<String, Object> map = new HashMap<>();
			Pageable paging = createPageableTicket(page, size, sortBy, sortDir);
			String assignedUser = "";

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User currentUser = op.get();
				assignedUser = currentUser.getUserId();

				Page<Ticket> tickets = getTickets(status, us, paging, assignedUser, value);
				updateTicketsWithUserInfo(tickets);
				TicketSummary summary = calculateTicketSummary(us);
				map.put(StringIteration.TICKETS, tickets);

				map.put(StringIteration.SUMMARY, summary);
				map.put(StringIteration.USERS, StringIteration.MANAGER);

				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYCREATED);
				result.setData(map);
			}
		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
		return result;
	}

	private Pageable createPageableOrder(int page, int size, String sortBy, String sortDir) {
		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = "a.sent_date";
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir = "DESC";
		}
		return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	}

	private Pageable createPageableTicket(int page, int size, String sortBy, String sortDir) {
		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = "sent_date";
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir = "DESC";
		}
		return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	}

	private Pageable createPageableTicketUser(int page, int size, String sortBy, String sortDir) {
		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = "sent_date";
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir = "DESC";
		}
		return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	}

	private Page<Ticket> getTickets(String status, User us, Pageable paging, String assignedUser, String value) {
		Page<Ticket> tickets;
		if (status.equalsIgnoreCase("SELF")) {

			tickets = ticketRepository.getTicketBySelf(us.getUserId(), us.getTenantId(), paging, assignedUser, value);
		} else if (status.equalsIgnoreCase("OTHERS")) {
			tickets = ticketRepository.getTicketByUser(us.getUserId(), us.getTenantId(), paging, assignedUser, value);
		} else if (status.equalsIgnoreCase("ASSIGNED")) {

			tickets = ticketRepository.getTicketAllAssinged(us.getUserId(), us.getTenantId(), paging, value);
		}

		else {

			tickets = ticketRepository.getTicketByManager(us.getUserId(), us.getTenantId(), status, paging, value);
		}
		return tickets;
	}

	private void updateTicketsWithUserInfo(Page<Ticket> tickets) {
		for (Ticket t : tickets) {
			if (t.getAssignedTo() != null) {
				Optional<User> u = userRepository.findById(t.getAssignedTo());
				if (u.isPresent()) {
					t.setUserName(u.get().getFirstName() + u.get().getLastName());
				}
			}
		}
	}

	private TicketSummary calculateTicketSummary(User us) {
		TicketSummary summary = new TicketSummary();
		return summaryStatus(summary, us);
	}

	private TicketSummary summaryStatus(TicketSummary summary, User us) {
		summary.setTotal(ticketRepository.getByTicketManager(us.getUserId(), "", us.getTenantId()).size());
		summary.setUnAssigned(
				ticketRepository.getByTicketManager(us.getUserId(), StringIteration.CREATED, us.getTenantId()).size());
		summary.setInProcess(ticketRepository
				.getByTicketManager(us.getUserId(), StringIteration.INPROCESS, us.getTenantId()).size());
		summary.setCompleted(ticketRepository
				.getByTicketManager(us.getUserId(), StringIteration.COMPLETED, us.getTenantId()).size());
		summary.setAssigned(
				ticketRepository.getByTicketManager(us.getUserId(), StringIteration.ASSIGNED, us.getTenantId()).size());
		return summary;
	}

	public Result<HashMap<String, Object>> getAllTickets(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			HashMap<String, Object> map = new HashMap<>();
			int size = 10;

			if (StringUtils.isNullOrEmpty(sortBy)) {
				sortBy = StringIteration.CREATED_ON;
			}
			if (StringUtils.isNullOrEmpty(sortDir)) {
				sortDir = "DESC";
			}
			Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

			List<Ticket> tickets = ticketRepository.getAllTicketsByTenant(us.getTenantId(), status, value, paging);
			for (Ticket t : tickets) {
				if (t.getAssignedTo() != null) {
					Optional<User> u = userRepository.findById(t.getAssignedTo());
					if (u.isPresent()) {
						t.setUserName(u.get().getFirstName() + u.get().getLastName());
					}
				}
			}
			int totalItems = ticketRepository.getAllTicketsByTenant(us.getTenantId(), status).size();
			TicketSummary summary = new TicketSummary();
			summary.setTotal(ticketRepository.getAllTicketsByTenant(us.getTenantId(), "").size());
			summary.setUnAssigned(ticketRepository.getAllTicketsByTenant(us.getTenantId(), "CREATED").size());
			summary.setInProcess(ticketRepository.getAllTicketsByTenant(us.getTenantId(), "INPROCESS").size());
			summary.setCompleted(ticketRepository.getAllTicketsByTenant(us.getTenantId(), "COMPLETED").size());
			summary.setAssigned(ticketRepository.getAllTicketsByTenant(us.getTenantId(), "ASSIGNED").size());

			map.put("tickets", tickets);
			map.put(StringIteration.TOTALCOUNT, totalItems);
			map.put("summary", summary);
			map.put("user", "SUPERADMIN");

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.SUCCESS);
			result.setData(map);
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public List<ManagerManufacturerMapping> getAllMangerManufacture() {
		List<ManagerManufacturerMapping> list = new ArrayList<>();
		Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
		if (us.isPresent()) {
			User u = us.get();
			if (u.getRoleName().equals(StringIteration.MANAGER)) {
				list = managerManufacturerMappingRepository.findManufacturBYUserId(u.getUserId(), u.getTenantId());
			} else if (u.getRoleName().equals(StringIteration.USER)) {
				list = managerManufacturerMappingRepository.findManufacturBYUserId(u.getHierarachyId(),
						u.getTenantId());
			} else if (u.getRoleName().equals(StringIteration.SUPERADMIN)) {
				list = managerManufacturerMappingRepository.findManufacturBYUserId(u.getTenantId());
			}

		}

		return list;
	}

	public OrderSummary orderSummary() {
		OrderSummary summary = new OrderSummary();
		try {
			summary.setOrders(ticketRepository.findByType());
			summary.setTotal(summary.getOrders().size());
		} catch (Exception e) {
			logger.error("", e);
		}
		return summary;
	}

	public Result<HashMap<String, Object>> getAllOrders(int page, String value, String status, String sortBy,
			String sortDir) {
		Result<HashMap<String, Object>> result = new Result<>();

		try {

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();

				if (us.getRoleName().equals("MANAGER")) {
					return getAllorderByManager(page, value, status, sortBy, sortDir, us);
				} else if (us.getRoleName().equals("USER")) {
					return getAllorderByUser(page, value, sortBy, sortDir, us, status);
				} else if (us.getRoleName().equals(StringIteration.STOCKIST)) {
					return getAllorderByStockist(page, value, status, sortBy, sortDir, us);
				} else {
					result.setMessage("Invalid User");
					result.setCode(StringIteration.ERROR_CODE1);
				}
			}

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.SUCCESS_CODE);

		}

		return result;
	}

	public Result<HashMap<String, Object>> getAllorderByStockist(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User currentUser = user.get();
				Stockist stockist = stockistRepository.findByUserId(us.getUserId(), us.getTenantId());
				if (stockist != null) {
					HashMap<String, Object> map = new HashMap<>();
					int size = 10;
					Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

					List<Ticket> orders = ticketRepository.getOrderByAssignedTo(stockist.getStockistId(), status,
							paging, currentUser.getTenantId());
					updateTicketDetails(orders);

					if (value != null && !value.isEmpty()) {
						orders = filterOrdersByValue(orders, value);
					}

					int totalItems = ticketRepository
							.getOrderByAssignedTo(stockist.getStockistId(), currentUser.getTenantId()).size();
					OrderSummary orderSummary = getOrderSummary(us.getUserId(), currentUser.getTenantId());

					map.put(StringIteration.ORDERS, orders);
					map.put(StringIteration.TOTALCOUNT, totalItems);
					map.put(StringIteration.ORDER, orderSummary);
					map.put(StringIteration.USER, "STOCKIST");

					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.SUCCESS);
					result.setData(map);
				}
			}
		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private void updateTicketDetails(List<Ticket> orders) {
		for (Ticket t : orders) {
			t.setStockistName(t.getStockistId());
			if (t.getAssignedTo() != null) {
				Optional<User> user = userRepository.findById(t.getAssignedTo());
				user.ifPresent(u -> t.setUserName(u.getFirstName() + u.getLastName()));
			}
		}
	}

	private List<Ticket> filterOrdersByValue(List<Ticket> orders, String value) {
		return orders.stream().filter(obj -> {
			String ticketId = obj.getTicketId();
			String type = obj.getType();
			String subject = obj.getSubject();
			String formattedCreatedOn = formatDate(obj.getCreatedOn());
			String assignedTo = obj.getAssignedTo();
			String stockistName = obj.getStockistName();
			String emailedBy = obj.getEmailedBy();

			return (containsIgnoreCase(ticketId, value) || containsIgnoreCase(type, value)
					|| containsIgnoreCase(subject, value) || containsIgnoreCase(formattedCreatedOn, value)
					|| containsIgnoreCase(assignedTo, value) || containsIgnoreCase(stockistName, value)
					|| containsIgnoreCase(emailedBy, value));
		}).collect(Collectors.toList());
	}

	private boolean containsIgnoreCase(String str1, String str2) {
		return str1 != null && str2 != null && str1.toLowerCase().contains(str2.toLowerCase());
	}

	private OrderSummary getOrderSummary(String userId, String tenantId) {
		OrderSummary orderSummary = new OrderSummary();
		orderSummary.setTotal(ticketRepository.getOrderByAssignedTo(userId, tenantId).size());
		orderSummary.setPicked(ticketRepository.getOrderAssignedTo(userId, "PICKED", tenantId).size());
		orderSummary.setChecked(ticketRepository.getOrderAssignedTo(userId, "CHECKED", tenantId).size());
		orderSummary.setDeliverd(ticketRepository.getOrderAssignedTo(userId, "DELIVERED", tenantId).size());
		orderSummary.setDispatched(ticketRepository.getOrderAssignedTo(userId, "DISPATCHED", tenantId).size());
		orderSummary.setPacked(ticketRepository.getOrderAssignedTo(userId, "PACKED", tenantId).size());
		orderSummary.setCancel(ticketRepository.getOrderAssignedTo(userId, "CANCELLED", tenantId).size());
		orderSummary.setBlocked(ticketRepository.getOrderAssignedTo(userId, "BLOCKED", tenantId).size());
		return orderSummary;
	}

	public Result<HashMap<String, Object>> getAllorderByUser(int page, String value, String sortBy, String sortDir,
			User user, String status) {
		int totalItems = 0;
		Result<HashMap<String, Object>> result = new Result<>();
		try {
			Optional<User> currentUser = getCurrentUser();
			if (currentUser.isPresent()) {
				HashMap<String, Object> map = new HashMap<>();
				int size = 10;
				Pageable paging = createPageableOrder(page, size, sortBy, sortDir);
				List<Object[]> orders = new ArrayList<>();
//				List<Object[]> orders = retrieveOrders(user, value, paging);

				Optional<User> users = userRepository.findById(SecurityUtil.getUserName());
				User us = users.get();
				List<Object[]> ordersCount;

				if (status.equalsIgnoreCase(StringIteration.POD_RECEIVED)) {
					orders = ticketRepository.getOrderByUserPod(us.getUserId(), us.getTenantId(), value, paging);
					totalItems = ticketRepository
							.getOrderByUserPodWithOutPaging(us.getUserId(), us.getTenantId(), value).size();
					ordersCount = ticketRepository.getOrderByUserPodWithOutPaging(us.getUserId(), us.getTenantId(), "");

				} else if (status.equalsIgnoreCase(StringIteration.CCD_RECEIVED)) {
					orders = ticketRepository.getOrderByUserCcd(us.getUserId(), us.getTenantId(), value, paging);
					totalItems = ticketRepository
							.getOrderByUserCcdWithOutPaging(us.getUserId(), us.getTenantId(), value).size();
					ordersCount = ticketRepository.getOrderByUserCcdWithOutPaging(us.getUserId(), us.getTenantId(), "");

				} else if (status.equalsIgnoreCase(StringIteration.BLOCKED)) {
					orders = ticketRepository.getOrderByUser(us.getUserId(), us.getTenantId(), value,
							StringIteration.BLOCKED, paging);
					totalItems = ticketRepository.getOrderByUserWithOutPaging(us.getUserId(), us.getTenantId(), value,
							StringIteration.BLOCKED).size();
					ordersCount = ticketRepository.getOrderByUserWithOutPaging(us.getUserId(), us.getTenantId(), "",
							StringIteration.BLOCKED);

				} else if (status.equalsIgnoreCase("ALLUSER")) {
					orders = ticketRepository.getOrderByUser(us.getUserId(), us.getTenantId(), value, "", paging);
					totalItems = ticketRepository
							.getOrderByUserWithOutPaging(us.getUserId(), us.getTenantId(), value, "").size();
					ordersCount = ticketRepository.getOrderByUserWithOutPaging(us.getUserId(), us.getTenantId(), "",
							"");
				}

				else {
					orders = ticketRepository.getOrderByUserrAssigned(us.getUserId(), us.getTenantId(), value,
							StringIteration.ASSIGNED, paging);
					totalItems = ticketRepository.getOrderByUserWithOutPagingAssigned(us.getUserId(), us.getTenantId(),
							value, StringIteration.ASSIGNED).size();
					ordersCount = ticketRepository.getOrderByUserWithOutPagingAssigned(us.getUserId(), us.getTenantId(),
							"", StringIteration.ASSIGNED);

				}

				List<TicketInvoiceDTO> list = constructTicketInvoiceDTOList(orders);

				ordersCount = retrieveOrdersCount(user);
				Map<String, Long> statusCounts = countByIndex(ordersCount, 5);
				OrderSummary orderSummary = constructOrderSummary(statusCounts, ordersCount.size());

				populateMap(map, list, orderSummary);
				map.put(StringIteration.TOTALCOUNT, totalItems);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage("success");
				result.setData(map);
			}
		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	private Optional<User> getCurrentUser() {
		return userRepository.findByUserId(SecurityUtil.getUserName());
	}

//	private List<Object[]> retrieveOrders(User user, String value, Pageable paging) {
//		return ticketRepository.getOrderByManagerAssignSelf(user.getHierarachyId(), user.getTenantId(), value,
//				user.getUserId(), paging);
//	}

	private List<Object[]> retrieveOrdersCount(User user) {
		return ticketRepository.getOrderByManagerAssignSelfWithOutPaging(user.getHierarachyId(), user.getTenantId(), "",
				user.getUserId());
	}

	private List<TicketInvoiceDTO> constructTicketInvoiceDTOList(List<Object[]> orders) {
		List<TicketInvoiceDTO> list = new ArrayList<>();
		for (Object[] b : orders) {
			TicketInvoiceDTO t = new TicketInvoiceDTO();
			t.setInvoiceNumber(String.valueOf(b[0]));
			t.setTicketId(String.valueOf(b[1]));
			t.setCreatedOn(String.valueOf(b[2]));
			t.setUserName(String.valueOf(b[3]));
			t.setEmailedBy(String.valueOf(b[4]));
			if (b[6] != null) {
				t.setStockistName(String.valueOf(b[6]));
			} else {
				t.setStockistName("");
			}
			t.setSubject(String.valueOf(b[7]));
			t.setStatus(String.valueOf(b[5]));
			if (t.getStockistName() != null) {
				Optional<Stockist> st = stockistRepository.findById(t.getStockistName());
				if (st.isPresent()) {
					t.setStockistName(st.get().getStockistName());
					Optional<String> ct = cityRepository.findByCityCode(st.get().getCityId());
					ct.ifPresent(t::setLocation);
				}
			} else {
				t.setStockistName("");
			}

			t.setEml(String.valueOf(b[8]));

			t.setSentDate(b[9]);
			t.setFilePath(String.valueOf(b[10]));

			list.add(t);
		}
		return list;
	}

	private OrderSummary constructOrderSummary(Map<String, Long> statusCounts, int totalOrders) {
		OrderSummary orderSummary = new OrderSummary();
		updateOrderSummary(orderSummary, statusCounts);
		orderSummary.setTotal(totalOrders);
		return orderSummary;
	}

	private void populateMap(HashMap<String, Object> map, List<TicketInvoiceDTO> list, OrderSummary orderSummary) {
		map.put(StringIteration.ORDERS, list);

		map.put("order", orderSummary);
		map.put("user", "USER");
	}

	public static Map<String, Long> countObjectsByStatus(List<TicketInvoiceDTO> objects) {
		return objects.stream().collect(Collectors.groupingBy(TicketInvoiceDTO::getStatus, Collectors.counting()));
	}

	public static Map<String, Long> countByIndex(List<Object[]> list, int index) {
		return list.stream().map(array -> array[index])
				.collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
	}

	public static void updateOrderSummary(OrderSummary orderSummary, Map<String, Long> statusCounts) {
		orderSummary.setAssigned(statusCounts.getOrDefault(StringIteration.ASSIGNED, 0L).intValue());
		orderSummary.setInvoiceCreated(statusCounts.getOrDefault(StringIteration.INVOICECREATED, 0L).intValue());
		orderSummary.setPicked(statusCounts.getOrDefault(StringIteration.PICKED, 0L).intValue());
		orderSummary.setChecked(statusCounts.getOrDefault(StringIteration.CHECKEDD, 0L).intValue());
		orderSummary.setPacked(statusCounts.getOrDefault(StringIteration.PACKED, 0L).intValue());
		orderSummary.setDispatched(statusCounts.getOrDefault(StringIteration.DISPATCHED, 0L).intValue());
		orderSummary.setDelivered(statusCounts.getOrDefault(StringIteration.DELIVERED, 0L).intValue());
		orderSummary.setPodReceived(statusCounts.getOrDefault(StringIteration.PODRECEIVED, 0L).intValue());
		orderSummary.setCancel(statusCounts.getOrDefault(StringIteration.CANCELLED, 0L).intValue());
		orderSummary.setBlocked(statusCounts.getOrDefault(StringIteration.BLOCKED, 0L).intValue());
		orderSummary
				.setTransporterAssigned(statusCounts.getOrDefault(StringIteration.TRANSPORTER_ASSIGNED, 0L).intValue());
	}

	public Result<HashMap<String, Object>> getAllorderByManager(int page, String value, String status, String sortBy,
			String sortDir, User us) {
		Result<HashMap<String, Object>> result = new Result<>();
		try {

			HashMap<String, Object> map = new HashMap<>();

			int size = 10;

			Pageable paging = createPageableOrder(page, size, sortBy, sortDir);
			HashMap<String, Object> values = getOrders(status, paging, us, value);

			int totalItems = (int) values.get("totalItems");
			@SuppressWarnings("unchecked")
			List<Object[]> orders = (List<Object[]>) values.get(StringIteration.ORDERS);
			@SuppressWarnings("unchecked")
			List<Object[]> ordersCount = (List<Object[]>) values.get("ordersCount");

			List<TicketInvoiceDTO> list = getOrderList(orders);
			Map<String, Long> statusCounts = countByIndex(ordersCount, 5);
			OrderSummary order = new OrderSummary();
			updateOrderSummary(order, statusCounts);
			order.setTotal(ordersCount.size());

			map.put(StringIteration.ORDERS, list);
			map.put(StringIteration.TOTALCOUNT, totalItems);
			map.put("order", order);
			map.put("user", "MANAGER");

			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage("sucess");
			result.setData(map);

		} catch (Exception e) {
			result.setMessage("");
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}

		return result;

	}

	private List<TicketInvoiceDTO> getOrderList(List<Object[]> orders) {
		List<TicketInvoiceDTO> list = new ArrayList<>();
		for (Object[] b : orders) {
			TicketInvoiceDTO t = new TicketInvoiceDTO();
			t.setInvoiceNumber(String.valueOf(b[0]));
			t.setTicketId(String.valueOf(b[1]));
			if (b[2] != null) {
				t.setCreatedOn(String.valueOf(b[2]));
			}
			t.setUserName(String.valueOf(b[3]));
			t.setEmailedBy(String.valueOf(b[4]));
			if (b[6] != null) {
				t.setStockistName(String.valueOf(b[6]));
			}
			t.setSubject(String.valueOf(b[7]));
			t.setStatus(String.valueOf(b[5]));

			if (b[8] != null) {
				t.setEml(String.valueOf(b[8]));
			}
			t.setSentDate(b[9]);
			t.setFilePath(String.valueOf(b[10]));

			if (t.getStockistName() != null && t.getStockistName().length() > 0
					&& stockistRepository.findById(t.getStockistName()).isPresent()) {
				Optional<Stockist> st = stockistRepository.findById(t.getStockistName());
				if (st.isPresent()) {
					t.setStockistName(st.get().getStockistName());
					Optional<String> ct = cityRepository.findByCityCode(st.get().getCityId());
					if (ct.isPresent()) {
						t.setLocation(ct.get());
					}
				}
			}

			list.add(t);

		}
		return list;

	}

	private HashMap<String, Object> getOrders(String status, Pageable paging, User us, String value) {

		HashMap<String, Object> mapValue = new HashMap<>();
		int totalItems = 0;
		List<Object[]> orders;
		List<Object[]> ordersCount;

		if (status.equalsIgnoreCase("SELF")) {
			orders = ticketRepository.getOrderByManagerAssignSelf(us.getUserId(), us.getTenantId(), value,
					us.getUserId(), paging);
			totalItems = ticketRepository
					.getOrderByManagerAssignSelfWithOutPaging(us.getUserId(), us.getTenantId(), value, us.getUserId())
					.size();
			ordersCount = ticketRepository.getOrderByManagerAssignSelfWithOutPaging(us.getUserId(), us.getTenantId(),
					"", us.getUserId());

		} else if (status.equalsIgnoreCase("OTHERS")) {
			orders = ticketRepository.getOrderByManagerAssignOthers(us.getUserId(), us.getTenantId(), value,
					us.getUserId(), paging);
			totalItems = ticketRepository
					.getOrderByManagerAssignOthersWithOutPaging(us.getUserId(), us.getTenantId(), value, us.getUserId())
					.size();
			ordersCount = ticketRepository.getOrderByManagerAssignOthersWithOutPaging(us.getUserId(), us.getTenantId(),
					"", us.getUserId());

		} else if (status.equalsIgnoreCase(StringIteration.POD_RECEIVED)) {
			orders = ticketRepository.getOrderByManagerPod(us.getUserId(), us.getTenantId(), value, paging);
			totalItems = ticketRepository.getOrderByManagerPodWithOutPaging(us.getUserId(), us.getTenantId(), value)
					.size();
			ordersCount = ticketRepository.getOrderByManagerPodWithOutPaging(us.getUserId(), us.getTenantId(), "");

		} else if (status.equalsIgnoreCase(StringIteration.CCD_RECEIVED)) {
			orders = ticketRepository.getOrderByManagerCcd(us.getUserId(), us.getTenantId(), value, paging);
			totalItems = ticketRepository.getOrderByManagerCcdWithOutPaging(us.getUserId(), us.getTenantId(), value)
					.size();
			ordersCount = ticketRepository.getOrderByManagerCcdWithOutPaging(us.getUserId(), us.getTenantId(), "");

		} else if (status.equalsIgnoreCase(StringIteration.ASSIGNED)) {
			orders = ticketRepository.getOrderByManagerAssigned(us.getUserId(), us.getTenantId(), value, status,
					paging);
			totalItems = ticketRepository
					.getOrderByManagerWithOutPagingAssigned(us.getUserId(), us.getTenantId(), value, status).size();
			ordersCount = ticketRepository.getOrderByManagerWithOutPagingAssigned(us.getUserId(), us.getTenantId(), "",
					status);
		}

		else {
			orders = ticketRepository.getOrderByManager(us.getUserId(), us.getTenantId(), value, status, paging);
			totalItems = ticketRepository
					.getOrderByManagerWithOutPaging(us.getUserId(), us.getTenantId(), value, status).size();
			ordersCount = ticketRepository.getOrderByManagerWithOutPaging(us.getUserId(), us.getTenantId(), "", status);
		}
		mapValue.put("totalItems", totalItems);
		mapValue.put(StringIteration.ORDERS, orders);
		mapValue.put("ordersCount", ordersCount);

		return mapValue;

	}

	private static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(date);
	}

	public List<Ticket> findAllOrders() {
		Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
		if (op.isPresent()) {
			User us = op.get();
			Stockist st = stockistRepository.findByUserId(us.getUserId(), us.getTenantId());
			if (us.getRoleName().equals(StringIteration.STOCKIST)) {
				return ticketRepository.findOrderByStk(us.getTenantId(), st.getStockistId());
			} else {
				return ticketRepository.orderByuser(us.getTenantId(), us.getUserId());
			}
		}
		return Collections.emptyList();
	}

	public Result<HashMap<String, Object>> findTransportOrders(String search, int page, String sortDir, String sortBy,
			String status, String stockist, String manufacturer, String trans) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());

			Result<HashMap<String, Object>> result = new Result<>();
			HashMap<String, Object> map = new HashMap<>();
			List<TicketReportsDTO> li = new ArrayList<>();

			if (StringUtils.isNullOrEmpty(sortBy)) {
				sortBy = StringIteration.CREATED_ON;
			}
			if (StringUtils.isNullOrEmpty(sortDir)) {
				sortDir = "DESC";
			}

			Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

			if (us.isPresent()) {
				User u = us.get();

				if (u.getRoleName().equals(StringIteration.TRANSPORT)) {
					List<Object[]> list = invoiceRepository.getTransporterOrders(u.getUserId(), u.getTenantId(), search,
							status, pageable, stockist, manufacturer);
					int totalCount = invoiceRepository.getTransporterOrders(u.getUserId(), u.getTenantId(), search,
							status, stockist, manufacturer).size();
					li = findTransportOrdersOb(list);

					List<Object[]> counts = invoiceRepository.getTransporterOrdersCount(u.getUserId(), u.getTenantId());
					Map<String, Object> coun = new HashMap<>();

					coun.put("total", counts.get(0)[0]);
					coun.put("dispatched", counts.get(0)[1]);
					coun.put("delivered", counts.get(0)[2]);
					if (counts.get(0)[3] != null) {
						coun.put(StringIteration.CASES, counts.get(0)[3]);
					} else {
						coun.put(StringIteration.CASES, "0");
					}
					coun.put("assigned", counts.get(0)[4]);
					map.put("orders", li);
					map.put(StringIteration.TOTALCOUNT, totalCount);
					map.put("counts", coun);

					result.setCode(StringIteration.SUCCESS_CODE);
					result.setData(map);

				}

				else if (u.getRoleName().equals(StringIteration.SUPERADMIN)) {
					List<Object[]> list = invoiceRepository.getAllTransporterOrders(u.getTenantId(), search, status,
							pageable, stockist, manufacturer, trans);
					int totalCount = invoiceRepository
							.getAllTransporterOrders(u.getTenantId(), search, status, stockist, manufacturer, trans)
							.size();
					li = findTransportOrdersSuperAdmin(list);

					List<Object[]> counts = invoiceRepository.getAllTransporterOrdersCount(u.getTenantId());
					Map<String, Object> coun = new HashMap<>();

					coun.put("total", counts.get(0)[0]);
					coun.put("dispatched", counts.get(0)[1]);
					coun.put("delivered", counts.get(0)[2]);
					if (counts.get(0)[3] != null) {
						coun.put("cases", counts.get(0)[3]);
					} else {
						coun.put("cases", "0");
					}
					coun.put("assigned", counts.get(0)[4]);
					map.put("orders", li);
					map.put("totalCount", totalCount);
					map.put("counts", coun);

					result.setCode(StringIteration.SUCCESS_CODE);
					result.setData(map);
				}

				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<TicketReportsDTO> findTransportOrdersSuperAdmin(List<Object[]> list) {
		List<TicketReportsDTO> li = new ArrayList<>();
		for (Object[] i : list) {
			TicketReportsDTO m = new TicketReportsDTO();
			m.setInvoiceNumber(String.valueOf(i[0]));
			m.setNoOfCases(String.valueOf(i[1]));
			m.setStockist(String.valueOf(i[2]));
			m.setManufacturer(String.valueOf(i[3]));
			m.setLocation(String.valueOf(i[4]));
			m.setStatus(String.valueOf(i[5]));

			if (m.getStatus().equals(StringIteration.POD_RECEIVED)) {
				m.setStatus(StringIteration.DELIVERED);
			}

			if (i[6] != null) {
				m.setAssignedOn(String.valueOf(i[6]));
			} else {
				m.setAssignedOn("");
			}
			m.setTransporter(String.valueOf(i[7]));
			li.add(m);
		}

		return li;
	}

	private List<TicketReportsDTO> findTransportOrdersOb(List<Object[]> list) {
		List<TicketReportsDTO> li = new ArrayList<>();
		for (Object[] i : list) {
			TicketReportsDTO m = new TicketReportsDTO();
			m.setInvoiceNumber(String.valueOf(i[0]));
			m.setNoOfCases(String.valueOf(i[1]));
			m.setStockist(String.valueOf(i[2]));
			m.setManufacturer(String.valueOf(i[3]));
			m.setLocation(String.valueOf(i[4]));
			m.setStatus(String.valueOf(i[5]));
			if (m.getStatus().equals(StringIteration.POD_RECEIVED)) {
				m.setStatus(StringIteration.DELIVERED);
			}
			if (i[6] != null) {
				m.setAssignedOn(String.valueOf(i[6]));
			} else {
				m.setAssignedOn("");
			}
			li.add(m);
		}

		return li;
	}

	public List<TicketStatusHistory> findTicketByIds(String id) {

		try {
			List<TicketStatusHistory> result = new ArrayList<>();
			Date date = null;

			List<TicketStatusHistory> ticketStatusHistory = ticketStatusHistoryRepository.findAllByTicketId(id);
			Set<String> processedInvoices = new HashSet<>();

			for (TicketStatusHistory ticketStatus : ticketStatusHistory) {
				TicketStatusHistory ticket = new TicketStatusHistory();
				ticket.setInvoice(ticketStatus.getInvoice());

				if (ticketStatus.getStatus().equalsIgnoreCase(StringIteration.ASSIGNED)) {
					date = ticketStatus.getCreatedOn();
				}

				if (ticketStatus.getInvoice() != null && !processedInvoices.contains(ticketStatus.getInvoice())) {
					List<TicketStatusHistory> invoice = ticketStatusHistoryRepository
							.findAByinvoiceNumbers(ticketStatus.getInvoice());
					Date date1 = null;
					Date date2 = null;
					Date date3 = null;
					Date date4 = null;
					Date date5 = null;
					Date date6 = null;
					Date date7 = null;
					Date date8 = null;

					for (TicketStatusHistory status : invoice) {
						if (status.getStatus().equalsIgnoreCase(StringIteration.INVOICECREATED)) {
							date1 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.PICKED)) {
							date2 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.CHECKED)) {
							date3 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.PACKED)) {
							date4 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.TRANSPORTER_ASSIGNED)) {
							date5 = status.getHistoryOn();
						}

						if (status.getStatus().equalsIgnoreCase(StringIteration.DISPATCHED)) {
							date6 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.DELIVERED)) {
							date7 = status.getHistoryOn();
						}
						if (status.getStatus().equalsIgnoreCase(StringIteration.PODRECEIVED)) {
							date8 = status.getHistoryOn();
						}
					}

					int count = 0;
					if (date != null) {
						count = daysBetweenTwoDates(new Date(), date);
					}
					if (date1 != null) {
						count = daysBetweenTwoDates(date1, date);
					}
					if (date2 != null) {
						count = daysBetweenTwoDates(date2, date);
					}
					if (date3 != null) {
						count = daysBetweenTwoDates(date3, date);
					}
					if (date4 != null) {
						count = daysBetweenTwoDates(date4, date);
					}
					if (date5 != null) {
						count = daysBetweenTwoDates(date5, date);
					}
					if (date6 != null) {
						count = daysBetweenTwoDates(date6, date);
					}
					if (date7 != null) {
						count = daysBetweenTwoDates(date7, date);
					}
					if (date8 != null) {
						count = daysBetweenTwoDates(date8, date);
					}

					ticket.setDurationDate(count);

					processedInvoices.add(ticketStatus.getInvoice());

					result.add(ticket);
				}
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Result<Ticket> findBylogin(String id) {
		Result<Ticket> result = new Result<>();
		try {

			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();
				Optional<Ticket> optional = ticketRepository.getTicketsByTenant(id, us.getTenantId());
				if (optional.isPresent()) {
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setData(optional.get());
				} else {
					result.setCode(StringIteration.ERROR_CODE3);
					result.setMessage(StringIteration.FILENOTFOUND);
				}
			} else {
				result.setCode(StringIteration.ERROR_CODE3);
				result.setMessage(StringIteration.INVALID_USER);
			}
		} catch (Exception e) {
			logger.error("", e);
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public Result<Object> findTicketsEml(String id) {
		Result<Object> result = new Result<>();
		Map<String, String> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<Ticket> ticket = ticketRepository.getTicketsByTenant(id, u.getTenantId());
				if (ticket.isPresent()) {
					Ticket t = ticket.get();
					Optional<TenantManufacture> tenantM = tenantManufactureRepository
							.findByManufactureId(t.getManufacturerId(), u.getTenantId());
					if (tenantM.isPresent()) {
						TenantManufacture tm = tenantM.get();

						map.put("host", tm.getHost());
						map.put("port", String.valueOf(tm.getPort()));
						map.put("userName", tm.getEmailId());
						map.put("password", tm.getPassword());
						map.put("messageId", t.getEml());
						System.err.println(map.toString());
//						Result<Object> response = sendPayloadToMailService(map);
						result.setCode(StringIteration.SUCCESS_CODE);
//						result.setData(response.getData());
					}
				}

			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("invalid user");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void sendMailStockist(User user, OrderDTO dto, String temp) {
		try {
			String location = "";
				EmailTemplate emailTemplate = emailTemplateService.findById(temp);
					if (emailTemplate != null) {
						Optional<Stockist> stk = stockistRepository.findById(dto.getStockistId());
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
						context.put("invoiceCreatedOnDate",dateFormatter(new Date()));
						context.put("DispatchDate",dateFormatter(new Date()));
						context.put("invoice", dto.getInvoice());
						context.put("CustomerPO",dto.getInvoice() );
						context.put("tenantName",tenant != null ? tenant.get().getTenantName() : "");
						context.put("location",location);
						context.put("deliveryDate",dateFormatter(dto.getDeliveryDate()));
						
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

//	private Result<Object> sendPayloadToMailService(Map<String, String> map) {
//		Result<Object> result = new Result<>();
//		try {
//			HttpHeaders header = new HttpHeaders();
//			header.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, header);
//			String url = "http://192.168.68.151:9090/rest/api/v1/get-s3-url";
//			System.err.println(new Date());
//			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//			logger.info(responseEntity.getBody());
//			System.err.println(new Date());
//			result.setData(responseEntity.getBody());
//			result.setCode(StringIteration.SUCCESS_CODE);
//		} catch (Exception e) {
//			logger.info(e);
//			e.printStackTrace();
//		}
//
//		return result;
//	}

	/*
	 * public Result<String> getMessageById(String host, int port, String userName,
	 * String password, String messageId,Result<String> result) {
	 * 
	 * try {
	 * 
	 * Properties props = configureEmailProperties(host, port); Session session =
	 * Session.getDefaultInstance(props); Store store =
	 * session.getStore(Constants.IMAPS); store.connect(host, port, userName,
	 * password); IMAPFolder inbox = (IMAPFolder) store.getFolder(Constants.INBOX);
	 * Folder[] inboxes = { inbox }; inbox.open(Folder.READ_WRITE);
	 * 
	 * SearchTerm searchTerm = new MessageIDTerm(messageId); Message[] messages =
	 * inbox.search(searchTerm);
	 * 
	 * String strDate = dateFormat.format(messages[0].getSentDate()); String[]
	 * splitEmailDate = strDate.trim().split("-"); String year = splitEmailDate[0];
	 * String month = splitEmailDate[1]; String date = splitEmailDate[2]; String
	 * folder = "email"; String strEmails3Dir = folder + File.separator + year +
	 * File.separator + month + File.separator + date + File.separator + "email" +
	 * File.separator; String url = processSaveToFile(messages[0],
	 * strEmails3Dir,userName);
	 * 
	 * result.setCode("0000"); result.setData(url);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); result.setCode("1111"); }
	 * 
	 * return result; }
	 */

}