package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResRight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * ResRightRepository
 */

@Repository
public interface ResRightRepository extends BaseRepository<DbResRight> {

    List<DbResRight> findByRightPid(Long pid);

    DbResRight findDbResRightById(Long pid);

    @Query(value = "SELECT\n" +
            "\tRR.*\n" +
            "FROM\n" +
            "\tRES_RIGHT rr START WITH RR. ID IN ?1 CONNECT BY PRIOR RR.RIGHT_PID = RR. ID\n" +
            "UNION\n" +
            "\tSELECT\n" +
            "\t\tRR.*\n" +
            "\tFROM\n" +
            "\t\tRES_RIGHT rr START WITH RR.RIGHT_PID IN ?1 CONNECT BY PRIOR RR. ID = RR.RIGHT_PID",nativeQuery = true)
    List<Map<String,Object>> findRightTreeByIds(List<Long> ids);
}
