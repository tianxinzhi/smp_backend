package com.pccw.backend.bean.masterfile_spec_attr;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Spec_Attr模块 - SearchBean",description="")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="specId",name="specId",example="")
    private String specId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="verId",name="verId",example="")
    private String verId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="attrId",name="attrId",example="")
    private String attrId;

}
