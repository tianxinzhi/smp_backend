package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAttrAttrValue;
import com.pccw.backend.entity.DbResAttrValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ResAttrAttrValueRepository extends BaseRepository<DbResAttrAttrValue>{
    @Query(value = "SELECT * FROM res_attr_attr_value",nativeQuery = true)
   List<Map> searchAll();

    List<DbResAttrAttrValue> findDbResAttrAttrValuesByAttrValue(DbResAttrValue attrValue);
}
