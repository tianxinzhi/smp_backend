package com.pccw.backend.bean.masterfile_attr_value;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Attr_Value模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="属性名",name="attrId",example="")
    private String attrId;

    @ApiModelProperty(value="属性值",name="attrValue",example="")
    private String attrValue;

    @ApiModelProperty(value="单位",name="unitOfMeasure",example="")
    private String unitOfMeasure;

    @ApiModelProperty(value="来源",name="valueFrom",example="")
    private String valueFrom;

    @ApiModelProperty(value="作用",name="valueTo",example="")
    private String valueTo;

    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;
}
