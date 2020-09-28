package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResRoleRight;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResRoleRightRepository extends BaseRepository<DbResRoleRight> {

    List<DbResRoleRight> getDbResRoleRightsByRightId(Long rightId);
}
