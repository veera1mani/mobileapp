package com.healthtraze.etraze.api.security.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.security.model.Role;
import com.healthtraze.etraze.api.security.repository.RoleRepository;
import com.healthtraze.etraze.api.security.service.RoleService;
import java.util.Collections;


@RestController
public class RoleController implements BaseCrudController<Role, String> {

	private Logger logger = LogManager.getLogger(RoleController.class);

	
	private final RoleService roleService;
	private final RoleRepository roleRepository;
	
	@Autowired(required = true)
	public RoleController(RoleService roleService,RoleRepository roleRepository) {
		this.roleService=roleService;
		this.roleRepository=roleRepository;
	}

	@GetMapping(value = "/roles")
	@Override
	public List<Role> findAll() {
		return roleService.findAll();
	}

	@GetMapping(value = "/role/{id}")
	@Override
	public Role findById(@PathVariable String id) {
		try {
			return roleService.findById(id);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@GetMapping(value = "/role/findByRoleId/{roleId}")
	public Role findByRoleId(@PathVariable String roleId) {
		try {
			return roleService.findByRoleId(roleId);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PostMapping(value = "/role")
	@Override
	public Result<Role> create(@RequestBody Role t) {
		try {
			return roleService.create(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@PutMapping(value = "/role")
	@Override
	public Result<Role> update(@RequestBody Role t) {
		try {


			return roleService.update(t);
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return null;
	}

	@DeleteMapping(value = "/role/deleteBy/{roleId}")
	@Override
	public Result<Role> delete(@PathVariable String roleId) {
		return roleService.delete(roleId);
	}
	
	@GetMapping(value = "/roless")
	public List<Role> findRoles() {
		try {
			return roleRepository.findRoles();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}
	
}
