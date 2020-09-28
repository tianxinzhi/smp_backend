package com.pccw.backend.bean.stock_transactions;

import com.pccw.backend.bean.BaseBean;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-12 16:18
 **/
@Data
@NoArgsConstructor
public class CreateBean extends BaseBean {

//  dtlQty: "1"
//dtlSkuId: "2"
//fromStockTypeId: "1"
//logRepoOut: "4"
//status: "OUT"
//toStockTypeId: "3"
    private long dtlQty;

    private long dtlSkuId;

    private String fromStockTypeId;

    private String toStockTypeId;

    private long logRepoOut;

    private String logOrderNature;
}
