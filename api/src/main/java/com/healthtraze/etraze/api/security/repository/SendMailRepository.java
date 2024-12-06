package com.healthtraze.etraze.api.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.SendMail;

@Repository
public interface SendMailRepository  extends BaseRepository<SendMail, Long>{

	public Optional<List<SendMail>> findByStatus(@Param(Constants.STATUS) String status);
}
