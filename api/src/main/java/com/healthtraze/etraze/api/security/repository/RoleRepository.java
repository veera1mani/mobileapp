package com.healthtraze.etraze.api.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.Role;

@Repository
public interface RoleRepository extends BaseRepository<Role, String> {
	
    public Optional<Role> findByRoleId(String roleId);
	
	public Optional<Role> findByRoleName(String roleName);
	
	public void deleteByRoleId(Integer roleId);
	
	@Query(value = "SELECT * FROM tbl_role WHERE role_name != 'BUSINESS ADMIN' AND role_name != 'STOCKIST' AND role_name != 'TRANSPORT'", nativeQuery = true)
	public List<Role> findRoles();

	
	}
