package com.pccw.backend.bean.stock_replenishment;


import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="StockReplenishment 模块 - EditBean",description="")
public class EditBean extends CreateReplBean {

    @ApiModelProperty(value="id",name="id",example="")
    private long id;

}
