package com.pccw.backend.bean.stock_out;


import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value = "Stock Out 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @ApiModelProperty(value="门店id",name="repoId",example="")
    private long fromRepoId;

    @ApiModelProperty(value="类型",name="repoType",example="")
    private String repoType;
    
}