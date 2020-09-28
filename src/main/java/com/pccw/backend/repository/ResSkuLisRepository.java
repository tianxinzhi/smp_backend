package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.entity.DbResSkuLis;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResSkuLisRepository extends BaseRepository<DbResSkuLis>{

    List<DbResSkuLis> getDbResSkuLissBySkuCode(String skuCode);

    DbResSkuLis getDbResSkuLisBySkuId(DbResSku skuId);

}
