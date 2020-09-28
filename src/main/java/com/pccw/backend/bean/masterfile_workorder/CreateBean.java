package com.pccw.backend.bean.masterfile_workorder;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "WorkOrder 模块 - SearchBean", description = "")
public class CreateBean extends BaseBean {

    private Long id;

    @ApiModelProperty(value="工单code",name="woCode",example="")
    private String woCode;

    @ApiModelProperty(value="工单Name",name="woName",example="")
    private String woName;
}
