package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResStockReplenishment;
import com.pccw.backend.entity.DbResStockReturn;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResStockReplenishmentRepository extends BaseRepository<DbResStockReplenishment> {
    List<DbResStockReplenishment> getDbResStockReplenishmentsByActiveEquals(String active,Sort sort);
}

