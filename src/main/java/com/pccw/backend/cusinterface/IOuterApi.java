package com.pccw.backend.cusinterface;

/**
 * @Author: xiaozhi
 * @Date: 2020/8/18 10:41
 * @Desc:
 */
public interface IOuterApi {

    /**
     * 给第三方api调用
     * @param o
     * @param logTxtNum
     * @return
     */
    String outerApi(Object o,String logTxtNum);
}
