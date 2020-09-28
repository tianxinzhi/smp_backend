package com.pccw.backend.bean.stock_balance;




import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Shop模块 - EditBean",description="")
public class StockEditBean extends StockCreateBean {

    private Long id;

}
