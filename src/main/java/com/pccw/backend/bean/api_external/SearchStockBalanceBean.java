package com.pccw.backend.bean.api_external;

import com.pccw.backend.bean.stock_balance.SearchBean;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-02 15:52
 **/
@Data
@NoArgsConstructor
public class SearchStockBalanceBean {

    private String skuNum;

    private String channelCode;
}
