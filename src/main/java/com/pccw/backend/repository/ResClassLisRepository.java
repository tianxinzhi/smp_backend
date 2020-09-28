package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResClassLis;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResClassLisRepository extends BaseRepository<DbResClassLis>{
    List<DbResClassLis> getDbResClassLissByClassDesc(String classDesc);
}
