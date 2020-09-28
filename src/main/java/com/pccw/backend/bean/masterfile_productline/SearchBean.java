package com.pccw.backend.bean.masterfile_productline;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "ProductLine 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {
    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="生产线ID",name="id",example="")
    private Long id;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="生产线编码",name="plCode",example="")
    private String plCode;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="生产线名称",name="plName",example="")
    private String plName;
}
