package com.pccw.backend.bean.process_process;




import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResProcessDtl;
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
 @ApiModel(value="模块 - EditBean",description="")
public class EditBean extends BaseBean {

    private Long id;

    @ApiModelProperty(value="status",name="status",example="")
    private String status;

    private String statusPro;

    private String logTxtBum;

    private Long repoId;

    private String logOrderNature;

    private Long flowId;

    private String remark;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<Step> steps;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResProcessDtl> processDtls;


}