package com.pccw.backend.repository;



import com.pccw.backend.entity.DbResItem;


public interface ResItemRepository extends BaseRepository<DbResItem> {

    DbResItem findFirst1ByItemCode(String item_id);
}

