package com.pccw.backend.bean.stock_out;


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
 @ApiModel(value="Stock out模块 - CreateBean",description="")
public class ProcessCreateBean extends BaseBean {

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logTxtBum;

    @ApiModelProperty(value="门店出货",name="logRepoOut",example="")
    private Long repoId;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logType;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logOrderNature;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String status;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String remark;

    private Long flowId;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResProcessDtl> processDtls;


}