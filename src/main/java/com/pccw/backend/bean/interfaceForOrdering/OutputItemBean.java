package com.pccw.backend.bean.interfaceForOrdering;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @AllArgsConstructor
 @ApiModel(value="Stock update模块 - InputBean",description="")
public class OutputItemBean {

    private String detail_id;

    private String sku_id;

    private String quantity;

    private String item_id;

    private String repo_id;

    private String ccc;

    private String wo;


}