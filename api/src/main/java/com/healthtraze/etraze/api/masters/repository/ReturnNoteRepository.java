package com.healthtraze.etraze.api.masters.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.ReturnNote;


@Repository
public interface ReturnNoteRepository extends BaseRepository<ReturnNote, String>{

	
	public List<ReturnNote> findBySerialNumber(String serialNumber);
	
	public List<ReturnNote> findByReturnId(String returnId);
	
	@Query(value = "select * from tbl_return_note where return_id =:serialNumber And note_type =:noteType And tenant_id=:tenantId",nativeQuery = true)
	public List<ReturnNote> findBySerialNumber(String serialNumber,String noteType,String tenantId);
	
	@Query(value = "select * from tbl_return_note where return_id =:returnId And note_type ='GRRN' AND claim_type='NonSaleable' And tenant_id=:tenantId",nativeQuery = true)
	public List<ReturnNote> findAllNonSalableGRRNByReturnId(String returnId,String tenantId);
	
	@Query(value = "select * from tbl_return_note where return_id =:returnId And note_type ='GRRN' And tenant_id=:tenantId",nativeQuery = true)
	public List<ReturnNote> findAllGRRNByReturnId(String returnId,String tenantId);
	
	@Query(value = "select * from tbl_return_note where id =:id And tenant_id=:tenantId",nativeQuery = true)
	public Optional<ReturnNote> findById(String id,String tenantId);
	
	@Query(value = "SELECT * FROM public.tbl_return_note where tenant_id=:tenantId AND return_id=:returnId AND note_type='CN' AND note_number=:noteNumber LIMIT 1",nativeQuery = true)
	public Optional<ReturnNote> findCnDuplicate(String tenantId,String returnId,String noteNumber);
	
	@Query(value = "select * from tbl_return_note rn LEFT JOIN tbl_returns r ON r.return_id = rn.return_id where rn.note_type='GRRN' AND rn.tenant_id=:tenantId AND r.manufacturer=:manufacturer AND rn.note_number=:grrnNumber LIMIT 1",nativeQuery = true)
	public Optional<ReturnNote> findGrrnNumberDuplicateByManufacturer(String tenantId,String manufacturer,String grrnNumber);
	

}
