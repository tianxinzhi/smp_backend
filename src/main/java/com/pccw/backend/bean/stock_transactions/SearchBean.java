package com.pccw.backend.bean.stock_transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-16 10:55
 **/
@Data
@NoArgsConstructor
public class SearchBean {

    private String nature;

    private long skuId;
}
