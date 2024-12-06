package com.healthtraze.etraze.api.security.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.NewLocationToken;
import com.healthtraze.etraze.api.security.model.UserLocation;

@Repository
public interface NewLocationTokenRepository extends BaseRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}
