package com.healthtraze.etraze.api.masters.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ChequeCardView;

public interface ChequeCardViewRepository extends BaseRepository<ChequeCardView,String> {
	
	
	@Query(value = "SELECT * FROM cheque_card_view where tenantid=:tenantId AND userid=:userId",nativeQuery = true)
	Optional<ChequeCardView> getByUser(String tenantId,String userId);

}
