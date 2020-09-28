package com.pccw.backend.bean.stock_replenishment;


import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResStockReplenishment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="StockReplenishment 模块 - CreateReplBean",description="")
public class CreateReplBean extends BaseBean {

    @ApiModelProperty(value="",name="logTxtNum",example="")
    private String logTxtNum;

    @ApiModelProperty(value="",name="fromChannelId",example="")
    private Long fromChannelId;

    @ApiModelProperty(value="",name="status",example="")
    private String status;

    private List<DbResStockReplenishment> line;
}
