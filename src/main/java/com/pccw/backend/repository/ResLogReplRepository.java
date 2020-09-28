package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResLogRepl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ResLogReplRepository extends BaseRepository<DbResLogRepl> {

    @Query(value = "SELECT\n" +
            "RLR.REMARK,RLR.LOG_BATCH_ID,RLR.REPO_ID_FROM Log_Repo_Out,RLR.REPO_ID_TO Log_Repo_In,RLR.LOG_TXT_NUM LOG_TXT_BUM,RLR.CREATE_AT,\n" +
            "(SELECT RES_REPO.REPO_CODE FROM RES_REPO WHERE RES_REPO.id=RLR.REPO_ID_FROM) log_Repo_Form,\n" +
            "(SELECT RES_REPO.REPO_CODE FROM RES_REPO WHERE RES_REPO.id=RLR.REPO_ID_TO) log_Repo_To,\n" +
            "(SELECT RES_SKU.SKU_CODE FROM RES_SKU WHERE RES_SKU.id=RLRD.DTL_SKU_ID) SKU_CODE,\n" +
            "(SELECT RES_SKU.SKU_DESC FROM RES_SKU WHERE RES_SKU.id=RLRD.DTL_SKU_ID) SKU_DESC,\n" +
            "RLRD.DTL_QTY,DTL_SKU_ID,RLR.LOG_DN_NUM\n" +
            " FROM RES_LOG_REPL rlr,RES_LOG_REPL_DTL rlrd\n" +
            "WHERE\n" +
            "RLR.id=RLRD.DB_RES_LOG_REPL_ID\n" +
            "and RLR.LOG_BATCH_ID=nvl(?1,RLR.LOG_BATCH_ID)\n" +
            "and RLR.LOG_DN_NUM = nvl(?2,RLR.LOG_DN_NUM)\n" +
            "and RLRD.STATUS IS NULL\n" +
            "and RLRD.DTL_ACTION IS NULL",nativeQuery = true)
    List<Map> getReplenishmentInfo(String bathcID, String dnNum);

    DbResLogRepl findDbResLogReplByLogTxtBum(String logTxtBum);
}
