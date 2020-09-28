package com.pccw.backend.bean.masterfile_attr;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Attr模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="属性名",name="attrName",example="")
    private String attrName;

    @ApiModelProperty(value="属性详情",name="attrDesc",example="")
    private String attrDesc;

    @ApiModelProperty(value="属性值",name="attrValue",example="")
    private String[] attrValues;

//    @ApiModelProperty(value="是否有效",name="active",example="")
//    private String active;
}
