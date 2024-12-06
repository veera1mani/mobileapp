package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.TicketStatusHistory;


@Repository
public interface TicketStatusHistoryRepository extends BaseRepository<TicketStatusHistory, String>{

	
	public List<TicketStatusHistory> findByTicketId(String ticketId);
	
	@Query(value = "select * from tbl_ticket_status_history where ticket_id = :ticketId ORDER BY id ASC",nativeQuery = true)
	public List<TicketStatusHistory> findAllByTicketId(String ticketId);
	
	@Query(value = "select * from tbl_ticket_status_history where invoice = :invoice LIMIT 1",nativeQuery = true)
	public Optional<TicketStatusHistory> findAByinvoiceNumber(String invoice);
	@Query(value = "select * from tbl_ticket_status_history where invoice = :invoice ",nativeQuery = true)
	public List<TicketStatusHistory> findAByinvoiceNumbers(String invoice);

	@Query(value="select * from tbl_ticket_status_history where invoice=:invoice and tenant_id=:tenantId and status='DELIVERED'",nativeQuery = true)
	public Optional<TicketStatusHistory> finddeliverytimeline(String invoice, String tenantId);
	
}
