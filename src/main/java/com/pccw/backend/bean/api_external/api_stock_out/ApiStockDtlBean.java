package com.pccw.backend.bean.api_external.api_stock_out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-02 17:20
 **/
@Data
public class ApiStockDtlBean{
    @ApiModelProperty(value="qty",name="qty",example="100")
    private long dtlQty;

    @ApiModelProperty(value="skuCode",name="skuCode",example="SkuCode-01")
    private String dtlSkuCode;

}
