package com.pccw.backend.bean.masterfile_productline;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "ProductLine 模块 - SearchBean", description = "")
public class CreateBean extends BaseBean {

    private Long id;

    @ApiModelProperty(value="生产线code",name="plCode",example="")
    private String plCode;

    @ApiModelProperty(value="生产线Name",name="plName",example="")
    private String plName;
}
