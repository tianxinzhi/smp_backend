package com.pccw.backend.bean.masterfile_workorder;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "WorkOrder 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {
    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="工单ID",name="id",example="")
    private Long id;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="工单编码",name="woCode",example="")
    private String woCode;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="工单名称",name="woName",example="")
    private String woName;
}
