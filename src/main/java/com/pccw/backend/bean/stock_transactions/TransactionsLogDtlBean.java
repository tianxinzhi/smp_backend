package com.pccw.backend.bean.stock_transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-12 15:31
 **/
@Data
@NoArgsConstructor
public class TransactionsLogDtlBean {

    private long dtlSkuId;//Stock Item

    private long dtlQty;//Transaction Quantity

    private String status;//Transaction Type

    private String dtlSubin;

    private String dtlAction;

    private long toStockTypeId;

    private long fromStockTypeId;

}
