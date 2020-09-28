package com.pccw.backend.bean.stock_out;


import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResLogMgtDtl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Stock out模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="门店接收",name="logRepoIn",example="")
    private long logRepoIn;

    @ApiModelProperty(value="门店出货",name="logRepoOut",example="")
    private long logRepoOut;


    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logTxtBum;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logType;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logOrderNature;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String status;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String remark;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String repoType;

    @ApiModelProperty(value="deliveryDate",name="deliveryDate",example="")
    private long deliveryDate;

    @ApiModelProperty(value="deliveryStatus",name="deliveryStatus",example="")
    private String deliveryStatus;

    @ApiModelProperty(value="deliveryNumber",name="deliveryNumber",example="")
    private String deliveryNumber;

    @ApiModelProperty(value="staffNumber",name="staffNumber",example="")
    private String staffNumber;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResLogMgtDtl> line;


}
