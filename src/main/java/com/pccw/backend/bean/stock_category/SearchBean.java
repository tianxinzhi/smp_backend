package com.pccw.backend.bean.stock_category;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
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
 @ApiModel(value = "TypeSkuSpec 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="商店ID",name="repoId",example="")
    private Long repoId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="skuId",name="skuId",example="")
    private Long skuId;

    private Long id;
    private String repoCode;
    private String skuCode;
    private Long stockTypeId;
    private String stockTypeName;
    private Integer qty;
    private String skuDesc;
    private String createAccountName;
    private String updateAccountName;

    
}