package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResLogRor;


public interface ResLogRorRepository extends BaseRepository<DbResLogRor> {

    DbResLogRor findDbResLogRorByLogOrderId(String logTxtBum);

}
