package com.pccw.backend.cusinterface;

import com.pccw.backend.repository.BaseRepository;

/**
 * @Author: xiaozhi
 * @Date: 2019/12/17 10:52
 * @Desc:
 */
public interface ICheck {

    /**
     * 检查数据是否被占用，返回占用的id
     * @param obj
     * @param check 需要查询用的BaseRepo，支持多个
     * @return
     */
    long checkCanDisable(Object obj, BaseRepository... check);
}
