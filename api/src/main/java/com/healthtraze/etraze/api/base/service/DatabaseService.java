package com.healthtraze.etraze.api.base.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            logger.info("Database connection test query result: {}", result);
        } catch (Exception e) {
            logger.error("Database connection error", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        // Any cleanup code can be placed here
    }
}
