package com.healthtraze.etraze.api.security.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.DeviceMetadata;

@Repository
public interface DeviceMetadataRepository extends BaseRepository<DeviceMetadata, Long> {

    List<DeviceMetadata> findByUserId(String userId);
}
