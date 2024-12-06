package com.healthtraze.etraze.api.security.repository;

import java.util.Date;
import java.util.stream.Stream;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.VerificationToken;

public interface VerificationTokenRepository extends BaseRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

}
