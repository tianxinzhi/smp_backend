package com.pccw.backend.ctrl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: xiaozhi
 * @Date: 2020/8/17 17:00
 * @Desc:
 */
public class BaseStockCtrl<T> extends BaseCtrl<T>{

   public String genTranNum(Date date, String tpye, String storeNameFrom) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        String strDate = format.format(date);
        System.out.println(strDate);
        String prefix = strDate.substring(2, 8);
        String suffix = strDate.substring(9);
        String transationNumber = prefix + tpye + storeNameFrom  + suffix;
        return transationNumber;
    }
}
