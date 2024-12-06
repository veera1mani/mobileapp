package com.healthtraze.etraze.api.masters.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.masters.model.DeadlineList;

@Repository
public interface DeadlineListRepository extends BaseRepository<DeadlineList,Long> {

}
