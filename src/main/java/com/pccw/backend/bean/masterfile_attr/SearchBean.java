package com.pccw.backend.bean.masterfile_attr;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Attr 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="属性名称",name="attrName",example="")
    private String attrName;

//    @PredicateAnnotation(type = PredicateType.LIKE)
//    @ApiModelProperty(value="属性值",name="attrValue",example="")
//    private String attrValue;

//    @PredicateAnnotation(type = PredicateType.LIKE)
//    @ApiModelProperty(value="是否有效",name="active",example="")
//    private String active;

}
