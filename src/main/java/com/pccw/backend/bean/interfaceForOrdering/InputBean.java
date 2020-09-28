package com.pccw.backend.bean.interfaceForOrdering;


import com.pccw.backend.bean.BaseBean;
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
 @ApiModel(value="Stock update模块 - InputBean",description="")
public class InputBean extends BaseBean {

    @ApiModelProperty(value="门店接收",name="logRepoIn",example="")
    private String order_system;

    @ApiModelProperty(value="门店出货",name="logRepoOut",example="")
    private String order_id ;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String request_nature ;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String sales_id ;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<InputItemBean> item_details;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String tx_date ;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String biz_date ;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String remarks;





}