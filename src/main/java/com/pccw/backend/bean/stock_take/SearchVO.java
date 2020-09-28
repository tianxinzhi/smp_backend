package com.pccw.backend.bean.stock_take;

import com.pccw.backend.entity.DbResStockTake;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-08-04 09:44
 **/
@Data
@NoArgsConstructor
public class SearchVO extends DbResStockTake {

    private String ChannelCode;
}
