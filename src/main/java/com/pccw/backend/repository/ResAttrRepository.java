package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAttr;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResAttrRepository extends BaseRepository<DbResAttr> {
    List<DbResAttr> getDbResAttrsByAttrName(String attrName);
}
