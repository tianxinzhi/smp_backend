package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResLogMgtDtlSerial;
import com.pccw.backend.entity.DbResSkuRepoSerial;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface ResSkuRepoSerialRepository extends BaseRepository<DbResSkuRepoSerial> {
    @Query(value = "select T1.id \"id\" , T1.SKU_REPO_ID \"skuRepoId\",t1.COURIER \"courier\",t1.serial \"serial\",t1.expiry_date \"expiryDate\" from RES_SKU_REPO_SERIAL t1 inner join  RES_SKU_REPO t2 on  \n" +
            "t1.SKU_REPO_ID = T2.id  where T2.SKU_ID = ?1 and T2.REPO_ID = ?2 ",nativeQuery = true)
   List<Map> getBalanceSerials(@Param("skuId")Long skuId, @Param("repoId")Long repoId);
}
