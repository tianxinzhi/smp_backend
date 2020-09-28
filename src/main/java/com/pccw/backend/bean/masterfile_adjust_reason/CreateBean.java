package com.pccw.backend.bean.masterfile_adjust_reason;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Adjust_Reason模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="调整名",name="adjustReasonName",example="")
    private String adjustReasonName;

    @ApiModelProperty(value="备注",name="remark",example="")
    private String remark;
}
