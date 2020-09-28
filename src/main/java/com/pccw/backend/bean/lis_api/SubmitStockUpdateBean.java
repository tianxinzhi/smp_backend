package com.pccw.backend.bean.lis_api;

import lombok.Data;

/**
 * @Author: xiaozhi
 * @Date: 2020/6/1 12:00
 * @Desc: 3.1.1-10u
 */
@Data
public class SubmitStockUpdateBean {

    private String P_API_VERSION; //1.0
    private String P_INIT_MSG_LIST; //T
    private String P_COMMIT; //T
    private String P_RECORD_TYPE;
    private String P_SOURCE_TYPE;
    private String P_FROM_WAREHOUSE;
    private String P_TRANSACTION_DATE;
    private String P_TRANSACTION_QTY;
    private String P_TRANSACTION_TYPE; //ADJ-IN,ADJ-OUT,INTRANSIT,ISSUE,RECEIPT,SUBINV
    private String P_ITEM_CODE;
    private String P_COST_CODE;
    private String P_WORK_ORDER;
    private String P_BUSINESS_DATE;
    private String P_TO_WAREHOUSE;
    private String P_SHIPMENT_NUM;
    private String P_SOURCE_REF;
    private String P_WAYBILL;
    private String P_SO_TYPE_GROUPING;

}
