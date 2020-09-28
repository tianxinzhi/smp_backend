package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResProcess;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResProcessRepository extends BaseRepository<DbResProcess> {


    /**
     * @param accountId
     * @param nature
     * @param repoId
     * @param txtNum
     * @param timeBegin
     * @param timeEnd
     * 查询审批进度到当前登陆人的单据
     * @return
     */
    @Query(value = "SELECT RP.id \t\n" +
            " FROM  RES_PROCESS rp,\n" +
            "\t     RES_PROCESS_DTL rpd\n" +
            "WHERE  RPD.PROCESS_ID = RP.ID   \n" +
            "       AND rp.STATUS='PENDING'  \n" +
            "       AND rpd.STATUS='PENDING'  \n" +
            "       AND exists (SELECT * FROM RES_ACCOUNT_ROLE\n" +
            "\t                  WHERE ACCOUNT_ID =?1  and  RPD.ROLE_ID=ROLE_ID )\n" +
            "       AND RP.log_order_nature=nvl(?2,RP.log_order_nature)\n" +
            "       AND RP.repo_id=nvl(?3,RP.repo_id) \n" +
            "       AND RP.log_txt_num LIKE CONCAT(CONCAT('%',?4),'%') \n" +
            "       AND RP.create_at BETWEEN ?5 AND ?6",nativeQuery = true)
    List<Long> findIdsByPending(long accountId, String nature, String repoId, String txtNum, long timeBegin, long timeEnd);

    List<DbResProcess> findDbResProcessesByIdIn(List ids);
}
