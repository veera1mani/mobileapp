package com.healthtraze.etraze.api.dashboard.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.dto.DashBordCount;
import com.healthtraze.etraze.api.masters.dto.ManagerCountDTO;
import com.healthtraze.etraze.api.masters.dto.ManufacturerDto;
import com.healthtraze.etraze.api.masters.dto.OrderSummary;
import com.healthtraze.etraze.api.masters.dto.TenantDashBoardDto;
import com.healthtraze.etraze.api.masters.model.DashboardChart;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.repository.ClaimRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TicketReportRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.report.model.OrderReport;
import com.healthtraze.etraze.api.report.model.ShipmentDetailReport;
import com.healthtraze.etraze.api.report.model.TicketReport;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

import lombok.Data;

/**
 *
 * DashboardController - It is used for controlling the APIs for Dashboard like
 * - Admin, Doctor, Channel Partner
 *
 *
 * @author Kaliyugan
 *
 */
@Component
public class DashboardService {
	private Logger logger = LogManager.getLogger(DashboardService.class);

	/**
	 *
	 *
	 *
	 *
	 *
	 * @return
	 */

	private final UserRepository userRepo;

	private final TenantRepository tenantRepository;

	private final ManufacturerRepository manufacturerRepo;

	private final StockistRepository stockistRepository;

	private final TransportRepository transportRepository;

	private final TicketReportRepository ticketRepository;

	private final TenantManufactureRepository tenantManufactureRepository;
	private final ClaimRepository claimRepo;
	private final ReturnRepository returnRepository; 
	 @PersistenceContext
	    private EntityManager entityManager;
	 
	 

	@Autowired
	public DashboardService(UserRepository userRepo, TenantRepository tenantRepository,
			ManufacturerRepository manufacturerRepo, StockistRepository stockistRepository,
			TransportRepository transportRepository, TicketReportRepository ticketRepository,
			TenantManufactureRepository tenantManufactureRepository,ClaimRepository claimRepo,ReturnRepository returnRepository,EntityManager entityManager) {

		this.userRepo = userRepo;
		this.tenantRepository = tenantRepository;
		this.manufacturerRepo = manufacturerRepo;
		this.stockistRepository = stockistRepository;
		this.transportRepository = transportRepository;
		this.ticketRepository = ticketRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.claimRepo=claimRepo;
		this.returnRepository=returnRepository;
		this.entityManager=entityManager;
	}

	public Map<String, Object> updateChartsList() {
		return new HashMap<>();
		
	}

	public Map<String, Integer> getCountOftheGivenList(List<String> list) {
		Map<String, Integer> map = new HashMap<>();
		Set<String> unique = new HashSet<>(list);
		for (String key : unique) {
			map.put(key, Collections.frequency(list, key));
		}

		return map;
	}

	public DashBordCount finddashboardCount(String tenantId) {
		try {

			List<Object[]> list;
			if (!tenantId.isEmpty()) {
				list = manufacturerRepo.finddashboardCountTenantId(tenantId);
			} else {
				list = manufacturerRepo.finddashboardCount();
			}

			DashBordCount count = new DashBordCount();
			for (Object[] ob : list) {
				count.setTenant((BigInteger) ob[0]);
				count.setUsers((BigInteger) ob[1]);
				count.setStockist((BigInteger) ob[2]);
				count.setTransporter((BigInteger) ob[3]);
				count.setManufacturer((BigInteger) ob[4]);
				count.setOrderCount((BigInteger) ob[5]);
				count.setReturnCount((BigInteger) ob[6]);
			}

			return count;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public DashboardChart finddashboardChart() {
		try {
			DashboardChart chart = new DashboardChart();

			List<Tenant> tenants = tenantRepository.findAll();
			String[] name = new String[tenants.size()];
			int i = 0;
			for (Tenant tenant : tenants) {

				name[i] = tenant.getTenantName();
				i++;
			}
			chart.setCategories(name);
			return chart;

		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public Result<HashMap<String, Object>> dashBoardOrders(String assignedTo) {
		Result<HashMap<String, Object>> result = new Result<>();
		Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
		if (user.isPresent()) {
			User us = user.get();
			try {

				HashMap<String, Object> map = new HashMap<>();
				OrderSummary order = new OrderSummary();
				ManagerCountDTO count = new ManagerCountDTO();
				if (us.getRoleName().equals("MANAGER")) {

					count.setRecievdTicket(
							ticketRepository.findByOtherCount(us.getTenantId(), us.getUserId(), assignedTo).size());
					count.setCloseTicket(ticketRepository
							.findByOtherCountClosed(us.getTenantId(), us.getUserId(), assignedTo).size());
					count.setOtherDeadline(ticketRepository
							.findByOtherCounts(us.getTenantId(), us.getUserId(), assignedTo).size());
					

					order.setAssigned(
							ticketRepository.findByOrderCount(us.getTenantId(), us.getUserId(), assignedTo).size());
					order.setDeliverd(ticketRepository
							.findByOrderCountDelivered(us.getTenantId(), us.getUserId(), assignedTo).size());
					count.setDeadlineOrder(ticketRepository
							.findByOrderCountDaadline(us.getTenantId(), us.getUserId(), assignedTo).size());

				}

				map.put("oredr", order);
				map.put("count", count);
				result.setCode("0000");
				result.setMessage("sucess");
				result.setData(map);

				return result;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public Map<String, Object> finddashboardCharts() {
		Map<String, Object> map = new HashMap<>();
		try {

			List<String> categories = new ArrayList<>();
			List<Integer> series = new ArrayList<>();
			List<Integer> stockistSeries = new ArrayList<>();
			List<Integer> transportSeries = new ArrayList<>();
			List<Integer> manufactureseries = new ArrayList<>();

			List<Tenant> t = tenantRepository.findAll();
			for (Tenant tn : t) {
				categories.add(tn.getTenantName());
				List<User> us = userRepo.findByTenantId(tn.getTenantId());
				series.add(us.size());
				List<Stockist> stk = stockistRepository.findStockistByTenantId(tn.getTenantId());
				stockistSeries.add(stk.size());
				List<Transport> trans = transportRepository.findByTenantId(tn.getTenantId());
				transportSeries.add(trans.size());
				List<TenantManufacture> manu = tenantManufactureRepository
						.getTenantManufacturesByIdByStatus(tn.getTenantId());
				manufactureseries.add(manu.size());

			}

			map.put("categories", categories);
			map.put("series", series);
			map.put("stockistseries", stockistSeries);
			map.put("transportseries", transportSeries);
			map.put("maufactureseries", manufactureseries);

		} catch (Exception e) {
			logger.error(e);
		}
		return map;
	}
   @Transactional
	public ManagerCountDTO findTicketManagerCount() {
		try {
			ManagerCountDTO count = new ManagerCountDTO();
			Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {

				List<Object[]> list = manufacturerRepo.findTicketManagerCount(user.get().getTenantId(),
						user.get().getUserId());

//				List<Object[]> li = manufacturerRepo.getReturnsCountByManager(user.get().getTenantId(),
//						user.get().getUserId());

				List<Object[]> listDelivery = manufacturerRepo.findDeliveryCounts(user.get().getTenantId(),
						user.get().getUserId());

//				for (Object[] ob : li) {
//					count.setReceivedreturns((BigInteger) ob[0]);
//					//count.setCncreated((BigInteger) ob[1]);
//					//count.setTotalreturns((BigInteger) ob[2]);
//					//count.setClaimsdeadlinemissed((BigInteger) ob[3]);
//				}

				  				
				count.setTotalreturns(claimRepo.returnsDetatilReport(user.get().getTenantId(),user.get().getUserId()).size());
				
				count.setClaimsdeadlinemissed(returnRepository.claimsNotClosed(user.get().getTenantId(),user.get().getUserId()).size());
				count.setCncreated(claimRepo.returnsDetatilReportCnCreated(user.get().getTenantId(),user.get().getUserId(),"").size());;
			
				for (Object[] ob : list) {
					
					count.setClosed((BigInteger) ob[2]);
					count.setTotaltickets((BigInteger) ob[0]);
					count.setOverdueticketdeadlines((BigInteger) ob[1]);
					count.setAssigned((BigInteger) ob[4]);
					count.setTotalorder((BigInteger) ob[3]);
					count.setOverdueorderdeadlines((BigInteger) ob[5]);
					count.setReceived((BigInteger) ob[6]);
				}
				
				
				//count.setOverdueorderdeadlines((BigInteger) ob[5]);
				
					count.setOverdueorderdeadlines(BigInteger.valueOf(ticketRepository.getAllOrderDeadlineManUser(user.get().getTenantId(), user.get().getUserId()).size()));
	                
				try {
					Query orderMissedQuery = entityManager.createNativeQuery("SELECT * FROM ticketreports WHERE tenantid =:tenantId and userid=:userId and days > deadline ", TicketReport.class);
		            orderMissedQuery.setParameter("tenantId",user.get().getTenantId());
		            orderMissedQuery.setParameter("userId", user.get().getUserId());
	                List<TicketReport> orderMissedList = orderMissedQuery.getResultList();
		            count.setOverdueticketdeadlines(BigInteger.valueOf(orderMissedList.size()));
					
				}catch(Exception e) {
					e.printStackTrace();
				}

				
				

				
				for (Object[] ob : listDelivery) {
					count.setDelivered((BigInteger) ob[0]);
					count.setDispatched((BigInteger) ob[1]);
					//count.setOverduedelivery((BigInteger) ob[2]);

				}
				try {
					Query shipment = entityManager.createNativeQuery("select * from shipmentdetail where tenantid=:tenantId and userid=:userId  and  CAST (tlt AS INTEGER)<daystaken and status IN ('DELIVERED','POD RECEIVED')", ShipmentDetailReport.class);
					shipment.setParameter("tenantId",user.get().getTenantId());
					shipment.setParameter("userId", user.get().getUserId());
	                List<ShipmentDetailReport> shipmentList = shipment.getResultList();
		            count.setOverduedelivery(BigInteger.valueOf(shipmentList.size()));
					
				}catch(Exception e) {
					e.printStackTrace();
				}


			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	public HashMap<String, Object> findTenantCount(String id, Integer months) {
		HashMap<String, Object> map = new HashMap<>();

		try {
			Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();
				TenantDashBoardDto count = new TenantDashBoardDto();
                   int year=LocalDateTime.now().getYear();
				Date date = new Date();

				if (months == null) {
					months = getMonthFromDate(date);
				}
				String tenantId = us.getTenantId();
                List<Object[]> rawData = ticketRepository.ticketCategory(tenantId, id, months);
				List<Object[]> processedData = new ArrayList<>();
				for (Object[] row : rawData) {
					String name = (String) row[0];
					BigInteger total = (BigInteger) row[1];
					Object[] processedRow = new Object[] { name, total };
					processedData.add(processedRow);
				}

				List<Object[]> ob = ticketRepository.paymentNotLocal(tenantId, id);
				Object[] obPaymentNotLocal = ob.get(0);
				count.setPaymentNotLocal((BigInteger) obPaymentNotLocal[0]);

				List<Object[]> obs = ticketRepository.paymentNotOutstation(tenantId, id);

				Object[] paymentNotOutstation = obs.get(0);
				count.setPaymentNotOutstaion((BigInteger) paymentNotOutstation[0]);

//				List<Object[]> obDelivered = ticketRepository.findByTwoAndTltmissedDelivered(tenantId,months, id);
//
//				Object[] obDeliveredTwoTlt = obDelivered.get(0);
				//count.setCcdNotDeliverd((BigInteger) obDeliveredTwoTlt[0]);
				//count.setDeliveredTlt((BigInteger) obDeliveredTwoTlt[1]);
				
				
				  
				  
				  
//				   Query ccdorder=entityManager.createNativeQuery("select * from orderdetailreportsuper where tenantid=:tenantId and ccdrequired='true'  and status ='DELIVERED' and  CAST(ordermonth AS INTEGER) =:month and CAST(ordermonth AS INTEGER) = 2024  ", OrderReport.class);
//				   ccdorder.setParameter(StringIteration.TENANTID, us.getTenantId());
//				   ccdorder.setParameter(StringIteration.MONTH, months);
		  
				  
				  //count.setCcdNotDeliverd(BigInteger.valueOf(ccdorder.getResultList().size()));
				
                Query query=entityManager.createNativeQuery("select * from shipmentdetailsuper where tenantid=:tenantId  and  CAST (tlt AS INTEGER)<daystaken and status IN ('DELIVERED','POD RECEIVED') and deliverydate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND deliverydate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month')  and manufacturerid ILIKE :id ", ShipmentDetailReport.class);
				query.setParameter(StringIteration.TENANTID, us.getTenantId());
				query.setParameter("year", year);
				 query.setParameter(StringIteration.MONTH, months);
				 query.setParameter("id", "%"+id+"%");
				  count.setDeliveredTlt(BigInteger.valueOf(query.getResultList().size()));
				  count.setCcdNotDeliverd(BigInteger.valueOf(ticketRepository.getAllOrderDeadlineSuperAdminDashCCD(tenantId, year, months, id).size()));
					  
				 System.err.println(months+year);
				  
				  
				  System.err.println("count"+count.getCcdNotDeliverd());
				
				map.put("count", count);
				map.put("processedData", processedData);

				return map;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static int getMonthFromDate(Date date) {
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.getMonthValue();
	}
    
	public Map<String, Object> dashboardCarton(String transport, String manufacturerId, Integer months) {

		try {
			int year =LocalDateTime.now().getYear();
			Optional<User> us = userRepo.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
//				LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
//				LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
//				LocalDateTime previounstartDate = CommonUtil.previousFinancialYeartartDate(LocalDateTime.now());
//				LocalDateTime previousendDate = CommonUtil.previousFinancialYearEndDate(LocalDateTime.now());
//
				//List<Object[]> deadlineDelivery = new ArrayList<>();

				List<Object[]> transportLocal = new ArrayList<>();
				List<Object[]> transportOutstation = new ArrayList<>();

//				List<Object[]> deliverySummary = ticketRepository.deliverySummary(us.get().getTenantId(), transport,
//						manufacturerId, startDate, endDate);
//				List<Object[]> deliverySummaryLast = ticketRepository.deliverySummary(us.get().getTenantId(), transport,
//						manufacturerId, previounstartDate, previousendDate);

				HashMap<String, Object> map = new HashMap<>();
				Date date = new Date();
				if (months == null) {
					months = getMonthFromDate(date);
				}

				TenantDashBoardDto ten = new TenantDashBoardDto();
				if (transport.isEmpty()) {

					transportLocal = ticketRepository.deliveryByTransporterLocal(us.get().getTenantId(), months,
							manufacturerId,year);
					transportOutstation = ticketRepository.deliveryByTransporterOutstation(us.get().getTenantId(),
							months, manufacturerId,year);

//					deadlineDelivery = ticketRepository.deadlineDelivery(us.get().getTenantId(), manufacturerId,
//							months);

				} else if (!transport.isEmpty()) {

					transportLocal = ticketRepository.transporterLocalTansp(us.get().getTenantId(), transport, months,
							manufacturerId,year);
					transportOutstation = ticketRepository.transporterOutstationTransp(us.get().getTenantId(),
							transport, months, manufacturerId,year);

//					deadlineDelivery = ticketRepository.deadlineDeliveryTransID(us.get().getTenantId(), manufacturerId,
//							transport, months);

				}
				Object[] tranlocal = transportLocal.get(0);
				ten.setCartonsLocal((BigInteger) tranlocal[0]);
				ten.setDisaptchedLocal((BigInteger) tranlocal[1]);

				Object[] tranosn = transportOutstation.get(0);

				ten.setCartonsOutstation((BigInteger) tranosn[0]);
				ten.setDispatchedOutstaion((BigInteger) tranosn[1]);

//				Object[] deadline = deadlineDelivery.get(0);
//				ten.setDeadlineCartonCount((BigInteger) deadline[0]);
//				ten.setDeadlineInvoice((BigInteger) deadline[1]);

//				Object[] deliverySum = deliverySummary.get(0);
//				ten.setdJan((BigDecimal) deliverySum[0]);
//				ten.setdFeb((BigDecimal) deliverySum[1]);
//				ten.setdMar((BigDecimal) deliverySum[2]);
//				ten.setdApr((BigDecimal) deliverySum[3]);
//				ten.setdMay((BigDecimal) deliverySum[4]);
//				ten.setdJun((BigDecimal) deliverySum[5]);
//				ten.setdJul((BigDecimal) deliverySum[6]);
//				ten.setdAug((BigDecimal) deliverySum[7]);
//				ten.setdSep((BigDecimal) deliverySum[8]);
//				ten.setdOct((BigDecimal) deliverySum[9]);
//				ten.setdNov((BigDecimal) deliverySum[10]);
//				ten.setdDec((BigDecimal) deliverySum[11]);
//
//				Object[] deliverySumLast = deliverySummaryLast.get(0);
//				ten.setdJanl((BigDecimal) deliverySumLast[0]);
//				ten.setdFebl((BigDecimal) deliverySumLast[1]);
//				ten.setdMarl((BigDecimal) deliverySumLast[2]);
//				ten.setdAprl((BigDecimal) deliverySumLast[3]);
//				ten.setdMayl((BigDecimal) deliverySumLast[4]);
//				ten.setdJunl((BigDecimal) deliverySumLast[5]);
//				ten.setdJull((BigDecimal) deliverySumLast[6]);
//				ten.setdAugl((BigDecimal) deliverySumLast[7]);
//				ten.setdSepl((BigDecimal) deliverySumLast[8]);
//				ten.setdOctl((BigDecimal) deliverySumLast[9]);
//				ten.setdNovl((BigDecimal) deliverySumLast[10]);
//				ten.setdDecl((BigDecimal) deliverySumLast[11]);

				map.put("ten", ten);
				return map;

			}
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptyMap();
	}

	public DashBordCount findtenantdetailsCount(String tenantId) {

		List<Object[]> list = manufacturerRepo.findTenantDetailsCount(tenantId);
		DashBordCount count = new DashBordCount();
		for (Object[] ob : list) {
			count.setTenantUser((BigInteger) ob[0]);
			count.setTenantManager((BigInteger) ob[1]);
			count.setTenantStockist((BigInteger) ob[2]);
			count.setTenantTransport((BigInteger) ob[3]);
		}
		return count;
	}

	@Data
	class AssetWithDetails {
		private String assetId;
		private String description;
	}
        
	public TenantDashBoardDto findOrderDashboard(String id, Integer month) {
		try {
			TenantDashBoardDto count = new TenantDashBoardDto();
			Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
				LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
				LocalDateTime previounstartDate = CommonUtil.previousFinancialYeartartDate(LocalDateTime.now());
				LocalDateTime previousendDate = CommonUtil.previousFinancialYearEndDate(LocalDateTime.now());
				Date date = new Date();

				if (month == null) {
				
					month = getMonthFromDate(date);
					
				}
				int year=LocalDateTime.now().getYear();
				String tenantId = us.getTenantId();
				count.setOrderTotal(ticketRepository.orderDetailsReportSuperMonthDash(tenantId, id, month,year).size());

				count.setBlocked(ticketRepository.blockedOrderSuperMonthDashBoard(tenantId, id, month,year).size());
//				System.err.println("month"+month);
//				Query orderCompleted = entityManager.createNativeQuery("SELECT * FROM orderdetailreport  WHERE tenantid =:tenantId AND status IN ('DELIVERED','POD RECEIVED') and manufacturerid  ILIKE :manufacturerId  AND ordermonth=:month and orderyear =2024 ",OrderReport.class);
//					orderCompleted.setParameter("tenantId", user.get().getTenantId());
//					orderCompleted.setParameter("manufacturerId","%"+id+"%"); // Add this line
//					orderCompleted.setParameter("month", month);

					//List<OrderReport> orderMissedList = orderCompleted.getResultList();
					//count.setCompletedOrder(orderMissedList.size());

		     		count.setCompletedOrder(ticketRepository.deliveredDashBoard(tenantId, "%"+id+"%", month,year).size());

				List<Object[]> invlocal = ticketRepository.invoiceAmountLocal(tenantId, id, month,year);
				List<Object[]> invOutstation = ticketRepository.invoiceAmountOutstation(tenantId, id, month,year);
				Object[] invoiceData = invlocal.get(0);
				count.setInvoiceAmountLocal((BigDecimal) invoiceData[0]);
				Object[] invoice = invOutstation.get(0);
				count.setInvoiceAmountOutstation((BigDecimal) invoice[0]);
				List<Object[]> localStockist = ticketRepository.localStockist(tenantId, id);
				List<Object[]> outStionStockist = ticketRepository.outStationStockist(tenantId, id);
				Object[] outStaionStk = outStionStockist.get(0);
				count.setOutStaionStockist((BigInteger) outStaionStk[0]);
				Object[] localStk = localStockist.get(0);
				count.setLocalStockist((BigInteger) localStk[0]);

				List<Object[]> salesInvoice = ticketRepository.invoiceSalesCurrent(tenantId, id, startDate, endDate);
				Object[] inv = salesInvoice.get(0);

				count.setJanI((BigDecimal) inv[0]);
				count.setFebI((BigDecimal) inv[1]);
				count.setMarI((BigDecimal) inv[2]);
				count.setAprI((BigDecimal) inv[3]);
				count.setMayI((BigDecimal) inv[4]);
				count.setJunI((BigDecimal) inv[5]);
				count.setJulyI((BigDecimal) inv[6]);
				count.setAugI((BigDecimal) inv[7]);
				count.setSepI((BigDecimal) inv[8]);
				count.setOctI((BigDecimal) inv[9]);
				count.setNovI((BigDecimal) inv[10]);
				count.setDecI((BigDecimal) inv[11]);

				List<Object[]> salesInvoiceLast = ticketRepository.invoiceSalesCurrent(tenantId, id, previounstartDate,
						previousendDate);
				Object[] linv = salesInvoiceLast.get(0);

				count.setLjanI((BigDecimal) linv[0]);
				count.setLfebI((BigDecimal) linv[1]);
				count.setLmarI((BigDecimal) linv[2]);
				count.setLaprI((BigDecimal) linv[3]);
				count.setLmayI((BigDecimal) linv[4]);
				count.setLjunI((BigDecimal) linv[5]);
				count.setLjulyI((BigDecimal) linv[6]);
				count.setLaugI((BigDecimal) linv[7]);
				count.setLsepI((BigDecimal) linv[8]);
				count.setLoctI((BigDecimal) linv[9]);
				count.setLnovI((BigDecimal) linv[10]);
				count.setLdecI((BigDecimal) linv[11]);

			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public TenantDashBoardDto findchequeDashBoard(String id, Integer month) {
		try {
			TenantDashBoardDto count = new TenantDashBoardDto();
			Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
				LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
				LocalDateTime previounstartDate = CommonUtil.previousFinancialYeartartDate(LocalDateTime.now());
				LocalDateTime previousendDate = CommonUtil.previousFinancialYearEndDate(LocalDateTime.now());
				Date date = new Date();

				if (month == null) {
					month = getMonthFromDate(date);
				}

				String tenantId = us.getTenantId();
				List<Object[]> advanceSecurity = ticketRepository.advanceSecurity(tenantId,id, month);
				List<Object[]> outward = ticketRepository.outWard(tenantId, id, month);
				List<Object[]> codDelivery = ticketRepository.codReceivedDelivery(tenantId, id, month);
				List<Object[]> codReceive = ticketRepository.codReceivedCheque(tenantId, id, month);
				
				
				Object[] as = advanceSecurity.get(0);
				count.setCodeAdvance((BigInteger) as[0]);
				count.setCodSecurity((BigInteger) as[1]);
				
				Object[] deposite=outward.get(0);
				count.setOutward((BigInteger)deposite[0]);
           
				Object[] cod = codDelivery.get(0);
				count.setCodDelivery((BigInteger) cod[0]);

				Object[] codRec = codReceive.get(0);

				count.setCodReceive((BigInteger) codRec[0]);
				List<Object[]> chequeSummary = ticketRepository.chequeSummary(tenantId, startDate, endDate);
				List<Object[]> chequeSummaryLast = ticketRepository.chequeSummary(tenantId, previounstartDate,
						previousendDate);
				Object[] chequeSumLast = chequeSummaryLast.get(0);
				count.setChJanl((BigDecimal) chequeSumLast[0]);
				count.setChfebl((BigDecimal) chequeSumLast[1]);
				count.setChmarl((BigDecimal) chequeSumLast[2]);
				count.setChaprl((BigDecimal) chequeSumLast[3]);
				count.setChmayl((BigDecimal) chequeSumLast[4]);
				count.setChJunl((BigDecimal) chequeSumLast[5]);
				count.setChJull((BigDecimal) chequeSumLast[6]);
				count.setChaugl((BigDecimal) chequeSumLast[7]);
				count.setChsepl((BigDecimal) chequeSumLast[8]);
				count.setChOctl((BigDecimal) chequeSumLast[9]);
				count.setChnovl((BigDecimal) chequeSumLast[10]);
				count.setChdecl((BigDecimal) chequeSumLast[11]);
				Object[] chequeSum = chequeSummary.get(0);
				count.setChJan((BigDecimal) chequeSum[0]);
				count.setChfeb((BigDecimal) chequeSum[1]);
				count.setChmar((BigDecimal) chequeSum[2]);
				count.setChapr((BigDecimal) chequeSum[3]);
				count.setChmay((BigDecimal) chequeSum[4]);
				count.setChJun((BigDecimal) chequeSum[5]);
				count.setChJul((BigDecimal) chequeSum[6]);
				count.setChaug((BigDecimal) chequeSum[7]);
				count.setChsep((BigDecimal) chequeSum[8]);
				count.setChOct((BigDecimal) chequeSum[9]);
				count.setChnov((BigDecimal) chequeSum[10]);
				count.setChdec((BigDecimal) chequeSum[11]);
				List<Object[]> locOut = ticketRepository.depositeLocOut(tenantId, id, month);
				Object[] lO = locOut.get(0);
				count.setDeplocal((BigInteger) lO[0]);
				count.setDepOutStaion((BigInteger) lO[1]);

			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public TenantDashBoardDto findreturnsdashBoard(String id, Integer month) {
		try {
			TenantDashBoardDto count = new TenantDashBoardDto();
			Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
			if (user.isPresent()) {
				User us = user.get();

				LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
				LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
				LocalDateTime previounstartDate = CommonUtil.previousFinancialYeartartDate(LocalDateTime.now());
				LocalDateTime previousendDate = CommonUtil.previousFinancialYearEndDate(LocalDateTime.now());
				Date date = new Date();

				if (month == null) {
					month = getMonthFromDate(date);
				}

				String tenantId = us.getTenantId();
				List<Object[]> claimReceived = ticketRepository.claimReceived(tenantId, id, month);
				List<Object[]> claimMatch=ticketRepository.claimMatch(tenantId, id, month);
				List<Object[]> claimAmount = ticketRepository.claimsAmount(tenantId, id, month);
				List<Object[]> claimsSummary = ticketRepository.claimsSummary(tenantId, id, startDate, endDate);
				List<Object[]> claimsSummaryLast = ticketRepository.claimsSummary(tenantId, id, previounstartDate,
						previousendDate);
				Object[] claimList = claimReceived.get(0);

				count.setClaimNonSale((BigInteger) claimList[0]);
				count.setClaimSale((BigInteger) claimList[1]);
				count.setClaimBoth((BigInteger) claimList[2]);
				
				Object[] claimsMatch = claimMatch.get(0);

				count.setClaimNonSaleMatch((BigInteger) claimsMatch[0]);
				count.setClaimSaleMatch((BigInteger) claimsMatch[1]);
				count.setClaimBothMatch((BigInteger) claimsMatch[2]);
				

				Object[] claimamt = claimAmount.get(0);

				count.setClaimAmtBoth((BigInteger) claimamt[2]);
				count.setClaimAmtSal((BigInteger) claimamt[0]);
				count.setClaimAmtNonSal((BigInteger) claimamt[1]);

				Object[] cliamSum = claimsSummary.get(0);
				count.setCjan((BigInteger) cliamSum[0]);
				count.setcFeb((BigInteger) cliamSum[1]);
				count.setcMar((BigInteger) cliamSum[2]);
				count.setcApr((BigInteger) cliamSum[3]);
				count.setcMay((BigInteger) cliamSum[4]);
				count.setCjun((BigInteger) cliamSum[5]);
				count.setcJuly((BigInteger) cliamSum[6]);
				count.setcAug((BigInteger) cliamSum[7]);
				count.setcSep((BigInteger) cliamSum[8]);
				count.setcOct((BigInteger) cliamSum[9]);
				count.setcNov((BigInteger) cliamSum[10]);
				count.setcDec((BigInteger) cliamSum[11]);

				Object[] cliamSumlast = claimsSummaryLast.get(0);
				count.setCjanl((BigInteger) cliamSumlast[0]);
				count.setcFebl((BigInteger) cliamSumlast[1]);
				count.setcMarl((BigInteger) cliamSumlast[2]);
				count.setcAprl((BigInteger) cliamSumlast[3]);
				count.setcMayl((BigInteger) cliamSumlast[4]);
				count.setCjunl((BigInteger) cliamSumlast[5]);
				count.setcJulyl((BigInteger) cliamSumlast[6]);
				count.setcAugl((BigInteger) cliamSumlast[7]);
				count.setcSepl((BigInteger) cliamSumlast[8]);
				count.setcOctl((BigInteger) cliamSumlast[9]);
				count.setcNovl((BigInteger) cliamSumlast[10]);
				count.setcDecl((BigInteger) cliamSumlast[11]);

			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Transactional
	public TenantDashBoardDto monthlyMissed(String id, Integer month) {
	    try {
	    	
	    	int year=LocalDateTime.now().getYear();
	        TenantDashBoardDto count = new TenantDashBoardDto();
	        Optional<User> user = userRepo.findById(SecurityUtil.getUserName());
	        if (user.isPresent()) {
	            User us = user.get();

	            Date date = new Date();

	            if (month == null) {
	                month = getMonthFromDate(date);
	            }
	            String tenantId = us.getTenantId();

	            List<Object[]> threshold = stockistRepository.thresholdLimitSuper(tenantId, id);
	            if (!threshold.isEmpty()) {
	                Object[] thresholdLimitSuper = threshold.get(0);
	                count.setBelowThresHold((BigInteger) thresholdLimitSuper[0]);
	            }

	            List<Object[]> nosequirity = stockistRepository.noSecuritChequesSuper(tenantId, id);
	            if (!nosequirity.isEmpty()) {
	                Object[] setNosequrity = nosequirity.get(0);
	                count.setNosequirity((BigInteger) setNosequrity[0]);
	            }
	            
	            count.setRetuenMissed(ticketRepository.claimsNotClosedSuper(tenantId, month,id).size());
	            //System.err.println(claimRepo.claimsNotReceievdSuperMonth(tenantId, "hh", month).size()+"claimRepo.claimsNotReceievdSuperMonth(tenantId, \"\", month).size()");

	            List<Object[]> setBlockedOrder = ticketRepository.blockedOrderSuper(tenantId, id);
	            if (!setBlockedOrder.isEmpty()) {
	                Object[] blockedOrder = setBlockedOrder.get(0);
	                count.setBlockedOrder((BigInteger) blockedOrder[0]);
	            }


	            List<Object[]> setLrNotReceived = ticketRepository.lrNotreceivedSuperLocal(tenantId, id);
	            if (!setLrNotReceived.isEmpty()) {
	                Object[] lrNotReceived = setLrNotReceived.get(0);
	                count.setLrNotReceived((BigInteger) lrNotReceived[0]);
	            }

	            List<TicketReport> ticketMissed = setTicketMiseed(tenantId, month, id);
	            BigInteger ticketMissedCount = BigInteger.valueOf(ticketMissed.size());
	            count.setTicketMiseed(ticketMissedCount);
    
	          
	            count.setOrderMissed(ticketRepository.getAllOrderDeadlineSuperAdminDash(tenantId, year, month,id).size());
	            count.setPodNotReceived(BigInteger.valueOf(ticketRepository.getAllOrderDetailSuperAdminDAshPod(tenantId ,id).size()));
	            return count;
	        }
	    } catch (Exception e) {
	        // Handle exception appropriately, log it, and maybe rethrow if necessary
	        e.printStackTrace(); // Consider using a logging framework like SLF4J
	    }
	    return null;
	}

	private List<TicketReport> setTicketMiseed(String tenantId, Integer month, String id) {
		try {
			int year=LocalDateTime.now().getYear();
		Query query = entityManager.createNativeQuery("SELECT * FROM ticketreportssuper WHERE tenantid = :tenantId  and deadline <days and emaildate >= DATE_TRUNC('month', DATE(:year || '-' || :month || '-01'))  AND emaildate < DATE_TRUNC('month', DATE(:year || '-' || :month || '-01') + INTERVAL '1 month')  ", TicketReport.class);
        query.setParameter(StringIteration.TENANTID, tenantId);
        query.setParameter(StringIteration.MONTH,month);
        query.setParameter("year",year);
        
		return query.getResultList();
	}catch (Exception e) {
		e.printStackTrace();
		}return null;
		}

	public ManufacturerDto manufacturerDashBoard() {
		try {
			ManufacturerDto man =new ManufacturerDto();
			Optional<User> us=userRepo.findById(SecurityUtil.getUserName());
			man.setTickets(ticketRepository.manufacturerDashBoardTicket(us.get().getUserId()));
			man.setOrders(ticketRepository.manufacturerDashBoardOrder(us.get().getUserId()));
			man.setReturns(ticketRepository.manufacturerDashBoardReturns(us.get().getUserId()));
			
			
			
			return man;
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();		
			}
		
		return null;
	}
	
	public  List<ManufacturerDto> manufaDashBoardDBytenant() {
		
		try {
			 List<ManufacturerDto>   manufacturerDto=new ArrayList<>();
			Optional<User> user=userRepo.findById(SecurityUtil.getUserName());
			if(user.isPresent()) {
				System.err.println("user"+user);
				if(user.get().getRoleName().equals(StringIteration.MANUFACTURERS)) {
					
			List<Object[]> ob=ticketRepository.manufacturerDashBoard(user.get().getUserId());
			
			for(Object[] b:ob) {
				ManufacturerDto  ma=new ManufacturerDto();
				ma.setTenantName(String.valueOf(b[0]));
				ma.setTickets(((BigInteger) b[1]).intValue());
				ma.setOrders(((BigInteger) b[2]).intValue());
				ma.setReturns(((BigInteger) b[3]).intValue());
	            manufacturerDto.add(ma);				
				
			}
			
				}
				
			}	
			return manufacturerDto;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
