package com.healthtraze.etraze.api.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.service.TenantService;
import com.healthtraze.etraze.api.security.model.Role;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

@Component
public class RoleService implements BaseService<Role, String> {

	private Logger logger = LogManager.getLogger(RoleService.class);

	
	private final RoleRepository roleRepository;
	
	private final UserRepository userRepository;
	
	private final TenantService tenantService;
	
	
    @Autowired
	public RoleService(RoleRepository roleRepository, UserRepository userRepository, TenantService tenantService) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.tenantService = tenantService;
	}

	@Override
	public List<Role> findAll() {
		try {

			
			return roleRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return new ArrayList<>();
	}

	@Override
	public Role findById(String id) {
		try {
			Optional<Role> option = roleRepository.findById(id);
			if (option.isPresent()) {
				return option.get();
			}
			return null;
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	public Role findByRoleId(String id) {
		try {
			Optional<Role> option = roleRepository.findByRoleId(id);
			if (option.isPresent()) {
				return option.get();
			}

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<Role> create(Role t) {
		Result<Role> result = new Result<>();
		try {
			Optional<User> ten = userRepository.findByUserId(SecurityUtil.getUserName());
			Tenant tenant = tenantService.findById(ten.get().getTenantId());
			if(tenant.getStatus().equals("PENDING")) {
				result.setCode("1111");
				result.setMessage("Tenant Not Approved");
				return result;
			}
			CommonUtil.setCreatedOn(t);
			t.setRoleId(CommonUtil.getID());
			Optional<Role> r = roleRepository.findByRoleName(t.getRoleName());
			if (r.isPresent()) {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.ROLENAMEALREADYEXIST);
			} else {	
				
				Role role = roleRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYCREATED);
				result.setData(role);
			}		
			
			return result;
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@Override
	public Result<Role> update(Role t) {
		Result<Role> result = new Result<>();
		try {
			if (t != null) {
				CommonUtil.setModifiedOn(t);
				Role role = roleRepository.save(t);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.SUCCESSFULLYUPDATED);
				result.setData(role);
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage(StringIteration.SHOULDNOTBEEMPTY);
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(Constants.ERROR);
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

	@Override
	public Result<Role> delete(String id) {
		Result<Role> result = new Result<>();
		try {
			Optional<Role> tr = roleRepository.findById(id);
			if (tr.isPresent()) {
				roleRepository.delete(tr.get());
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(id + StringIteration.DELETEDSUCCESSFULLY);
				return result;
			}

		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
			logger.error(StringIteration.SPACE, e);
		}
		return result;
	}

}
