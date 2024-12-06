
package com.healthtraze.etraze.api.masters.service;
 
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.ClaimOverviewDTO;
import com.healthtraze.etraze.api.masters.dto.ClaimsReturnsReportDTO;
import com.healthtraze.etraze.api.masters.model.Claim;
import com.healthtraze.etraze.api.masters.model.Return;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.ClaimRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.ReturnRepository;
import com.healthtraze.etraze.api.masters.repository.StockistRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service

public class ClaimService implements BaseService<Claim, String> {

 
	private static final String PREFIX = "C";

	private Logger logger = LogManager.getLogger(ClaimService.class);

	private final UserRepository userRepository;
	  
	private final ClaimRepository claimRepository;

	private final ReturnRepository returnRepository;

	private final StockistRepository stockistRepository;

	private final TenantRepository tenantRepository;

	private final EntityManager entityManager;

	private final ManufacturerRepository manufacturerRepository;
	
	private final CityRepository cityRepository;
	
	private final ListValueRepository listValue;
	
	@Autowired
	public ClaimService(ClaimRepository claimRepository,ReturnRepository returnRepository,StockistRepository stockistRepository,
			TenantRepository tenantRepository,NamedParameterJdbcTemplate namedParameterJdbcTemplate,
			EntityManager entityManager,ManufacturerRepository manufacturerRepository,UserRepository userRepository,CityRepository cityRepository,ListValueRepository listValue) {
	      this.listValue=listValue;    
		this.claimRepository = claimRepository;
		this.returnRepository = returnRepository;
		this.stockistRepository  = stockistRepository;
		this.tenantRepository = tenantRepository;
		this.entityManager = entityManager;	
		this.manufacturerRepository = manufacturerRepository;
		this.userRepository = userRepository;
		this.cityRepository=cityRepository;
		
	}
	@Override

	public List<Claim> findAll() {

		return claimRepository.findAll();

	}

	@Override

	public Claim findById(String id) {

		try {

			Optional<Claim> option = claimRepository.findById(id);

			if (option.isPresent()) {

				return option.get();

			}

		} catch (Exception e) {
			
			logger.error("", e);
		}

		return null;

	}

	@Override

	public Result<Claim> create(Claim t) {

		Result<Claim> result = new Result<>();

		try {
			Optional<User> usr = userRepository.findByUserId(SecurityUtil.getUserName());
			
			if(usr.isPresent()) {
				
				User u = usr.get();
				Optional<Claim> cl = claimRepository.findClaims(u.getTenantId(),u.getUserId(),t.getClaimNumber());
				
				Optional<Tenant> tn = tenantRepository.findById(usr.get().getTenantId());

				if (!cl.isPresent() && tn.isPresent()) {
					
					String lastNum = claimRepository.getLastSequence(usr.get().getTenantId());
		            ZoneId zoneId = ZoneId.systemDefault();
		            ZonedDateTime now = ZonedDateTime.now(zoneId);
		            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd", Locale.ENGLISH);
		            String strToday = dtf.format(now);
		            
					if(lastNum!=null) {
						String s=String.format("%s%02d", PREFIX, new BigInteger(lastNum).add(BigInteger.ONE));
						t.setClaimId(tn.get().getTenantCode()+strToday+s);
					}
					else {
						t.setClaimId(tn.get().getTenantCode()+strToday+PREFIX+"01");
					}


					t.setStatus("PENDING");

					Optional<Stockist> st = stockistRepository.email(u.getUserId(),u.getTenantId());

					if (st.isPresent()) {
						
						t.setStockistId(u.getUserId());

						t.setTenantId(u.getTenantId());

					}

					CommonUtil.setCreatedOn(t);

					Claim claim = claimRepository.save(t);

				Return rtn = new Return();
				rtn.setReturnId(System.currentTimeMillis() + "");

				 rtn.setClaimId(claim.getClaimId());
				rtn.setCreatedOn(new Date());
				rtn.setStockistId(claim.getStockistId());
				rtn.setClaimNumber(claim.getClaimNumber());

					rtn.setStatus(claim.getStatus());

					rtn.setSalabletatus(claim.getStatus());

					rtn.setNonSalabletatus(claim.getStatus());

					rtn.setTenantId(tn.get().getTenantId());

					rtn.setManufacturer(claim.getManufacturerId());

					rtn.setDocument(claim.getDocument());
					
					rtn.setDocumentName(claim.getDocumentName());

					returnRepository.save(rtn);

					result.setData(claim);

					result.setMessage("created");

					result.setCode("0000");

				}

				else {

					result.setMessage("Claim Already Exist");

					result.setCode("1111");

				}

			}

			}
			
		catch (Exception e) {

			result.setMessage(StringIteration.ERROR);

			result.setCode("1111");

			logger.error(e);

		}

		return result;

	}

	@Override

	public Result<Claim> update(Claim t) {

		Result<Claim> result = new Result<>();
		Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
		if(user.isPresent()) {
			User us = user.get();
			Optional<Claim> clm = claimRepository.findById(us.getTenantId(),t.getClaimId());
			if(clm.isPresent() && clm.get().getStatus().equals("PENDING")) {
				Claim cus = clm.get();
				Optional<Return> ret = returnRepository.findByClaimId(cus.getClaimId(),us.getTenantId());
				if(ret.isPresent()) {
					Return rn = ret.get();
					rn.setClaimNumber(t.getClaimNumber());
					rn.setManufacturer(t.getManufacturerId());
					rn.setDocument(t.getDocument());
					rn.setDocumentName(t.getDocumentName());
					returnRepository.save(rn);
					
					
					cus.setClaimNumber(t.getClaimNumber());
					cus.setStockistId(t.getStockistId());
					cus.setStatus(t.getStatus());
					cus.setManufacturer(t.getManufacturer());
					cus.setManufacturerId(t.getManufacturerId());
					cus.setDocument(t.getDocument());
					cus.setDocumentName(t.getDocumentName());
					Claim claim = claimRepository.save(cus);
					result.setData(claim);
					result.setCode("0000");
					result.setMessage("updated");
				}
			}else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Claim Cannot Be Change");
			}

		}
		return result;

		
	}

	@Override

	public Result<Claim> delete(String id) {

		Result<Claim> result = new Result<>();

		try {
            Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
			Optional<Claim> cl = claimRepository.findByClaimNumber(id);

			if(user.isPresent()) {
				if (cl.isPresent()) {

					if (cl.get().getStatus().equals("PENDING")) {

						claimRepository.deleteById(cl.get().getClaimId());

						Return rn = returnRepository.getByClaimNumber(cl.get().getClaimNumber(),user.get().getTenantId());

						returnRepository.deleteById(rn.getSerialNumber());

						result.setMessage("deleted");

						result.setCode("0000");

					}

					else {

						result.setMessage("");

						result.setCode("1111");

					}

				}

				else {

					result.setMessage("");

					result.setCode("1111");

				}

			}
			
		} catch (Exception e) {

			logger.error(e);

		}

		return result;

	}


public List<ClaimsReturnsReportDTO> claimsStatusReport( Map<String, String> params) {
 
	        try {
	        	
	            List<Object[]> ob=new ArrayList<>();
	        
	        	String claimNumber="";
				String serialNumber="";
				String status ="";
				String location="";
				String stockist="";
				
				if(params.get(StringIteration.STATUS) != null) {
					status=params.get(StringIteration.STATUS);
				}
				if(params.get(StringIteration.STOCKISTNAME) != null) {
					stockist =params.get(StringIteration.STOCKISTNAME).trim();
				}
				if(params.get(StringIteration.LOCATION)!= null) {
					location=params.get(StringIteration.LOCATION);
				}
				if(params.get(StringIteration.CLAIMNUMBER) != null) {
					claimNumber =params.get(StringIteration.CLAIMNUMBER).trim();
				}
				if(params.get("serialNumber")!= null) {
					serialNumber=params.get("serialNumber").trim();
				}
 
 
       	       Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName()); 
	        	if(us.isPresent()&& us.get().getRoleName().equals(StringIteration.MANAGER)) {
                 ob= claimRepository.claimsStatusReport(us.get().getTenantId(),us.get().getUserId(),status,claimNumber,stockist,location);
	        	}else if (us.isPresent()&& us.get().getRoleName().equals("USER")) {
	        		
	        		 ob= claimRepository.claimsStatusReport(us.get().getTenantId(),us.get().getHierarachyId(),status,claimNumber,stockist,location);	
	        	}
 
	            List<ClaimsReturnsReportDTO> tr = new ArrayList<>();
 
	            for(Object[] b : ob) {
 
	            	ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();
 
	            	td.setSerialNumber(String.valueOf(b[0]));
 
	            	td.setClaimNumber(String.valueOf(b[1]));
 
	            	td.setReceivedDate(String.valueOf(b[4]));
 
	            	td.setStockistName(String.valueOf(b[5]));
 
	            	td.setLocation(String.valueOf(b[6]));
 
	            	td.setClaimType(String.valueOf(b[2]));
 
	            	td.setStatus(String.valueOf(b[3]));
 
	            	tr.add(td);
 
	            }
               
                if(!serialNumber.equals("")) {
            	final String stk=serialNumber;
            	tr=tr.stream().filter(obj->obj.getSerialNumber().contains(stk)).collect(Collectors.toList());
            	return tr;
              }
                 
               
	            return tr;
 
	        } catch (Exception e) {
 
	            logger.error("", e);
 
	        }
 
	        return new ArrayList<>();
 
	    }
	
	 public List<ClaimOverviewDTO> claimOverviewReport(Map<String, String> params) {
	        try {
				
	        	List<Object[]> ob=new ArrayList<>();
	            LocalDateTime startDate = CommonUtil.financialYearStartDate(LocalDateTime.now());
	            LocalDateTime endDate = CommonUtil.financialYearEndDate(LocalDateTime.now());
 
	            if (params.get("selectedMonth") != null) {
	                String selectedMonth = params.get("selectedMonth");
	                Month month = Month.valueOf(selectedMonth.toUpperCase());
	                int year = Year.now().getValue();
 
	                startDate = LocalDate.of(year, month, 1).atStartOfDay();
	                endDate = LocalDate.of(year, month, startDate.toLocalDate().lengthOfMonth()).atTime(23, 59, 59);
	            }
	        	
	        	Optional<User> u = userRepository.findByUserId(SecurityUtil.getUserName());
	        	if(u.isPresent()) {
	        		User us = u.get();
		        	if(us.getRoleName().equals("MANAGER")) {
		             ob= claimRepository.claimOverviewReport(us.getTenantId(),us.getUserId(),startDate,endDate);
		        	}
		        	else if (us.getRoleName().equals("USER")) {
		        		  ob= claimRepository.claimOverviewReport(us.getTenantId(),us.getHierarachyId(),startDate,endDate);
		        	}
		            List<ClaimOverviewDTO> tr = new ArrayList<>();
		            for(Object[] b : ob) {
		            	ClaimOverviewDTO td = new ClaimOverviewDTO();
		            	td.setCount(String.valueOf(b[0]));
		            	td.setAmount(String.valueOf(b[1]));
		            	td.setReceived(String.valueOf(b[2]));
		            
		            	td.setFirstCheck(String.valueOf(b[3]));
		            	td.setGrrn(String.valueOf(b[4]));
		            	td.setSecondCheck(String.valueOf(b[5]));
		            	td.setCn(String.valueOf(b[6]));
		            	td.setHo(String.valueOf(b[7]));            	
		            	
		            	tr.add(td);	
		            	
		            }
		            return tr;
	        	}
	            
	        } catch (Exception e) {
	            logger.error("", e);
	        }
	        return new ArrayList<>();
	    }
  
public Result<HashMap<String, Object>> findAllClaimsByTenant(int page,String value,String sortBy,String sortDir) {

		Result<HashMap<String, Object>> result = new Result<>();

		try {
			
            Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
           
			HashMap<String, Object> map = new HashMap<>();
			if(user.isPresent()) {
				
				if (StringUtils.isNullOrEmpty(sortBy)) {

					sortBy = "claim_id";

				}

				if (StringUtils.isNullOrEmpty(sortDir)) {

					sortDir = "DESC";

				}

				if (StringUtils.isNullOrEmpty(value)) {

					value = "";

				}
				User us = userRepository.getById(SecurityUtil.getUserName());
				   Stockist st = stockistRepository.findByUserId(us.getUserId(),us.getTenantId());

				int total = claimRepository.findAllClaimesByTenantId(st.getStockistId(), value,user.get().getTenantId()).size();

				Pageable paging = PageRequest.of(page, 10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
                 System.err.println(value+"===========>");
				List<Claim> claimList = claimRepository.findAllClaimesByTenant(st.getStockistId(), value, paging,user.get().getTenantId());

				claimList.forEach(cl -> {

					Optional<Manufacturer> manf = manufacturerRepository.findById(cl.getManufacturerId());

					if (manf.isPresent()) {

						cl.setManufacturer(manf.get().getManufacturerName());

					}

				});

				map.put(StringIteration.CLAIMS, claimList);

				map.put(Constants.TOTALCOUNT, total);

				result.setData(map);

				result.setCode(StringIteration.SUCCESS_CODE);

				result.setMessage(StringIteration.SUCCESS_MESSAGE);

			}

			
		}

		catch (Exception e) {

			result.setCode(StringIteration.ERROR_CODE1);

			result.setMessage("error");
			e.printStackTrace();

			logger.error(e.getMessage());

		}

		return result;

	}

	@Transactional

	public Result<HashMap<String, Object>> findAllClaim(int page, int limit, String value, String sortBy,
			String sortDir) {

		Result<HashMap<String, Object>> result = new Result<>();

		HashMap<String, Object> map = new HashMap<>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

		Root<Claim> root = countQuery.from(Claim.class);

		if (StringUtils.isNullOrEmpty(sortBy)) {

			sortBy = "createdOn";

		}

		if (StringUtils.isNullOrEmpty(sortDir)) {

			sortDir = "desc";

		}
	           

		Predicate[] predicates = getPredicates(criteriaBuilder, root, value);

		countQuery.select(criteriaBuilder.count(root)).where(predicates);

		long totalCount = entityManager.createQuery(countQuery).getSingleResult();

		map.put(Constants.TOTALCOUNT, totalCount);

		CriteriaQuery<Claim> dataQuery = criteriaBuilder.createQuery(Claim.class);

		root = dataQuery.from(Claim.class);

		dataQuery.select(root).where(predicates);

		dataQuery.orderBy(

				sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?

						criteriaBuilder.asc(root.get(sortBy)) :

						criteriaBuilder.desc(root.get(sortBy)),

				criteriaBuilder.desc(root.get(Constants.CREATEDON))

		);

		TypedQuery<Claim> typedQuery = entityManager.createQuery(dataQuery);

		typedQuery.setFirstResult(page * limit);

		typedQuery.setMaxResults(limit);

		List<Claim> claims = typedQuery.getResultList();

		map.put("claims", claims);

		result.setCode("0000");

		result.setMessage("sucess");

		result.setData(map);

		return result;

	}

	private Predicate[] getPredicates(CriteriaBuilder criteriaBuilder, Root<Claim> root, String value) {

		return new Predicate[] {

				criteriaBuilder.or(

						criteriaBuilder.like(root.get("claimId"), "%" + value + "%"),

						criteriaBuilder.like(root.get(StringIteration.CLAIMNUMBER), "%" + value + "%"),

						criteriaBuilder.like(root.get(StringIteration.STATUS), "%" + value + "%")

				// criteriaBuilder.like(root.get("createdOn"), "%" + value + "%")

				// criteriaBuilder.like(root.get("receivedate"), "%" + value + "%")

				)

		};

	}


	public Result<HashMap<String, Object>> findClaims() {

        Result<HashMap<String, Object>> result = new Result<>();

        try {
            HashMap<String, Object> map = new HashMap<>();
            Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
            if(us.isPresent()) {
            	User u =us.get();
            	 Stockist s = stockistRepository.findByUserId(u.getUserId(),u.getTenantId());
                 List<Claim> claimList = claimRepository.findClaims(s.getTenantId(), s.getStockistId());

                 claimList.forEach(cl -> {
                     if (cl.getManufacturerId() != null) {
                         Optional<Manufacturer> manf = manufacturerRepository.findById(cl.getManufacturerId());
                         if (manf.isPresent()) {
                             cl.setManufacturer(manf.get().getManufacturerName());
                         }
                     }
                 });

                 map.put("claims", claimList);

                 result.setData(map);
                 result.setCode(StringIteration.SUCCESS_CODE);
                 result.setMessage(StringIteration.SUCCESS_MESSAGE);

            }
           
        } catch (Exception e) {
            result.setCode(StringIteration.ERROR_CODE1);
            result.setMessage("error");
            logger.error(e.getMessage());
        }

        return result;
    }

	public List<ClaimsReturnsReportDTO> claimNotReecived(Map<String, String> params) {
		   try {
	       
	            List<Object[]> ob=new ArrayList<>();
	        
	        	String claimNumber="";
				String status ="";
				String location="";
				String stockist="";
				String	manufacturer="";
				String sapId="";
				String uploadedDate="";
				String daysTaken="";
				String locality="";
				if(params.get("status") != null) {
					status=params.get("status");
				}
				if(params.get("stockist") != null) {
					stockist =params.get("stockist").trim();
				}
				if(params.get("location")!= null) {
					location=params.get("location");
				}
				if(params.get("claimNumber") != null) {
					claimNumber =params.get("claimNumber").trim();
				}
				if(params.get("manufacturer")!= null) {
					manufacturer=params.get("manufacturer").trim();
				}
				if(params.get("sapId") != null) {
					sapId =params.get("sapId").trim();
				}
				
				if(params.get("cnDate") != null) {
					uploadedDate =params.get("cnDate");
				}

				 if(params.get("daysTaken")!= null) {
						daysTaken=params.get("daysTaken").trim();
					 }
					

                 if(params.get(StringIteration.IS_LOCALITY)!=null) {
						locality=params.get(StringIteration.IS_LOCALITY);
					}

                 int months=0;
				  if(params.get("selectedMonth") != null) {
					 months = Integer.parseInt(params.get("selectedMonth"));
				    }
					

      	       Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName()); 
	        	if(us.isPresent()&& us.get().getRoleName().equals("MANAGER")) {
	        		if(months!=0) {
	        			  ob= claimRepository.claimsNotReceievdMonth(us.get().getTenantId(),us.get().getUserId(),months);	
	        		}else {
                ob= claimRepository.claimsNotReceievd(us.get().getTenantId(),us.get().getUserId());
	        	}
	        		
	        	}
	        	else if (us.isPresent()&& us.get().getRoleName().equals("USER")) {
	        		
	        		if(months!=0) {
	        			  ob= claimRepository.claimsNotReceievdMonth(us.get().getTenantId(),us.get().getHierarachyId(),months);	
	        		}else {
              ob= claimRepository.claimsNotReceievd(us.get().getTenantId(),us.get().getHierarachyId());
	        	}
	
	        	}
	        	else if (us.isPresent()&& us.get().getRoleName().equals(StringIteration.SUPERADMIN)) {
	        		if(months!=0) {
	        			 ob= claimRepository.claimsNotReceievdMonthSuper(us.get().getTenantId(),months);	
	        		}else {
	        			 
	        			   ob= claimRepository.claimsNotReceievdSuper(us.get().getTenantId());
	        		}
	        			
	        	}
	        	

	            List<ClaimsReturnsReportDTO> tr =claimNotReceivedOb(ob);
                                             tr=filterclaimNotReceivedOb(tr,stockist,location,manufacturer,claimNumber,status,sapId);
	          
	           
                
                
                if(!uploadedDate.equals("")) {
	            	final String stk=uploadedDate.toLowerCase();
	            	tr=tr.stream().filter(obj->obj.getLrDate().contains(stk)).collect(Collectors.toList());
	            	
	            	
	            }
                if(!daysTaken.equals("")) {
	            	final String stk=daysTaken;
	            	tr=tr.stream().filter(obj->obj.getDays().equals(stk)).collect(Collectors.toList());
	            	
	            	
	            } 
                if(!locality.equals("")) {
                  	final String local =locality;
                  
                  	tr=tr.stream().filter(obj->obj.getLocal().equalsIgnoreCase(local)).collect(Collectors.toList());
                  }
        

	            return tr;

	        } catch (Exception e) {

	            logger.error("", e);

	        }
		   return new ArrayList<>();
	}
	private List<ClaimsReturnsReportDTO> filterclaimNotReceivedOb(List<ClaimsReturnsReportDTO> tr, String stockist,
			String location, String manufacturer, String claimNumber, String status, String sapId) {
		 if(!stockist.equals("")) {
         	final String stk=stockist.toLowerCase();
         	tr=tr.stream().filter(obj->obj.getStockistName().toLowerCase().contains(stk)).collect(Collectors.toList());
         	
         	
         } 
         if (!location.equals("")) {
         	
         	final String city=location;
             
            tr = tr.stream()
                     .filter(obj -> obj.getLocation().equalsIgnoreCase(city))
                      .collect(Collectors.toList());
             
             }
         
         

	
			
			if (manufacturer!= null && !manufacturer.isEmpty()) {
	                final String city = manufacturer;
	                tr = tr.stream()
	                        .filter(obj -> obj.getManufacturer().contains(city))
	                        .collect(Collectors.toList());
	            }

         if(status!=null&&!status.isEmpty()) {
         	final String statuss = status;
             tr = tr.stream()
                     .filter(obj -> obj.getStatus().contains(statuss))
                     .collect(Collectors.toList());
         
         	
         }
         if(!claimNumber.equals("")) {
         	final String stk=claimNumber.toLowerCase();
         	tr=tr.stream().filter(obj->obj.getClaimNumber().toLowerCase().contains(stk)).collect(Collectors.toList());
         	
         	
         }

         if(!sapId.equals("")) {
	            	final String stk=sapId.toLowerCase();
	            	tr=tr.stream().filter(obj->obj.getSapId().toLowerCase().contains(stk)).collect(Collectors.toList());
	            	
	            	
	            }
		return tr;
	}
	private List<ClaimsReturnsReportDTO> claimNotReceivedOb(List<Object[]> ob) {
		List<ClaimsReturnsReportDTO> tr =new ArrayList<>();
		  for(Object[] b : ob) {

          	ClaimsReturnsReportDTO td = new ClaimsReturnsReportDTO();

          	td.setClaimNumber(String.valueOf(b[0]));
          	td.setLrDate(String.valueOf(b[2]));
          	td.setStockistName(String.valueOf(b[1]));
          	td.setDays(String.valueOf(b[3]));
              td.setManufacturer(String.valueOf(b[4]));
              td.setStatus(String.valueOf(b[5]));
           	td.setLocation(String.valueOf(b[6]));
         	    td.setSapId(String.valueOf(b[7]));
         	    td.setLocal(listValue.findByCodeAndName(String.valueOf(b[8])));
         
          Optional<String> city=cityRepository.findByCityCode(td.getLocation());
          if(city.isPresent()) {
         	 td.setLocation(city.get());
          }
           

          	tr.add(td);
          }
        
		return tr;
	}
}
