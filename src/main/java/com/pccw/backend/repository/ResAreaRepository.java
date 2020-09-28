package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ResAreaRepository extends JpaRepository<DbResArea, Long>,JpaSpecificationExecutor {

}
