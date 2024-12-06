package com.healthtraze.etraze.api.security.service;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
