package com.healthtraze.etraze.api.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.OneTimePassword;

@Repository
public interface OneTimePasswordRepository extends BaseRepository<OneTimePassword, Long> {

	OneTimePassword findByOtp(String otp);
	
	@Query(value = "SELECT * FROM tbl_one_time_password where user_id =:userId LIMIT 1",nativeQuery = true)
	OneTimePassword findByUserId(String userId);

	OneTimePassword findByEmailId(String emailId);


}
