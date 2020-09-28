package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResSpec;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface ResSpecRepository extends BaseRepository<DbResSpec> {


//    @Query(value = "SELECT rsa.SPEC_ID specId,ra.ATTR_NAME attrName,nvl(rav.attr_value,rav.value_from||'~'||rav.value_to) attrValue from RES_SPEC_ATTR rsa\n" +
//            "LEFT JOIN RES_ATTR ra on ra.ID=rsa.ATTR_ID\n" +
//            "LEFT JOIN RES_ATTR_VALUE rav on rav.id =rsa.ATTR_VALUE_ID\n" +
//            "where rsa.SPEC_ID = ?1  order by rsa.id", nativeQuery = true)
   @Query(value = "SELECT rsa.SPEC_ID specId,ra.ATTR_NAME attrName,coalesce(rav.attr_value,rav.value_from||'~'||rav.value_to) attrValue from RES_SPEC_ATTR rsa\n" +
        "LEFT JOIN RES_ATTR ra on ra.ID=CAST(rsa.ATTR_ID as BIGINT)\n" +
        "LEFT JOIN RES_ATTR_VALUE rav on rav.id =CAST(rsa.ATTR_VALUE_ID as BIGINT)\n" +
        "where rsa.SPEC_ID = ?1  order by rsa.id", nativeQuery = true)
    List<Map> attrSearch(@Param("id") long id);


    List<DbResSpec> findById(long id);

    List<DbResSpec> getDbResSpecsBySpecName(String specName);
}
