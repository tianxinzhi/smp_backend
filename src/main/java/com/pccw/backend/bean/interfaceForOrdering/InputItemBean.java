package com.pccw.backend.bean.interfaceForOrdering;


import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Stock out模块 - CreateBean",description="")
public class InputItemBean extends BaseBean {

    @ApiModelProperty(value="门店接收",name="logRepoIn",example="")
    private String detail_id;

    @ApiModelProperty(value="门店出货",name="logRepoOut",example="")
    private String item_action;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String action_status;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String sku_code;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String quantity;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String item_code;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String repo_id;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String ccc;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String wo;




}