package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResStockReplenishment;
import com.pccw.backend.entity.DbResStockReplenishmentHeader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResStockReplenishmentHeaderRepository extends BaseRepository<DbResStockReplenishmentHeader> {
}

