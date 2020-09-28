package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.entity.DbResSkuType;

import java.util.List;


public interface ResSkuTypeRepository extends BaseRepository<DbResSkuType> {

  DbResSkuType findBySkuAndTypeId(DbResSku sku, long typeId);

    DbResSkuType findBySku(DbResSku sku);

    List<DbResSkuType> getDbResSkuTypesByTypeId(Long id);
}
