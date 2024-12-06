package com.healthtraze.etraze.api.file.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.file.model.File;

@Repository
public interface FileRepository extends BaseRepository<File, String> {
	
	public List<File> findByMappingId(String mappingId);
	
}
