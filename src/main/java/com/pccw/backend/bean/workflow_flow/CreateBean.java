package com.pccw.backend.bean.workflow_flow;


import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResFlowStep;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
public class CreateBean extends BaseBean{

    private String flowName;

    private String flowDesc;

    private String flowNature;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResFlowStep> resFlowStepList;

    
}