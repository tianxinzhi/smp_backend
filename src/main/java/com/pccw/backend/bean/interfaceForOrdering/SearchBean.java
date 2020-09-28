package com.pccw.backend.bean.interfaceForOrdering;


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
 @ApiModel(value="Stock update模块 - InputBean",description="")
public class SearchBean extends BaseBean {

    @ApiModelProperty(value="订单系统",name="logRepoIn",example="")
    private String order_system;

    @ApiModelProperty(value="sales_id",name="sales_id",example="")
    private String sales_id ;

    @ApiModelProperty(value="sku_code",name="sku_code",example="")
    private String sku_code;

    @ApiModelProperty(value="item_code",name="item_code",example="")
    private String item_code;

    @ApiModelProperty(value="repo_id",name="repo_id",example="")
    private String repo_id;





}