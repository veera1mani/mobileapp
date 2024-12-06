package com.healthtraze.etraze.api.masters.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.dto.TicketDTO;
import com.healthtraze.etraze.api.masters.dto.TicketReportsDTO;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.TicketReportRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.report.model.OrderReport;
import com.healthtraze.etraze.api.report.model.ShipmentDetailReport;
import com.healthtraze.etraze.api.report.model.TicketReport;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class TicketReportService implements BaseService<Ticket, String> {
	private Logger logger = LogManager.getLogger(TicketReportService.class);

	private final TicketReportRepository ticketReportRepository;

	private final CityRepository cityReportRepository;

	private final UserRepository userRepository;

	private final TransportRepository transportRepository;
	private final ListValueRepository listvalue;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public TicketReportService(TicketReportRepository ticketReportRepository, TransportRepository transportRepository,
			CityRepository cityReportRepository, FileStorageService fileStorageService,

			UserRepository userRepository, ListValueRepository listvalue) {

		this.ticketReportRepository = ticketReportRepository;
		this.cityReportRepository = cityReportRepository;
		this.userRepository = userRepository;
		this.transportRepository = transportRepository;
		this.listvalue = listvalue;

	}

	@SuppressWarnings("unchecked")
	public List<TicketReport> ticketDetailsReport(Map<String, String> params) {
		try {
			List<TicketReport> obs = new ArrayList<>();

			String ticketId = "";
			String category = "";
			String status = "";
			String assignedTo = "";
			String stockist = "";
			String citi = "";
			String manufacturer = "";
			String emailDate = "";
			String locality = "";

			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET).trim();
			}
			if (params.get(StringIteration.CATEGORY) != null) {
				category = params.get(StringIteration.CATEGORY);
			}
			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}
			if (params.get(StringIteration.ASSIGNEDTO) != null) {
				assignedTo = params.get(StringIteration.ASSIGNEDTO);
			}
			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}
			if (params.get(StringIteration.LOCATION) != null) {
				citi = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.MANUFACTURER) != null)
				manufacturer = params.get(StringIteration.MANUFACTURER);

			if (params.get(StringIteration.EMAILDATE) != null) {
				emailDate = params.get(StringIteration.EMAILDATE);
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreports WHERE tenantid = :tenantId AND userid = :userId ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, citi,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getUserId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							citi, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();

				} else if (us.getRoleName().equals(StringIteration.USER)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreports WHERE tenantid = :tenantId AND userid = :userId ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, citi,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getHierarachyId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							citi, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();

				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreportssuper WHERE tenantid = :tenantId ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, citi,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							citi, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();
				}
				else if (us.getRoleName().equals(StringIteration.MANUFACTURERS)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreportssuper WHERE manufacturerid = :manufacturerid ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, citi,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter("manufacturerid", us.getUserId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							citi, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();
				}


			}
			return obs;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private Query ticketDetailsReportParameter(Query query, String status, String manufacturer, int month) {

		if (!status.isEmpty()) {
			query.setParameter("status", status);
		}
		if (!manufacturer.isEmpty()) {
			query.setParameter("manufacturer", manufacturer);
		}
		int year = LocalDateTime.now().getYear();
		if (month != 0) {
			query.setParameter("month", month);
			query.setParameter("year", year);
		}
		
		return query;
	}

	private Query ticketDetailsReportParameter(Query query, String ticketId, String category, String assignedTo,
			String stockist, String emailDate, String citi, String locality) {

		if (!ticketId.isEmpty()) {
			query.setParameter("ticketId", ticketId);
		}
		if (!category.isEmpty()) {
			query.setParameter("category", category);
		}

		if (!stockist.isEmpty()) {
			query.setParameter("stockist", stockist);
		}
		if (!assignedTo.isEmpty()) {
			query.setParameter("assignedTo", assignedTo);
		}
		if (!emailDate.isEmpty()) {
			query.setParameter("emailDate", emailDate);
		}
		if (!citi.isEmpty()) {
			query.setParameter("citi", citi);
		}
		if (!locality.isEmpty()) {
			query.setParameter("locality", locality);
		}

		return query;
	}

	private StringBuilder ticketDetailsReportBuilder1(StringBuilder sql, String status, String manufacturer,
			int month) {

		if (!status.isEmpty()) {
			sql = sql.append("AND status=:status ");
		}
		if (!manufacturer.isEmpty()) {
			sql = sql.append("AND manufacturer=:manufacturer ");
		}
		if (month != 0) {
			sql = sql.append(" AND emaildate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND emaildate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month') ");
		}
		return sql;
	}

	private StringBuilder ticketDetailsReportBuilder(StringBuilder sql, String ticketId, String category,
			String assignedTo, String stockist, String emailDate, String citi, String locality) {
		if (!ticketId.isEmpty()) {
			sql = sql.append(" AND ticketnumber=:ticketId ");
		}
		if (!category.isEmpty()) {
			
			sql = sql.append(" AND category=:category ");
		}

		if (!stockist.isEmpty()) {
			sql = sql.append(" AND stockist=:stockist ");
		}
		if (!assignedTo.isEmpty()) {
			sql = sql.append(" AND assignto=:assignedTo ");
		}
		if (!emailDate.isEmpty()) {
			sql = sql.append(" AND TO_CHAR(emaildate,'yyyy-mm-dd')=:emailDate ");
		}
		if (!citi.isEmpty()) {
			sql = sql.append(" AND location=:citi ");
		}
		if (!locality.isEmpty()) {
			sql = sql.append(" AND locality=:locality ");
		}

		return sql;
	} 

	@SuppressWarnings("unchecked")
	public List<OrderReport> orderDetailsReport(Map<String, String> params) {
		try {

			List<OrderReport> obs = new ArrayList<>();

			String ticketNumber = "";
			String status = "";
			String assignedTo = "";
			String stockist = "";
			String location = "";
			String invoiceNumbers = "";
			String manufacturer = "";
			String locality = "";
			String sapId = "";
			String invoiceDate = "";
			String deliverDate = "";
			String dispatchedDate = "";
			String emailDate = "";
			String isccdpod = "";
			String cusNoRef = "";

			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			if (params.get(StringIteration.ASSIGNEDTO) != null) {
				assignedTo = params.get(StringIteration.ASSIGNEDTO);
			}
			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME).trim();
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoiceNumbers = params.get(StringIteration.INVOICENUMBER).trim();
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketNumber = params.get(StringIteration.TICKET).trim();
			}
			if (params.get(StringIteration.INVOICEDATE) != null) {
				invoiceDate = params.get(StringIteration.INVOICEDATE);
			}
			if (params.get(StringIteration.DELIVERDDATE) != null) {
				deliverDate = params.get(StringIteration.DELIVERDDATE);
			}

			if (params.get(StringIteration.DISPATCHEDDATE) != null) {
				dispatchedDate = params.get(StringIteration.DISPATCHEDDATE);
			}
			if (params.get(StringIteration.EMAILDATE) != null) {
				emailDate = params.get(StringIteration.EMAILDATE);
			}

			if (params.get(StringIteration.CCDPOD) != null) {
				isccdpod = params.get(StringIteration.CCDPOD);
			}

			if (params.get(StringIteration.ISCUSTOMER) != null) {
				cusNoRef = params.get(StringIteration.ISCUSTOMER).trim();
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}
                int year=LocalDateTime.now().getYear();
			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {
				User us = op.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDetailManUser(us.getTenantId(),us.getUserId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDetailManUser(us.getTenantId(),us.getUserId());
					}

				} else if (us.getRoleName().equals(StringIteration.USER)) {
					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDetailManUser(us.getTenantId(),us.getHierarachyId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDetailManUser(us.getTenantId(),us.getHierarachyId());
					}
					

				} 
				else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					
					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDetailSuperAdminMonth(us.getTenantId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDetailSuperAdmin(us.getTenantId());
					}
					
				}
				
				if(isccdpod.equals("CCD")) {
					obs=obs.stream().filter(obj->obj.getCcdrequired()!=null&&obj.getCcdrequired().equals("true")).collect(Collectors.toList());
				}
				if(isccdpod.equals("POD")) {
					obs=obs.stream().filter(obj->obj.getPodrequired()!=null&&obj.getPodrequired().equals("true")).collect(Collectors.toList());
				}
				
				
				obs=getAllOrderDetailFilter(obs,ticketNumber,status,assignedTo,stockist,location,invoiceNumbers);
				obs=getAllOrderDetailFilters(obs,manufacturer,locality,sapId,invoiceDate,deliverDate,dispatchedDate,emailDate,cusNoRef);

			}

			return obs;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}
	
	DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

private List<OrderReport> getAllOrderDetailFilters(List<OrderReport> obs, String manufacturer, String locality,
			String sapId, String invoiceDate, String deliverDate, String dispatchedDate, String emailDate,
			String cusNoRef) {
	return obs.stream().filter(dto->manufacturer.isEmpty()||dto.getManufacturer()!=null&& manufacturer.equalsIgnoreCase(dto.getManufacturer()))
			.filter(dto ->locality.isEmpty()|| dto.getLocality()!=null&& locality.equalsIgnoreCase(dto.getLocality()))
			.filter(dto ->sapId.isEmpty()|| dto.getSapid()!=null&& sapId.equalsIgnoreCase(dto.getSapid()))
			.filter(dto ->invoiceDate.isEmpty()|| dto.getInvoicedate()!=null&& invoiceDate.equals(dto.getInvoicedate()))
			.filter(dto -> deliverDate.isEmpty() || dto.getDeliveredddate() != null &&deliverDate.equals(dto.getDeliveredddate()))
			.filter(dto ->emailDate.isEmpty()|| dto.getEmaildate()!=null&& emailDate.contains(dto.getEmaildate()))
			.filter(dto ->cusNoRef.isEmpty()|| dto.getCusrefno()!=null&& cusNoRef.equalsIgnoreCase(dto.getCusrefno()))
			.filter(dto -> dispatchedDate.isEmpty() || dto.getDispatcheddate() != null &&
            dispatchedDate.equals(formatTimestamp(dto.getDispatcheddate(), timestampFormatter, dateFormatter))).collect(Collectors.toList());
	}
private String formatTimestamp(String timestamp, DateTimeFormatter timestampFormatter, DateTimeFormatter dateFormatter) {
    LocalDateTime dateTime = LocalDateTime.parse(timestamp, timestampFormatter);
    return dateTime.toLocalDate().format(dateFormatter);
}

private List<OrderReport> getAllOrderDetailFilter(List<OrderReport> obs, String ticketNumber,String status,String assignedTo,String stockist,String location,String invoiceNumbers) {
	
		
		return obs.stream().filter(dto->ticketNumber.isEmpty()||dto.getTicketid()!=null&& ticketNumber.equalsIgnoreCase(dto.getTicketid()))
				.filter(dto ->status.isEmpty()|| dto.getStatus()!=null&& status.equalsIgnoreCase(dto.getStatus()))
				.filter(dto ->location.isEmpty()|| dto.getLocation()!=null&& location.equalsIgnoreCase(dto.getLocation()))
				.filter(dto ->assignedTo.isEmpty()|| dto.getAsssignedto()!=null&& assignedTo.equalsIgnoreCase(dto.getAsssignedto()))
				.filter(dto ->stockist.isEmpty()|| dto.getStockist()!=null&& stockist.equalsIgnoreCase(dto.getStockist()))
				.filter(dto ->invoiceNumbers.isEmpty()|| dto.getInvoicenumber()!=null&& invoiceNumbers.equalsIgnoreCase(dto.getInvoicenumber())).collect(Collectors.toList());
	}

		
	

	@SuppressWarnings("unchecked")
	public List<OrderReport> orderDeadlineReport(Map<String, String> params) {
		try {

			List<OrderReport> obs = new ArrayList<>();

			String ticketNumber = "";
			String status = "";
			String assignedTo = "";
			String stockist = "";
			String location = "";
			String invoiceNumbers = "";
			String manufacturer = "";
			String locality = "";
			String sapId = "";
			String invoiceDate = "";
			String deliverDate = "";
			String dispatchedDate = "";
			String emailDate = "";
			String isccdpod = "";
			String cusNoRef = "";

			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			if (params.get(StringIteration.ASSIGNEDTO) != null) {
				assignedTo = params.get(StringIteration.ASSIGNEDTO);
			}
			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME).trim();
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}
			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoiceNumbers = params.get(StringIteration.INVOICENUMBER).trim();
			}
			if (params.get(StringIteration.SAPID) != null) {
				sapId = params.get(StringIteration.SAPID).trim();
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketNumber = params.get(StringIteration.TICKET).trim();
			}
			if (params.get(StringIteration.INVOICEDATE) != null) {
				invoiceDate = params.get(StringIteration.INVOICEDATE);
			}
			if (params.get(StringIteration.DELIVERDDATE) != null) {
				deliverDate = params.get(StringIteration.DELIVERDDATE);
			}

			if (params.get(StringIteration.DISPATCHEDDATE) != null) {
				dispatchedDate = params.get(StringIteration.DISPATCHEDDATE);
			}
			if (params.get(StringIteration.EMAILDATE) != null) {
				emailDate = params.get(StringIteration.EMAILDATE);
			}
			if (params.get("ccdpod") != null) {
				isccdpod = params.get("ccdpod");
			}

			if (params.get("isCustomer") != null) {
				cusNoRef = params.get("isCustomer").trim();
			}

			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}
			int year=LocalDateTime.now().getYear();

			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if (op.isPresent()) {

				User us = op.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDeadlineManUser(us.getTenantId(),us.getUserId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDeadlineManUser(us.getTenantId(),us.getUserId());
					}

				} else if (us.getRoleName().equals(StringIteration.USER)) {
					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDeadlineManUser(us.getTenantId(),us.getHierarachyId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDeadlineManUser(us.getTenantId(),us.getHierarachyId());
					}
					

				} 
				
				else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					if(months!=0) {
						obs=ticketReportRepository.getAllOrderDeadlineSuperAdmin(us.getTenantId(),year,months);
					}else {
						obs=ticketReportRepository.getAllOrderDeadlineSuperAdmin(us.getTenantId());
					}	
					
				}
				if(isccdpod.equals("CCD")) {
					obs=obs.stream().filter(obj->obj.getCcdrequired()!=null&&obj.getCcdrequired().equals("true")).collect(Collectors.toList());
				}
				if(isccdpod.equals("POD")) {
					obs=obs.stream().filter(obj->obj.getPodrequired()!=null&&obj.getPodrequired().equals("true")).collect(Collectors.toList());
				}
				

				
				obs=getAllOrderDetailFilter(obs,ticketNumber,status,assignedTo,stockist,location,invoiceNumbers);
				obs=getAllOrderDetailFilters(obs,manufacturer,locality,sapId,invoiceDate,deliverDate,dispatchedDate,emailDate,cusNoRef);


			}
			return obs;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public List<ShipmentDetailReport> shipmentDetailsReport(Map<String, String> params) {
		try {

			List<ShipmentDetailReport> obs = new ArrayList<>();
			String location = "";
			String transporter = "";
			String locality = "";
			String ticketId = "";
			String manufacturer = "";
			String stockist = "";
			String status = "";
			String invoiceNumber = "";
			String isccdpod = "";

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.TRANSPORTERS) != null) {
				transporter = params.get(StringIteration.TRANSPORTERS);
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET).trim();
			}
			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}
			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoiceNumber = params.get(StringIteration.INVOICENUMBER).trim();
			}
			if (params.get(StringIteration.CCDPOD) != null) {
				isccdpod = params.get(StringIteration.CCDPOD);
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			String reportId = params.get("reportId");
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM shipmentdetail WHERE tenantid = :tenantId and userid=:userId  ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getUserId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();

				}

				else if (us.getRoleName().equals(StringIteration.USER)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM shipmentdetail WHERE tenantid = :tenantId and userid=:userId ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getHierarachyId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();
				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM shipmentdetailsuper WHERE tenantid = :tenantId ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();
				}

				return obs;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<>();
	}

	private Query querySetParameter(Query query, String status, String locality, String invoiceNumber) {

		if (!status.isEmpty()) {
			query = query.setParameter("status", status);
		}
		if (!locality.isEmpty()) {
			query = query.setParameter("locality", locality);
		}
		if (!invoiceNumber.isEmpty()) {
			query = query.setParameter("invoiceNumber", invoiceNumber);
		}

		return query;
	}

	private Query querySetParameter(Query query, String location, String transporter, String stockist,
			String manufacturer, String ticketId, int month) {
		int year = LocalDateTime.now().getYear();
		if (month != 0) {
			query.setParameter(StringIteration.MONTH, month);
			query.setParameter("year", year);

		}

		if (!location.isEmpty()) {
			query = query.setParameter("location", location);
		}
		if (!transporter.isEmpty()) {
			query = query.setParameter("transporter", transporter);
		}
		if (!stockist.isEmpty()) {
			query = query.setParameter("stockist", stockist);
		}
		if (!manufacturer.isEmpty()) {
			query = query.setParameter("manufacturer", manufacturer);
		}
		if (!ticketId.isEmpty()) {
			query = query.setParameter("ticketId", ticketId);
		}

		return query;
	}

	private StringBuilder shipemetUserBuilder(StringBuilder sql, String status, String locality, String invoiceNumber,
			int months, String isccdpod, String reportId) {
		if (!status.isEmpty()) {
			sql = sql.append("AND status=:status ");
		}
		if (!locality.isEmpty()) {
			sql = sql.append("AND locality=:locality ");
		}
		if (!invoiceNumber.isEmpty()) {
			sql = sql.append("AND inovicenumber=:invoiceNumber ");
		}

		if (months != 0 && reportId.equals("RP4017")) {
			sql = sql.append(
					" and deliverydate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND deliverydate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month') ");
		} else if (months != 0) {
			sql = sql.append(
					"  and  dispatcheddate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND dispatcheddate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month') ");

		}
		if (isccdpod.equals("POD")) {
			sql = sql.append(" and podrequired='true' ");
		}
		if (isccdpod.equals("CCD")) {
			sql = sql.append(" and ccdrequired='true' ");
		}

		return sql;
	}

	private StringBuilder shipemetUserBuilder(StringBuilder sql, String location, String transporter, String stockist,
			String manufacturer, String ticketId) {
		if (!location.isEmpty()) {
			sql = sql.append("AND location=:location ");
		}
		if (!transporter.isEmpty()) {
			sql = sql.append("AND transporter=:transporter ");
		}

		if (!stockist.isEmpty()) {
			sql = sql.append("AND stockist=:stockist ");
		}

		if (!manufacturer.isEmpty()) {
			sql = sql.append("AND manufacturer=:manufacturer ");
		}
		if (!ticketId.isEmpty()) {
			sql = sql.append("AND ticketid=:ticketId ");
		}

		return sql;
	}

	@SuppressWarnings("unchecked")
	public List<TicketReport> ticketNotClosed(Map<String, String> params) {
		try {

			List<TicketReport> obs = new ArrayList<>();
			String ticketId = "";
			String category = "";
			String emailDate = "";
			String assignedTo = "";
			String manufacturer = "";
			String status = "";
			String stockist = "";
			String location = "";
			String locality = "";

			if (params.get("emailDate") != null) {
				emailDate = params.get("emailDate");
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET);
			}
			if (params.get("category") != null) {
				category = params.get("category");
			}

			if (params.get("assignedTo") != null) {
				assignedTo = params.get("assignedTo");
			}
			if (params.get("status") != null) {
				status = params.get("status");
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
			if (u.isPresent()) {
				User us = u.get();

				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					StringBuilder sql = new StringBuilder(" SELECT * FROM ticketreports WHERE tenantid = :tenantId AND userid = :userId and deadline<days ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, location,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getUserId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							location, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();

				} else if (us.getRoleName().equals(StringIteration.USER)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreports WHERE tenantid = :tenantId AND userid = :userId AND deadline<days ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, location,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getHierarachyId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							location, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();

				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {

					StringBuilder sql = new StringBuilder(
							"SELECT * FROM ticketreportssuper WHERE tenantid = :tenantId AND deadline<days ");
					sql = ticketDetailsReportBuilder(sql, ticketId, category, assignedTo, stockist, emailDate, location,
							locality);
					sql = ticketDetailsReportBuilder1(sql, status, manufacturer, months);
					Query query = entityManager.createNativeQuery(sql.toString(), TicketReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query = ticketDetailsReportParameter(query, ticketId, category, assignedTo, stockist, emailDate,
							location, locality);
					query = ticketDetailsReportParameter(query, status, manufacturer, months);
					obs = query.getResultList();

				}
			}

			return obs;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	public List<TicketReportsDTO> blockedOrder(Map<String, String> params) {
		try {

			List<Object[]> ob = new ArrayList<>();

			String ticket = "";
			String manufacuturer = "";
			String stockists = "";
			String city = "";
			String assignedTo = "";
			String emailDate = "";
			String emailSubject = "";
			String locality = "";

			if (params.get(StringIteration.TICKET) != null) {
				ticket = params.get(StringIteration.TICKET).trim();
			}

			if (params.get("emailSubject") != null) {
				emailSubject = params.get("emailSubject").trim();
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockists = params.get(StringIteration.STOCKISTNAME);
			}
			if (params.get(StringIteration.LOCATION) != null) {
				city = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacuturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.EMAILDATE) != null) {
				emailDate = params.get(StringIteration.EMAILDATE);
			}

			if (params.get(StringIteration.ASSIGNEDTO) != null) {
				assignedTo = params.get(StringIteration.ASSIGNEDTO);
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
						ob = ticketReportRepository.blockedOrderMonth(us.getTenantId(), us.getUserId(), months);
					} else {
						ob = ticketReportRepository.blockedOrder(us.getTenantId(), us.getUserId());
					}

				}

				else if (us.getRoleName().equals(StringIteration.USER)) {
					if (months != 0) {
						ob = ticketReportRepository.blockedOrderMonth(us.getTenantId(), us.getHierarachyId(), months);
					} else {
						ob = ticketReportRepository.blockedOrder(us.getTenantId(), us.getHierarachyId());
					}
				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					if (months != 0) {
						ob = ticketReportRepository.blockedOrderSuperMonth(us.getTenantId(), months);
					} else {
						ob = ticketReportRepository.blockedOrderSuper(us.getTenantId());
					}
				}

			}
			List<TicketReportsDTO> tr = blockOrderDb(ob);
			tr = blockOrderDbFilter(tr, ticket, emailSubject, manufacuturer, stockists, emailDate, assignedTo);
			tr = blockOrderDbFilter(tr, city, locality);

			return tr;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	private List<TicketReportsDTO> blockOrderDbFilter(List<TicketReportsDTO> tr, String city, String locality) {
		if (!city.equals("")) {

			

				tr = tr.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(city))
						.collect(Collectors.toList());
			}
		
		if (!locality.equals("")) {
			tr = tr.stream().filter(obj -> obj.getLocality().equalsIgnoreCase(locality)).collect(Collectors.toList());
		}

		return tr;
	}

	private List<TicketReportsDTO> blockOrderDbFilter(List<TicketReportsDTO> tr, String ticket, String emailSubject,
			String manufacuturer, String stockists, String emailDate, String assignedTo) {

		if (!ticket.equals("")) {
			final String stk = ticket.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getTicketNumber().toLowerCase().contains(stk))
					.collect(Collectors.toList());

		}

		if (!emailSubject.equals("")) {
			System.err.println(emailSubject);
			final String stk = emailSubject.toLowerCase();
			tr = tr.stream().filter(obj -> obj.getEmailSubject().toLowerCase().contains(stk))
					.collect(Collectors.toList());

		}

		if (manufacuturer != null && !manufacuturer.isEmpty()) {
			final String man = manufacuturer;
			tr = tr.stream().filter(obj -> obj.getManufacturer().contains(man)).collect(Collectors.toList());
		}

		if (stockists != null && !stockists.isEmpty()) {
			final String stks = stockists;
			tr = tr.stream().filter(obj -> obj.getStockist().equalsIgnoreCase(stks)).collect(Collectors.toList());
		}
		if (emailDate != null && !emailDate.isEmpty()) {
			final String inv = emailDate;
			tr = tr.stream().filter(obj -> obj.getEmailDate().contains(inv)).collect(Collectors.toList());
		}
		if (!assignedTo.equals("")) {
			final String stk = assignedTo;
			tr = tr.stream().filter(obj -> obj.getAssignedTo().equalsIgnoreCase(stk)).collect(Collectors.toList());

		}

		return tr;
	}

	private List<TicketReportsDTO> blockOrderDb(List<Object[]> ob) {

		List<TicketReportsDTO> tr = new ArrayList<>();

		for (Object[] b : ob) {
			TicketReportsDTO td = new TicketReportsDTO();
			td.setTicketNumber(String.valueOf(b[0]));
			td.setManufacturer(String.valueOf(b[1]));
			td.setEmailSubject(String.valueOf(b[2]));
			td.setEmailDate(String.valueOf(b[3]));
			if (b[4] != null) {
				td.setStockist(String.valueOf(b[4]));
			} else {
				td.setStockist(" -");
			}
			if (b[5] != null) {
				Optional<String> city = cityReportRepository.findByCityCode(String.valueOf(b[5]));
				if (city.isPresent()) {
					td.setLocation(city.get());
				}
			} else {
				td.setLocation(" -");
			}
			td.setAssignedTo(String.valueOf(b[6]));
			td.setDaysTaken(String.valueOf(b[7]));
			td.setStatus(String.valueOf(b[8]));
			if (b[9] != null) {
				td.setRemarks(String.valueOf(b[9]));
			}
			if (b[10] != null) {
				td.setLocality(listvalue.findByCodeAndName(String.valueOf(b[10])));
			} else {
				td.setLocality("-");
			}
			tr.add(td);
		}
		return tr;
	}

	@SuppressWarnings("unchecked")
	public List<ShipmentDetailReport> dispacthedNotDelivered(Map<String, String> params) {
		try {
			List<ShipmentDetailReport> obs = new ArrayList<>();
			String location = "";
			String transporter = "";
			String isccdpod = "";
			String locality = "";
			String ticketId = "";
			String manufacturer = "";
			String stockist = "";
			String status = "";
			String invoiceNumber = "";

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.TRANSPORTERS) != null) {
				transporter = params.get(StringIteration.TRANSPORTERS);
			}

			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET).trim();
			}
			if (params.get("invoiceNumber") != null) {
				invoiceNumber = params.get("invoiceNumber").trim();
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}
			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS);
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}
			if (params.get(StringIteration.CCDPOD) != null) {
				isccdpod = params.get(StringIteration.CCDPOD);
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			String reportId = params.get("reportId");
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());

			if (user.isPresent()) {
				User us = user.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetail where tenantid=:tenantId and userid=:userId  and  CAST (tlt AS INTEGER)<daystaken and status='DISPATCHED' ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getUserId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();

				}
				if (us.getRoleName().equals(StringIteration.USER)) {
					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetail where tenantid=:tenantId and userid=:userId  and  CAST (tlt AS INTEGER)<daystaken and status='DISPATCHED' ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getHierarachyId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();
				}
				if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetailsuper where tenantid=:tenantId  and  CAST (tlt AS INTEGER)<daystaken and status='DISPATCHED' ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();

				}

			}

			return obs;

		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	@Override
	public List<Ticket> findAll() {

		return Collections.emptyList();
	}

	@Override
	public Ticket findById(String id) {

		return null;
	}

	@Override
	public Result<Ticket> create(Ticket t) {

		return null;
	}

	@Override
	public Result<Ticket> update(Ticket t) {

		return null;
	}

	@Override
	public Result<Ticket> delete(String id) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ShipmentDetailReport> delivered(Map<String, String> params) {

		List<ShipmentDetailReport> obs = new ArrayList<>();
		try {
			String location = "";
			String transporter = "";
			String isccdpod = "";
			String locality = "";
			String ticketId = "";
			String manufacturer = "";
			String stockist = "";
			String invoiceNumber = "";
			String status = "";
			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.TRANSPORTERS) != null) {
				transporter = params.get(StringIteration.TRANSPORTERS);
			}
			if (params.get(StringIteration.CCDPOD) != null) {
				isccdpod = params.get(StringIteration.CCDPOD);
			}
			if (params.get(StringIteration.IS_LOCALITY) != null) {
				locality = params.get(StringIteration.IS_LOCALITY);
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET).trim();
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}

			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoiceNumber = params.get(StringIteration.INVOICENUMBER).trim();
			}
			if (params.get(StringIteration.STATUS) != null) {
				status = params.get(StringIteration.STATUS).trim();
			}

			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}

			String reportId = params.get("reportId");

			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {

					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetail where tenantid=:tenantId and userid=:userId  and  CAST (tlt AS INTEGER)<daystaken and status IN ('DELIVERED','POD RECEIVED') ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getUserId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();

				}
				if (us.getRoleName().equals(StringIteration.USER)) {
					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetail where tenantid=:tenantId and userid=:userId  and  CAST (tlt AS INTEGER)<daystaken and  status IN ('DELIVERED','POD RECEIVED') ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query.setParameter(StringIteration.USERID, us.getHierarachyId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();
				}
				if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					StringBuilder sql = new StringBuilder(
							"select * from shipmentdetailsuper where tenantid=:tenantId  and  CAST (tlt AS INTEGER)<daystaken and status IN ('DELIVERED','POD RECEIVED') ");
					sql = shipemetUserBuilder(sql, location, transporter, stockist, manufacturer, ticketId);
					sql = shipemetUserBuilder(sql, status, locality, invoiceNumber, months, isccdpod, reportId);
					Query query = entityManager.createNativeQuery(sql.toString(), ShipmentDetailReport.class);
					query.setParameter(StringIteration.TENANTID, us.getTenantId());
					query = querySetParameter(query, location, transporter, stockist, manufacturer, ticketId, months);
					query = querySetParameter(query, status, locality, invoiceNumber);
					obs = query.getResultList();

				}

			}

			return obs;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();

	}

	public List<TicketDTO> findUserListReport() {
		List<TicketDTO> ticket = new ArrayList<>();
		try {
			List<Object[]> list = new ArrayList<>();
			Optional<User> user = userRepository.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();
				if (us.getRoleName().equals(StringIteration.MANAGER)) {
					list = ticketReportRepository.findUserListReport(us.getTenantId(), us.getUserId());
				} else if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
					list = ticketReportRepository.findUserListReportSuper(us.getTenantId());
				}
				for (Object[] ob : list) {
					TicketDTO tick = new TicketDTO();
					tick.setAssignedTo(String.valueOf(ob[0]));
					tick.setFirstName(String.valueOf(ob[1]));
					ticket.add(tick);
				}

				return ticket;
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ticket;
	}

	public List<TicketDTO> lrNotreceived(Map<String, String> params) {
		try {

			String location = "";
			String transporter = "";
			String ticketId = "";
			String manufacturer = "";
			String stockist = "";
			String invoiceNumber = "";
			String deliverdDate = "";
			String islocality = "";

			if (params.get(StringIteration.LOCATION) != null) {
				location = params.get(StringIteration.LOCATION);
			}

			if (params.get(StringIteration.TRANSPORTERS) != null) {
				transporter = params.get(StringIteration.TRANSPORTERS);
			}

			if (params.get(StringIteration.TICKET) != null) {
				ticketId = params.get(StringIteration.TICKET).trim();
			}

			if (params.get(StringIteration.MANUFACTURER) != null) {
				manufacturer = params.get(StringIteration.MANUFACTURER);
			}

			if (params.get(StringIteration.STOCKISTNAME) != null) {
				stockist = params.get(StringIteration.STOCKISTNAME);
			}

			if (params.get("deliverdDate") != null) {
				deliverdDate = params.get("deliverdDate");
			}
			if (params.get(StringIteration.INVOICENUMBER) != null) {
				invoiceNumber = params.get(StringIteration.INVOICENUMBER).trim();
			}

			if (params.get("isLocality") != null) {
				islocality = params.get("isLocality");
			}
			int months = 0;
			if (params.get("selectedMonth") != null) {
				months = Integer.parseInt(params.get("selectedMonth"));
			}
			List<Object[]> ob = new ArrayList<>();

			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if (us.isPresent()) {
				if (us.get().getRoleName().equals(StringIteration.MANAGER)) {
					if (months != 0) {
						ob = ticketReportRepository.lrNotreceivedMonth(us.get().getTenantId(), us.get().getUserId(),
								months);
					} else {
						ob = ticketReportRepository.lrNotreceived(us.get().getTenantId(), us.get().getUserId());
					}

				} else if (us.get().getRoleName().equals(StringIteration.USER)) {
					if (months != 0) {
						ob = ticketReportRepository.lrNotreceivedMonth(us.get().getTenantId(),
								us.get().getHierarachyId(), months);
					} else {
						ob = ticketReportRepository.lrNotreceived(us.get().getTenantId(), us.get().getHierarachyId());
					}

				} else if (us.get().getRoleName().equals(StringIteration.SUPERADMIN)) {
					if (months != 0) {
						ob = ticketReportRepository.lrNotreceivedSuperMonth(us.get().getTenantId(), months);
					} else {
						ob = ticketReportRepository.lrNotreceivedSuper(us.get().getTenantId());
					}

				}

			}
			List<TicketDTO> tick = lrNotreceivedOb(ob);
			tick = lrNotreceivedObFilter(tick, location, transporter, stockist, invoiceNumber, ticketId, islocality);

			if (!manufacturer.equals("")) {
				final String local = manufacturer;
				tick = tick.stream().filter(obj -> obj.getManufacturer().equalsIgnoreCase(local))
						.collect(Collectors.toList());
			}
			if (!deliverdDate.equals("")) {
				final String local = deliverdDate;
				tick = tick.stream().filter(obj -> obj.getDeliveryDate().contains(local)).collect(Collectors.toList());
			}

			return tick;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private List<TicketDTO> lrNotreceivedObFilter(List<TicketDTO> tick, String location, String transporter,
			String stockist, String invoiceNumber, String ticketId, String local) {
		if (!location.equals("")) {


				tick = tick.stream().filter(obj -> obj.getLocation().equalsIgnoreCase(location))
						.collect(Collectors.toList());
			
		}

		if (transporter != null && !transporter.isEmpty()) {
			final String trans = transporter;
			tick = tick.stream().filter(obj -> obj.getTransporter().equalsIgnoreCase(trans))
					.collect(Collectors.toList());

		}

		if (local != null && !local.isEmpty()) {
			final String trans = local;
			tick = tick.stream().filter(obj -> obj.getLocal().equalsIgnoreCase(trans)).collect(Collectors.toList());

		}

		if (stockist != null && !stockist.isEmpty()) {
			final String trans = stockist;
			tick = tick.stream().filter(obj -> obj.getStockistName().equalsIgnoreCase(trans))
					.collect(Collectors.toList());

		}
		if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
			final String trans = invoiceNumber.toLowerCase();
			tick = tick.stream().filter(obj -> obj.getInvoiceNumber().toLowerCase().contains(trans))
					.collect(Collectors.toList());

		}

		if (!ticketId.equals("")) {
			final String locals = ticketId.toLowerCase();
			tick = tick.stream().filter(obj -> obj.getTicketId().toLowerCase().equals(locals))
					.collect(Collectors.toList());
		}
		return tick;
	}

	private List<TicketDTO> lrNotreceivedOb(List<Object[]> ob) {
		List<TicketDTO> tick = new ArrayList<>();
		for (Object[] b : ob) {
			TicketDTO t = new TicketDTO();
			t.setInvoiceNumber(String.valueOf(b[0]));
			t.setTicketId(String.valueOf(b[1]));
			t.setStockistName(String.valueOf(b[2]));
			if (b[3] != null) {
				Optional<String> ct = cityReportRepository.findByCityCode(String.valueOf(b[3]));
				if (ct.isPresent()) {
					t.setLocation(ct.get());
				}
			} else {
				t.setLocation(" -");
			}
			t.setManufacturer(String.valueOf(b[4]));
			if (b[5] != null) {

				Optional<Transport> trans = transportRepository.findById(String.valueOf(b[5]));
				if (trans.isPresent()) {
					t.setTransporter(trans.get().getTransportName());
				}
			} else {
				t.setTransporter("  -");
			}
			t.setDeliveryDate(String.valueOf(b[6]));

			t.setLocal(listvalue.findByCodeAndName(String.valueOf(b[7])));
			tick.add(t);
		}

		return tick;
	}

	public static int getMonthFromDate(Date date) {
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.getMonthValue();
	}

}
