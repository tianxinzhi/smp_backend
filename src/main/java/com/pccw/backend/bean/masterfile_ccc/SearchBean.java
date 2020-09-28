package com.pccw.backend.bean.masterfile_ccc;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Ccc 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {
    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="CCCID",name="id",example="")
    private Long id;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="CCC编码",name="cccCode",example="")
    private String cccCode;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="CCC名称",name="cccName",example="")
    private String cccName;
}
