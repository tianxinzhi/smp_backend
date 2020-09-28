package com.pccw.backend.bean.masterfile_item;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Item 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {
    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="物料ID",name="id",example="")
    private Long id;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="物料编码",name="itemCode",example="")
    private String itemCode;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="物料名称",name="itemName",example="")
    private String itemName;
}
