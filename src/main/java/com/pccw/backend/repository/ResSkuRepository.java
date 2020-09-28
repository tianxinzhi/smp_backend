package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResSku;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface ResSkuRepository extends BaseRepository<DbResSku> {

    @Query(value = "SELECT  rsa.SPEC_ID spec,  rs.SPEC_NAME specName, ra.ID attr, string_agg(CAST(rav.ID as VARCHAR),',') attrValue, ra.ATTR_NAME attrName, \n" +
            "string_agg(case when rav.attr_value is not null then rav.attr_value  else rav.value_from || '~'||rav.value_to end,',') attrValueName\n" +
            " FROM RES_SPEC_ATTR rsa LEFT JOIN RES_ATTR ra ON ra.ID = CAST(rsa.ATTR_ID as BIGINT)\n" +
            "LEFT JOIN RES_ATTR_VALUE rav ON rav.id = CAST(rsa.ATTR_VALUE_ID as BIGINT) LEFT JOIN \n" +
            "RES_SPEC rs ON rs.id = rsa.SPEC_ID WHERE rsa.SPEC_ID = \n" +
            "(SELECT rtss.SPEC_ID  FROM  RES_TYPE_SKU_SPEC rtss WHERE rtss.is_type = 'Y' and rtss.TYPE_ID = 42)\n" +
            "GROUP BY ra.ATTR_NAME,rsa.SPEC_ID, rs.SPEC_NAME , ra.ID ORDER BY rsa.SPEC_ID ",nativeQuery = true)
    /**
     * 根据type查询spec及其attr相关信息
     */
    List<Map> getAllSpecsByType(@Param("typeId") long typeId);

    @Query(value = "select T3.type_name \"typeName\",T3.id \"type\",t5.spec_name \"specName\",\n" +
            "t5.id \"spec\",t6.attr_name \"attrName\",t6.id \"attr\",\n" +
            " string_agg(DISTINCT(case when t7.attr_value is not null then t7.attr_value else t7.value_from ||'~'||t7.value_to end),',') \"attrValueName\"\n" +
            ", string_agg(CAST(t7.id as VARCHAR),',') \"attrValue\"\n" +
            " from RES_TYPE t3 inner join RES_TYPE_SKU_SPEC t4 on t3.id = t4.type_id \n" +
            "left join res_spec t5 on t5.id = t4.spec_id left join\n" +
            " RES_SPEC_ATTR t2 on T5.ID = T2.Spec_ID left join RES_ATTR t6 \n" +
            "on t6.id = cast(t2.attr_id AS BIGINT) left join RES_ATTR_VALUE \n" +
            "t7 on t7.id = cast(t2.attr_value_id as BIGINT) where t4.sku_id  = ?1 \n" +
            "GROUP BY T3.type_name ,T3.id ,t5.spec_name,t5.id ,t6.attr_name ,t6.id  ",nativeQuery = true)
    /**
     * 根据sku查询type，spec，attr，attrValue信息
     */
    List<Map> getTypeDtlsBySku(@Param("skuId") long skuId);

    List<DbResSku> getDbResSkusBySkuCode(String skuCode);

    DbResSku findFirst1BySkuCode(String sku_id);


    DbResSku findDbResSkuBySkuCode(String skuCode);

}
