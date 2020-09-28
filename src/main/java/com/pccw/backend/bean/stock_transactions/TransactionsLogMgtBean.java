package com.pccw.backend.bean.stock_transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-12 15:30
 **/
@Data
@NoArgsConstructor
public class TransactionsLogMgtBean {

    private long logRepoOut;

    private List<TransactionsLogDtlBean> line;
}
