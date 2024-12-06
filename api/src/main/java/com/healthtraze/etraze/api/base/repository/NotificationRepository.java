package com.healthtraze.etraze.api.base.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.model.Notification;

@Repository
public interface NotificationRepository extends  BaseRepository<Notification, Long> {
	 
	public List<Notification> findByUserId(String userId);

 	public List<Notification> findByAssetId(String assetId);
	
	

}
