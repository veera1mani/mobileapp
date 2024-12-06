package com.healthtraze.etraze.api.base.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.model.Event;

@Repository
public interface EventRepository extends  BaseRepository<Event, Long> {
	
	
	
	
	public List<Event> findByVeteranId(String veteranId);
	
	

}
