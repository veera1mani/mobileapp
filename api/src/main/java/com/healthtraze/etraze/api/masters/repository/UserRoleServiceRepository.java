package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.UserRoleService;

@Repository
public interface UserRoleServiceRepository extends  BaseRepository<UserRoleService, String> {

	@Query(value = "SELECT urs.id, urs.created_by, urs.created_on, urs.modified_by, urs.modified_on, urs.tenant_id, urs.version_no, urs.role_id, urs.screen_id, urs.screenname, urs.user_id ,ts.path, ts.screen_order, ts.icon, ts.type FROM tbl_user_role_services urs JOIN tbl_screen ts ON urs.screen_id = ts.screen_id WHERE urs.user_id = :id and urs.tenant_id=:tenantId AND urs.screen_id IS NOT NULL",nativeQuery = true)
	public List<Object[]> findByUserId(String id,String tenantId);

	@Query(value = "SELECT urs.*, ts.path, ts.screen_order, ts.icon, ts.type FROM tbl_user_role_services urs JOIN tbl_screen ts ON urs.screen_id = ts.screen_id WHERE urs.user_id = :userId and urs.tenant_id=:tenantId AND urs.screen_id IS NOT NULL",nativeQuery = true)
	public List<UserRoleService> findByUser(String userId,String tenantId);
	
	@Query(value = "SELECT * FROM tbl_user_role_services where user_id=:userId and tenant_id=:tenantId",nativeQuery = true)
	public List<UserRoleService> findAllByUserId(String userId,String tenantId);
	
	
	
	
}
