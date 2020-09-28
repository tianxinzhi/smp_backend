package com.pccw.backend.bean.masterfile_trx_type;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@ApiModel(value="Trx_Type模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="属性值",name="transactionTypeName",example="")
    private String transactionTypeName;

    @ApiModelProperty(value="属性值",name="description",example="")
    private String description;

    @ApiModelProperty(value="属性值",name="transactionAction",example="")
    private String transactionAction;

    @ApiModelProperty(value="属性值",name="transactionSource",example="")
    private String transactionSource;

    @ApiModelProperty(value="属性值",name="disableDate",example="")
    private Long disableDate;

    @ApiModelProperty(value="属性值",name="serialControl",example="Y")
    private String serialControl;

    @ApiModelProperty(value="属性值",name="natureType",example="In/Out/Transfer")
    private String natureType;
}
