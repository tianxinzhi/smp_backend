package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAccountRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResAccountRoleRepository extends BaseRepository<DbResAccountRole> {

    List<DbResAccountRole> getDbResAccountRolesByRoleId(Long roleId);
}
