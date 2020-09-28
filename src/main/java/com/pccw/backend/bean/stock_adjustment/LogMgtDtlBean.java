package com.pccw.backend.bean.stock_adjustment;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_Adjustment子单模块 - LogMgtDtlBean",description="")
public class LogMgtDtlBean extends BaseBean {


    @ApiModelProperty(value="item",name="item",example="")
    private long dtlItemId;

    @ApiModelProperty(value="sku",name="sku",example="")
    private long dtlSkuId;

    @ApiModelProperty(value="catalog",name="catalog",example="")
    private long catalog;

    @ApiModelProperty(value="description",name="description",example="")
    private String description;

    @ApiModelProperty(value="qty",name="调整数量",example="")
    private long dtlQty;


}
