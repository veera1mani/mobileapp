package com.healthtraze.etraze.api.security.repository;

import java.util.Date;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.PasswordResetToken;
import com.healthtraze.etraze.api.security.model.User;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);


}
