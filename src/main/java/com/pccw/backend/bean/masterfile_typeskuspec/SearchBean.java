package com.pccw.backend.bean.masterfile_typeskuspec;


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
    @ApiModelProperty(value="类型标识",name="specName",example="")
    private String typeId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="sku标识",name="specName",example="")
    private String skuId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="规格标识",name="specId",example="")
    private String specId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;
    
}