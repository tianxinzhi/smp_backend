package com.pccw.backend.bean.masterfile_trx_type;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Trx_Type模块 - SearchBean",description="")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="属性值",name="transactionTypeName",example="")
    private String transactionTypeName;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="action",name="transactionAction",example="")
    private String transactionAction;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="来源",name="transactionSource",example="")
    private String transactionSource;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="来源",name="natureType",example="")
    private String natureType;
}
