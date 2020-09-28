package com.pccw.backend.bean.masterfile_class;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import com.pccw.backend.entity.DbResClassType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value = "Class 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="分类名称",name="className",example="")
    private String className;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="分类描述",name="classDesc",example="")
    private String classDesc;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="分类类型",name="classType",example="")
    private String classType;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="父级Id",name="parentClassId",example="")
    private String parentClassId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    private long id;
    private String createAccountName;
    private String updateAccountName;
    List<DbResClassType> relationOfTypeClass;
    
}