package com.healthtraze.etraze.api.masters.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ReturnAttachment;


@Repository
public interface ReturnAttachmentRepository extends BaseRepository<ReturnAttachment, String>{

	
	public List<ReturnAttachment> findBySerialNumber(String serialNumber);
	
//	public List<ReturnAttachment> findByReturnId(String returnId);


}
