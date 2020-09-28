package com.pccw.backend.bean.masterfile_class;


import com.pccw.backend.bean.BaseBean;
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
 @ApiModel(value="Class模块 - CreateBean",description="")
public class CreateBean extends BaseBean{
    @ApiModelProperty(value="分类名称",name="className",example="")
    private String className;

    @ApiModelProperty(value="分类描述",name="classDesc",example="")
    private String classDesc;

    @ApiModelProperty(value="类型",name="classType",example="")
    private String classType;

    @ApiModelProperty(value="父级Id",name="parentClassId",example="")
    private String parentClassId;
    
    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    private List<DbResClassType> ClassTypeList;
    
}