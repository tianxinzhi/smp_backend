package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResClass;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResClassRepository extends BaseRepository<DbResClass> {
	
	//  DbResClass findByClassName(String className);
    List<DbResClass> getDbResClasssByClassName(String className);

    List<DbResClass> getDbResClasssByParentClassId(String parentClassId);
}
