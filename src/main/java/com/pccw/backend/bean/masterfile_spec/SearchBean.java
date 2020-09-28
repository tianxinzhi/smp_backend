package com.pccw.backend.bean.masterfile_spec;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import com.pccw.backend.entity.DbResSpecAttr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value = "Spec 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="规格名称",name="specName",example="")
    private String specName;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="规格描述",name="specDesc",example="")
    private String specDesc;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @ApiModelProperty(value="版本",name="verId",example="")
    private String verId;

    private long id;

    private List<Map> attrData;

    private List<DbResSpecAttr> resSpecAttrList;

}