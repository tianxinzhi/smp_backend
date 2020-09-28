package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResReservation;
import com.pccw.backend.entity.DbResStockReturn;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResStockReturnRepository extends BaseRepository<DbResStockReturn> {
    List<DbResStockReturn> findDbResStockReturnsByActiveEquals(String active,Sort sort);
}

