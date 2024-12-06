package com.healthtraze.etraze.api.security.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, String> {



	public Optional<User> findByUserId(String userId);

	public Optional<User> findById(String userId);

	public Optional<User> findByPhoneNo(String phoneNo);

	public void deleteByUserId(String userId);

	public List<User> findByRoleId(String roleId);

	public User findByTenantIdAndRoleId(String tenantId, String roleId);

	public List<User> findByStatus(String status);
	@Query(value= "select * from tbl_user where tenant_id = :orgId and role_name!='STOCKIST' and role_name!='TRANSPORT'",nativeQuery = true)
	public List<User> findByTenantId(String orgId);
	
	@Query(value = "select * from tbl_user where tenant_id = :tenantId and role_id = :roleId",nativeQuery = true)
	public Optional<User> findUserByRoleAndTenant(@Param("tenantId") String tenantId,@Param("roleId") String roleId);
	
	@Query(value = "select * from tbl_user where tenant_id = :tenantId and role_name!='STOCKIST' and role_name!='TRANSPORT' AND role_name!='MANUFACTURER' and (user_id ILIKE %:search% OR first_name ILIKE %:search% OR last_name ILIKE %:search% OR email ILIKE %:search% OR phone_no ILIKE %:search% OR role_name ILIKE %:search% OR status ILIKE %:search%)",nativeQuery = true)
	public List<User> findUserAllByTenant(@Param("tenantId") String tenantId,@Param("search") String search,Pageable paging);
	
	@Query(value = "select * from tbl_user where tenant_id = :tenantId and role_name!='STOCKIST' and role_name!='TRANSPORT' and (user_id ILIKE %:search% OR first_name ILIKE %:search% OR last_name ILIKE %:search% OR email ILIKE %:search% OR phone_no ILIKE %:search% OR role_name ILIKE %:search% OR status ILIKE %:search%)",nativeQuery = true)
	public List<User> findUserAllByTenant(@Param("tenantId") String tenantId,@Param("search") String search);

	@Query(value="SELECT * FROM tbl_user WHERE hierarchy_id =:hierarchyId",nativeQuery = true)
	public List<User> finduserByTenantId(String hierarchyId);
	
	@Query(value="SELECT * FROM tbl_user u JOIN tbl_role r ON r.role_id = u.role_id WHERE r.role_name = 'MANAGER' and u.tenant_id =:tenantId ", nativeQuery = true)
	public List<User> findManagers(String tenantId);

	@Query(value = "SELECT RIGHT(user_id,4) FROM tbl_user WHERE tenant_id =:tenantId ORDER BY user_id DESC LIMIT 1", nativeQuery = true)
	BigInteger getNextSequentialNumberForUser(@Param("tenantId") String tenantId);
	
	@Query(value = "SELECT RIGHT(user_id, 4) FROM tbl_user WHERE role_name = 'SUPERADMIN' ORDER BY user_id ASC LIMIT 1", nativeQuery = true)
	String getNextSequentialNumberForTenant();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tbl_user SET isenable=false WHERE user_id=:user_id",nativeQuery = true)
	void deactivateUserDeactive(@Param("user_id") String user_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tbl_user SET isenable=true WHERE user_id=:user_id",nativeQuery = true)
	void deactivateUserActive(@Param("user_id") String user_id);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tbl_user SET status=:status WHERE user_id=:userId AND tenant_id=:tenantId",nativeQuery = true)
	void setUserStatus(@Param("userId") String userId,@Param("tenantId") String tenantId,@Param("status") String status);


	Optional<User> email(String email);

	
}
