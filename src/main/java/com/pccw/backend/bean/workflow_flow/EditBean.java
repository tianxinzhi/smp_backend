package com.pccw.backend.bean.workflow_flow;




import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 
 */

@Data
@NoArgsConstructor
public class EditBean extends BaseBean {

    private Long id;

    private String flowName;

    private String flowDesc;

    private String flowNature;

    @ApiModelProperty(value="resFlowStepList",name="resFlowStepList",example="")
    private List<FlowStepEditBean> resFlowStepList;

}