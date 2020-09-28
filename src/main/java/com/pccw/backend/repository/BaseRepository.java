package com.pccw.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Repository
@NoRepositoryBean
@Transactional
public interface BaseRepository<T> extends JpaRepository<T,Long>,JpaSpecificationExecutor<T> {

    // Page<DbResRight> findAll(Specification<DbResRight> spec,Pageable p);

    @Modifying
    //@Transactional
    long deleteByIdIn(ArrayList<Long> ids);
}
