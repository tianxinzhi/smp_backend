package com.pccw.backend.bean.masterfile_sku;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Sku模块 - SearchBean",description="")
public class SearchBean extends BaseSearchBean {

    //@PredicateAnnotation(type = PredicateType.EQUEL)
    //private String classId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="sku名",name="skuName",example="")
    private String skuName;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="sku编码",name="skuCode",example="")
    private String skuCode;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="sku详情",name="skuDesc",example="")
    private String skuDesc;
//    private List<DbResSkuType> skuTypeList;
}
