package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResLogMgtDtl;
import com.pccw.backend.entity.DbResLogMgtDtlSerial;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResLogMgtDtlSerialRepository extends BaseRepository<DbResLogMgtDtlSerial> {

    @Query(value = "select * from res_log_mgt_dtl_serial where log_mgt_dtl_id = ?1",nativeQuery = true)
    List<DbResLogMgtDtlSerial> findAllByDtlId(Long MgtDtlId);
}
