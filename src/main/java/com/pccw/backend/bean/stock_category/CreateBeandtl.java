package com.pccw.backend.bean.stock_category;


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
 @ApiModel(value="Sku_Repo模块 - CreateBean",description="")
public class CreateBeandtl extends BaseBean {

    @ApiModelProperty(value="skuId",name="skuId",example="")
    private Long skuId;

    @ApiModelProperty(value="商品类型ID",name="stockTypeId",example="")
    private Long fromStockTypeId;

    @ApiModelProperty(value="商品数量",name="qty",example="")
    private Integer qty;

    @ApiModelProperty(value="itemId",name="itemId",example="")
    private String itemId;

    @ApiModelProperty(value="商品类型ID",name="toStockTypeId",example="")
    private Long toStockTypeId;


    
}