package com.healthtraze.etraze.api.security.service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.security.model.MobilePin;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserAuth;
import com.healthtraze.etraze.api.security.repository.MobilePinRepository;
import com.healthtraze.etraze.api.security.repository.UserAuthRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Service
public class MobilePinService implements BaseService<MobilePin, Long>{
	
	Logger logger = LogManager.getLogger(MobilePinService.class);
	
	public final MobilePinRepository mobilePinRepository;
	
	public final UserAuthRepository userAuthRepository;
	
	public final UserRepository userRepository;
	
	@Autowired
	public MobilePinService(MobilePinRepository mobilePinRepository,UserAuthRepository userAuthRepository , UserRepository userRepository ) {
		this.mobilePinRepository = mobilePinRepository;
		this.userAuthRepository = userAuthRepository;
		this.userRepository = userRepository ;
	}

	@Override
	public List<MobilePin> findAll() {
		
		return Collections.emptyList();
	}

	@Override
	public MobilePin findById(Long id) {
		
		return null;
	}
	
public Result<MobilePin> checkPin() {
		
	Result<MobilePin> result = new Result<>();
	try {
		Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
		if(us.isPresent()) {
			User u = us.get();
			Optional<MobilePin> pin = mobilePinRepository.findByUserId(u.getUserId() , u.getTenantId());
			if(pin.isPresent()) {
				result.setCode(StringIteration.SUCCESS_CODE);				
				result.setMessage("pin Exist");
			}
			else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("pin does not exist");
			}
			return result;
		}
	} catch(Exception e){
		result.setCode(StringIteration.ERROR_CODE2);
		
		logger.error(e.getMessage());
	}
	return result;	
	
	}

	@Override
	public Result<MobilePin> create(MobilePin t) {	
		Result<MobilePin> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if(us.isPresent()) {
			
				User u = us.get();	
				
				t.setPin(Base64.getEncoder().encodeToString(t.getPin().getBytes()));
				Optional<MobilePin> mpin = mobilePinRepository.loginByPin(t.getPin(),u.getTenantId());
			
				if(!mpin.isPresent()) {	
					t.setUserId(u.getUserId());
					t.setTenantId(u.getTenantId());
					mobilePinRepository.save(t);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.SUCCESS_MESSAGE);
				}
				else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("enter unique pin");
				}
			
			}
			
		} 
		catch(Exception e){
			result.setCode("2222");
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public Result<MobilePin> update(MobilePin t) {
		return null;
	}

	@Override
	public Result<MobilePin> delete(Long id) {
		return null;
	}
	
	
    public Result<MobilePin> loginBypin(String pin, String tenantId){
    	Result<MobilePin> result = new Result<>();
		try {
			MobilePin p = new MobilePin();
			if(pin != null && tenantId != null) {
				Optional<MobilePin> mpin = mobilePinRepository.loginByPin(pin,tenantId);
				
				if(mpin.isPresent()) {
					Optional<UserAuth> us = userAuthRepository.findByUserId(mpin.get().getUserId());
					UserAuth userAuth = us.get();
					String originalInput = userAuth.getUserId();
					String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
					p.setUid(encodedString);
					p.setPass(userAuth.getPassword());
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setData(p);
					result.setMessage(StringIteration.SUCCESS_MESSAGE);
				
				}
				else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Inavlid User");
				}
			}
			else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Inavlid User");
			}	
		} 
		catch(Exception e){
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
		return result;
	}
    
    
    
    public String[] loginpin(String pin, String tenantId) {
        try {
            String[] credential = new String[2];
            if (pin != null && tenantId != null) {
                pin = Base64.getEncoder().encodeToString(pin.getBytes());
                Optional<MobilePin> mpin = mobilePinRepository.loginByPin(pin, tenantId);

                if (mpin.isPresent()) {
                    Optional<UserAuth> us = userAuthRepository.findByUserId(mpin.get().getUserId());
                    if (us.isPresent()) { 
                        UserAuth userAuth = us.get();
                        credential[0] = userAuth.getUserId();
                        credential[1] = new String(Base64.getDecoder().decode(userAuth.getPassword()));
                        return credential;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new String[0];
    }

}
