package com.pccw.backend.bean.masterfile_ccc;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Ccc 模块 - SearchBean", description = "")
public class CreateBean extends BaseBean {

    private Long id;

    @ApiModelProperty(value="CCCcode",name="cccCode",example="")
    private String cccCode;

    @ApiModelProperty(value="CCCName",name="cccName",example="")
    private String cccName;
}
