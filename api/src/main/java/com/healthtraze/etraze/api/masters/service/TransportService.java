package com.healthtraze.etraze.api.masters.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.dto.OrderTransporterDTO;
import com.healthtraze.etraze.api.masters.dto.TransporterTicketDTO;
import com.healthtraze.etraze.api.masters.model.Audit;
import com.healthtraze.etraze.api.masters.model.AuditHistory;
import com.healthtraze.etraze.api.masters.model.OrderTransporterMapping;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.repository.AuditHistoryRepository;
import com.healthtraze.etraze.api.masters.repository.AuditRepository;
import com.healthtraze.etraze.api.masters.repository.CityRepository;
import com.healthtraze.etraze.api.masters.repository.OrderTransporterMappingRepository;
import com.healthtraze.etraze.api.masters.repository.TenantRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderRepository;
import com.healthtraze.etraze.api.masters.repository.TransportRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.service.UserService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service

public class TransportService implements BaseService<Transport, String> {

	private Logger logger = LogManager.getLogger(TransportService.class);
	
	
	private  final TicketOrderRepository ticketOrderRepository;
    private final	TransportRepository transportRepository;	
	private final  UserService userservice;
	private final UserRepository userRepository;
	private final CityRepository cityRepository;
	private final OrderTransporterMappingRepository orderTransporterMappingRepository;
	private final AuditHistoryRepository auditHistoryRepository;
   private  final TenantRepository tenantRepository;
	private AuditRepository auditRepository;

	@Autowired
	public TransportService(TicketOrderRepository ticketOrderRepository,TransportRepository transportRepository ,UserService userservice,
			UserRepository userRepository,CityRepository cityRepository,OrderTransporterMappingRepository orderTransporterMappingRepository,
			AuditHistoryRepository auditHistoryRepository,AuditRepository auditRepository,TenantRepository tenantRepository	)
	{
	this.ticketOrderRepository=ticketOrderRepository;
	this.transportRepository=transportRepository;
	this.userservice=userservice;
	this.userRepository=userRepository;
	this.cityRepository=cityRepository;
	this.orderTransporterMappingRepository=orderTransporterMappingRepository;
	this.auditHistoryRepository=auditHistoryRepository;
	
	this.auditRepository=auditRepository;
	this.tenantRepository=tenantRepository;
	}

	
	public Result<HashMap<String, Object>> findAllTransport(int page,  String value, String sortBy,
			String sortDir) {
		
		Result<HashMap<String, Object>> result = new Result<>();
		try {
		HashMap<String,Object> map = new HashMap<>();
		int size=10;
			
			
		if(StringUtils.isNullOrEmpty(sortBy)) {
			sortBy="transport_id";
		}
		if(StringUtils.isNullOrEmpty(sortDir)) {
			sortDir="ASC";
		}
		Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
		if(user.isPresent()) {
		 Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.fromString(sortDir),sortBy));
	    List<Transport> transport=transportRepository.findByTenantId(paging, user.get().getTenantId(),value);   
	    int totalCount=transportRepository.findByTenantId(user.get().getTenantId(),value).size();
	   
	    for (Transport tr : transport) {
	    	 if(tr.getLocation()!=null) {
	        Optional<String> city = cityRepository.findByCityCode(tr.getLocation());
	        if(city.isPresent()) {
	            tr.setLocation(city.get());
	        } 	    }
	    	 else {
	    		 tr.setLocation(""); 
	    	 }
	    }	    
	     map.put(Constants.TOTALCOUNT, totalCount);	
		map.put("transports", transport);
		
		result.setData(map);
		result.setCode("0000");
		result.setMessage("success");
		return result;
		}
		
	}catch(Exception ex) {
	
	logger.error(ex);
	result.setCode("1111");
		
	}
	return null;}
	

	
	@Override
	public List<Transport> findAll() {
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if(us.isPresent()) {
				return transportRepository.findByTenantId(us.get().getTenantId());
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}	
	public Map<String,String> findTransporterOrdersCount() {
		Map<String,String> map = new HashMap<>();
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if(us.isPresent()) {
			Optional<Transport> t= transportRepository.findByTenantIdAndTransporter(us.get().getTenantId(), SecurityUtil.getUserName());
			if(t.isPresent()) {
				Object[] ob = transportRepository.findByTenantIdAndTransporterOrderCount(us.get().getTenantId(), t.get().getTransportId());
				if (ob != null && ob.length > 0 && ob[0] instanceof Object[]) {
	                Object[] innerArray = (Object[]) ob[0];

	                if (innerArray.length == 3) { 
	                    map.put("total", String.valueOf(innerArray[0]));
	                    map.put("dispatched", String.valueOf(innerArray[1]));
	                    map.put("delivered", String.valueOf(innerArray[2]));
	                }
				}
			}
			}
			

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return map;
	}
	@Override
	public Transport findById(String id) {
		try {
			Optional<Transport> option = transportRepository.findById(id);
			if(option.isPresent()) {
			return option.get();}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	
	


	@Override
	public Result<Transport> create(Transport t) {

		Result<Transport> result = new Result<>();
		
		
		

		try {
			Optional<User> users = userRepository.findByUserId(SecurityUtil.getUserName());
			if(users.isPresent()) {
			Optional<Tenant> tenant = tenantRepository.findById(users.get().getTenantId());
			if(tenant.isPresent()&&tenant.get().getStatus().equals("ONBOARDED")) {
				result.setCode("1111");
				result.setMessage("Tenant Not Approved");
				return result;
			}}
			t.setStatus(StringIteration.ACTIVE_STATUS);
			User user = new User();
			user.setFirstName(t.getTransportName());

			user.setEmail(t.getEmail());
			user.setPhoneNo(t.getMobile());
			user.setIsUserOnboarded(true);
			user.setNewUserValidateWeb(true);
			user.setOtpVerified(true);
			user.setRoleId("3");
			Optional<User> ten = userRepository.findByUserId(SecurityUtil.getUserName());
			if (ten.isPresent()) {
				user.setTenantId(ten.get().getTenantId());
				t.setTenantId(ten.get().getTenantId());
			}
			Optional<User> userOptional = userRepository.findByUserId(SecurityUtil.getUserName());
			if(userOptional.isPresent()) {
			Optional<Tenant> tenantOptional = tenantRepository.findById(userOptional.get().getTenantId());

			if (tenantOptional.isPresent()) {
			    String id = tenantOptional.get().getTenantCode();
			    String pathId= id+"T";
			    Integer lastSequence = transportRepository.getLastSequence(pathId,tenantOptional.get().getTenantId());
			    if (lastSequence != null) {
			        String sequenceNumber = String.format("%sT%04d", id, lastSequence + 1);
			        t.setTransportId(sequenceNumber);
			        user.setUserId(sequenceNumber);
			    } else {
			        String sequenceNumber = String.format("%sT%04d", id, 1);
			        t.setTransportId(sequenceNumber);
			    
			        user.setUserId(sequenceNumber);
			    }
			    
			}
			
			}
			Result<User> isuserCreated = userservice.signUp(user);

			if (isuserCreated.getCode().equals("0000")) {
             t.setEnable(true);  
           Transport newTransport = transportRepository.save(t);
               
				result.setCode("0000");
				result.setData(newTransport);

				result.setMessage("transport addded sucessfully");
				return result;
			} else {
				result.setCode("1111");

				result.setMessage(isuserCreated.getMessage());
			}
		} catch (Exception exc) {
			result.setCode("1111");
			 logger.atError();
			
			
		
		}
		return result;
	}

	@Override
	public Result<Transport> update(Transport t) {
	    Result<Transport> result = new Result<>();
	    
	    Audit audit = createAuditEntry(t);

	    Transport existingTransport = transportRepository.findById(t.getTransportId())
	            .orElseThrow(() -> new ResourceNotFoundException("Transporter with ID :" + t.getTransportId()+ " Not Found!"));
	    
	    try {
	        if (existingTransport != null) {
	            updateAndAuditFieldIfNeeded(audit.getId(), "transportName", existingTransport.getTransportName(), t.getTransportName());
	            updateAndAuditFieldIfNeeded(audit.getId(), "mobile", existingTransport.getMobile(), t.getMobile());
	            updateAndAuditFieldIfNeeded(audit.getId(), "mail", existingTransport.getEmail(), t.getEmail());
	            updateAndAuditFieldIfNeeded(audit.getId(), "firstName", existingTransport.getFirstName(), t.getFirstName());
	            updateAndAuditFieldIfNeeded(audit.getId(), "lastName", existingTransport.getLastName(), t.getLastName());
	            updateAndAuditFieldIfNeeded(audit.getId(), "address1", existingTransport.getAddress1(), t.getAddress1());
	            updateAndAuditFieldIfNeeded(audit.getId(), "address2", existingTransport.getAddress2(), t.getAddress2());
	            updateAndAuditFieldIfNeeded(audit.getId(), "country", existingTransport.getCountry(), t.getCountry());
	            updateAndAuditFieldIfNeeded(audit.getId(), "location", existingTransport.getLocation(), t.getLocation());
	            updateAndAuditFieldIfNeeded(audit.getId(), "state", existingTransport.getState(), t.getState());
	            
	            
	            updateTransportEntity(existingTransport, t);
	            transportRepository.save(existingTransport);

	            result.setCode(StringIteration.SUCCESS_CODE);
	            result.setData(existingTransport);
	            result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error(e);
	    }

	    return result;
	}

	private Audit createAuditEntry(Transport t) {
	    Audit audit = new Audit();
	    audit.setId(String.valueOf(System.currentTimeMillis()));
	    audit.setTransport_id(t.getTransportId());
	    audit.setTenantId(t.getTenantId());
	    audit.setVersionNo(1);
	    CommonUtil.setCreatedOn(audit);
	    auditRepository.save(audit);
	    return audit;
	}

	private void updateAndAuditFieldIfNeeded(String auditId, String fieldName, String oldValue, String newValue) {
	    if (!Objects.equals(oldValue, newValue)) {
	        updateToAudit(auditId, fieldName, oldValue, newValue);
	    }
	}

	private void updateTransportEntity(Transport existingTransport, Transport updatedTransport) {
	    existingTransport.setTransportId(updatedTransport.getTransportId());
	    existingTransport.setAddress1(updatedTransport.getAddress1());
	    existingTransport.setAddress2(updatedTransport.getAddress2());
	    existingTransport.setEmail(updatedTransport.getEmail());
	    existingTransport.setMobile(updatedTransport.getMobile());
	    existingTransport.setLocation(updatedTransport.getLocation());
	    existingTransport.setCountry(updatedTransport.getCountry());
	    existingTransport.setFirstName(updatedTransport.getFirstName());
	    existingTransport.setLastName(updatedTransport.getLastName());
	    existingTransport.setState(updatedTransport.getState());
	    existingTransport.setTransportName(updatedTransport.getTransportName());
	    existingTransport.setPincode(updatedTransport.getPincode());
	}

	private void updateToAudit(String id, String updatedField, String oldValue, String newValue) {
		
	    AuditHistory auditHistory = new AuditHistory();
	    auditHistory.setId(System.currentTimeMillis() + "");
	    auditHistory.setAudit_id(id);
	    auditHistory.setField(updatedField);
	    auditHistory.setBfrvalue(oldValue);
	    auditHistory.setAfrvalue(newValue);
	    Optional<User> us=userRepository.findById(SecurityUtil.getUserName());
	    if(us.isPresent()) {
	    User user=us.get();
	    
	    auditHistory.setTenantId(user.getTenantId());
	    }
	    CommonUtil.setCreatedOn(auditHistory);
	    auditHistoryRepository.save(auditHistory);
	}
	@Override
	public Result<Transport> delete(String transportId) {
		Result<Transport> result = new Result<>();
		try {
			Optional<User> user=userRepository.findByUserId(SecurityUtil.getUserName());
			Optional<Transport> tr = transportRepository.findById(transportId);
			if(user.isPresent() && tr.isPresent()) {
				User u=user.get();
				Transport t = tr.get();
				
				Optional<User> us = userRepository.findByUserId(transportId);
				
				if(t.getStatus().equals(StringIteration.DEACTIVE_STATUS)) {
					transportRepository.setStatus(transportId,u.getTenantId(),StringIteration.ACTIVE_STATUS);
					if(us.isPresent()) {
						userRepository.setUserStatus(transportId,u.getTenantId(),"Active");
					}
					result.setMessage("Activated...");
					result.setCode("0000");
				}else {
					transportRepository.setStatus(transportId,u.getTenantId(),StringIteration.DEACTIVE_STATUS);
					if(us.isPresent()) {
						userRepository.setUserStatus(transportId,u.getTenantId(),"Inactive");
					}
					result.setMessage("DeActivated...");
					result.setCode("0000");
				}
				
				
			}

		} catch (Exception ex) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(ex.getMessage());
		}

		return result;
	}


	public Result<OrderTransporterMapping> orderTransporter(OrderTransporterDTO ot) {
        Result<OrderTransporterMapping> result = new Result<>();

        try {
        
        	for(TicketOrderInvoice t : ot.getTickets()) {   
        		OrderTransporterMapping rt = new OrderTransporterMapping();
        		rt.setId(System.currentTimeMillis()+"");
        		rt.setTicketId(t.getTicketId());
        		rt.setTransporterId(ot.getTransporterId()); 
        		rt.setTenantId(t.getTenantId());
               orderTransporterMappingRepository.save(rt);        		
        	}  
            result.setMessage("Transporter ordered successfully");
        } catch (Exception ex) {
            result.setMessage("Failed to order transporter");
        }

        return result;
    }


	public List<Ticket> findTransporterOrders() {
	
		try {
			Optional<User> us = userRepository.findByUserId(SecurityUtil.getUserName());
			if(us.isPresent()) {
			Optional<Transport> t= transportRepository.findByTenantIdAndTransporter(us.get().getTenantId(), SecurityUtil.getUserName());
			if(t.isPresent()) {
			return transportRepository.findTransporterOrders(us.get().getTenantId(), t.get().getTransportId());
			
				
			}
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}


	public List<TransporterTicketDTO> findTenantTransporterOrders() {
		try {
			Optional<User> op = userRepository.findByUserId(SecurityUtil.getUserName());
			if(op.isPresent()) {
			User us = op.get();
		
			List<TransporterTicketDTO> list = null;
			if(us.getRoleName().equals("SUPERADMIN")) {
					list = ticketOrderRepository.findOrdersByTenantId(us.getTenantId());
			}else {
				Optional<Transport> t= transportRepository.findByTenantIdAndTransporter(us.getTenantId(), SecurityUtil.getUserName());
				if(t.isPresent()) {
					   list = ticketOrderRepository.findOrdersByTransporterId(us.getTenantId(),t.get().getTransportId());
				}
			}	
			return list;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

}