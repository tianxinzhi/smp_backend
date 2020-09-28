package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResLogMgt;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ResStockInRepository extends BaseRepository<DbResLogMgt>{

    @Query(value = "SELECT RLM.LOG_TXT_NUM log_Txt_Bum,RLM.CREATE_AT ,\n" +
            "(SELECT RES_REPO.REPO_CODE FROM RES_REPO WHERE RES_REPO.id=RLM.LOG_REPO_IN) as LOG_REPO_to,\n" +
            "(SELECT RES_REPO.id FROM RES_REPO WHERE RES_REPO.id=RLM.LOG_REPO_IN) as LOG_REPO_IN,\n" +
            "(SELECT RES_REPO.REPO_CODE FROM RES_REPO WHERE RES_REPO.id=RLM.LOG_REPO_OUT) as LOG_REPO_form,\n" +
            "(SELECT RES_REPO.id FROM RES_REPO WHERE RES_REPO.id=RLM.LOG_REPO_OUT) as LOG_REPO_OUT,\n" +
            "(SELECT RES_SKU.SKU_CODE FROM RES_SKU WHERE RES_SKU.id = RLMD.DTL_SKU_ID) as SKU_CODE,\n" +
            "(SELECT RES_SKU.SKU_DESC FROM RES_SKU WHERE RES_SKU.id = RLMD.DTL_SKU_ID) as SKU_DESC,\n" +
            "RLMD.DTL_QTY DTL_QTY,RLM.REMARK,RLMD.DTL_SKU_ID \n" +
            "FROM RES_LOG_MGT rlm,RES_LOG_MGT_DTL rlmd\n" +
            "where \n" +
            "rlm.id = rlmd.LOG_MGT_ID\n" +
            "and RLMD.DTL_SUBIN = 'Intra'\n" +
            "and RLMD.DTL_ACTION = 'A'\n" +
            "and RLMD.STATUS = 'INT'\n" +
            "and rlm.LOG_TXT_NUM = ?1",nativeQuery = true)
    List<Map> getStockOutInfo(String logTxtNum);

    List<DbResLogMgt> findAllByLogOrderNatureInAndLogRepoInIn(List<String> nature, List repoIds);

    List<DbResLogMgt> findAllByLogOrderNatureInAndLogRepoInInAndDeliveryNumberLike(List<String> nature, List repoIds,String deliveryNumber);

    DbResLogMgt findDbResLogMgtByLogTxtBum(String logTxtBum);
}
