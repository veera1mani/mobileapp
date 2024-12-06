package com.healthtraze.etraze.api.security.repository;

import org.springframework.stereotype.Repository;

import com.healthtraze.etraze.api.base.repository.BaseRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.model.UserLocation;

@Repository
public interface UserLocationRepository extends BaseRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);

}
