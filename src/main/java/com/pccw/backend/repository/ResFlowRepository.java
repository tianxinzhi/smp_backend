package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResFlow;


/**
 * ResFlowRepository
 */
public interface ResFlowRepository extends BaseRepository<DbResFlow>{

    DbResFlow findByFlowNature(String logOrderNature);

    // Page<DbResRole> findAll(Specification<DbResRole> spec,Pageable p);  
}