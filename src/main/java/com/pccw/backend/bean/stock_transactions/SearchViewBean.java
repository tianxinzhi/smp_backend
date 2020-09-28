package com.pccw.backend.bean.stock_transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-13 19:19
 **/
@Data
@NoArgsConstructor
public class SearchViewBean {

    private long id;

    private String itemCode;

    private String fromStockType;

    private String toStockType;

    private String type;

    private long qty;

    private String reference;



}
