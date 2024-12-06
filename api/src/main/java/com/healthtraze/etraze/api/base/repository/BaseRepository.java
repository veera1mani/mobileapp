package com.healthtraze.etraze.api.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseRepository<T, T1> extends JpaRepository<T, T1>{

}
