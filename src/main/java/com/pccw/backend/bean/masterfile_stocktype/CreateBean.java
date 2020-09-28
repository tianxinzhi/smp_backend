package com.pccw.backend.bean.masterfile_stocktype;


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
 @ApiModel(value="StockType 模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="商品类型名称",name="stockTypeName",example="")
    private String stockTypeName;

    
}