package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResRepo;


public interface ResRepoRepository extends BaseRepository<DbResRepo> {


    DbResRepo findFirst1ByRepoCode(String repo_id);

    DbResRepo findDbResRepoByRepoCode(String repoCode);
}
