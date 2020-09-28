package com.pccw.backend.bean.masterfile_type;


import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResClassType;
import com.pccw.backend.entity.DbResTypeSkuSpec;
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
 @ApiModel(value="Type模块 - CreateBean",description="")
public class CreateBean extends BaseBean{
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    @ApiModelProperty(value="类型名称",name="typeName",example="")
    private String typeName;

    @ApiModelProperty(value="类型编码",name="typeCode",example="")
    private String typeCode;

    @ApiModelProperty(value="序列号",name="sequential",example="")
    private String sequential;

    @ApiModelProperty(value="类型描述",name="typeDesc",example="")
    private String typeDesc;

    private List<Long> classId;

    private long specId;
    private String specName;
    private String verId;

//    private List<DbResClass> classList;
    private List<DbResClassType> ClassTypeList;

    private DbResTypeSkuSpec dbResTypeSkuSpec;
}