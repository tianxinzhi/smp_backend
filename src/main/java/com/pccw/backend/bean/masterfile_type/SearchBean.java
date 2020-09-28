package com.pccw.backend.bean.masterfile_type;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import com.pccw.backend.entity.DbResClassType;
import com.pccw.backend.entity.DbResTypeSkuSpec;
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
 @ApiModel(value = "Type 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    @ApiModelProperty(value="类型名称",name="typeName",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String typeName;

    @ApiModelProperty(value="类型编码",name="typeCode",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String typeCode;

    @ApiModelProperty(value="序列号",name="sequential",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String sequential;

    @ApiModelProperty(value="类型描述",name="typeDesc",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String typeDesc;

    private long id;
    private long specId;
    private String specName;
    private String verId;
    private List<Long> classId;
    private String className;
    private List<DbResClassType> relationOfTypeClass;
    private DbResTypeSkuSpec dbResTypeSkuSpec;
    private List<Map> attrData;
    private List<Map> typeSkuList;
    private String createAccountName;
    private String updateAccountName;
    
}