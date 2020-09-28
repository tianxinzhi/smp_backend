package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAttrValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResAttrValueRepository extends BaseRepository<DbResAttrValue> {
    List<DbResAttrValue> getDbResAttrValuesByAttrValue(String attrValue);
}
