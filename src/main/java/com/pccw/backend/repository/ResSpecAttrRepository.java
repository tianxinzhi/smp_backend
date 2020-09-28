package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResSpecAttr;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResSpecAttrRepository extends BaseRepository<DbResSpecAttr> {
    List<DbResSpecAttr> findDbResSpecAttrsByAttrId(String id);
}
