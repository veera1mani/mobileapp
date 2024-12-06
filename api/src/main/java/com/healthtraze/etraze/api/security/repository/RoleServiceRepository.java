package com.healthtraze.etraze.api.security.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.RoleServices;


@Repository
public interface RoleServiceRepository extends  BaseRepository<RoleServices, Integer> {
	
	public Optional<Set<RoleServices>> findByRoleId(String roleId);


}
