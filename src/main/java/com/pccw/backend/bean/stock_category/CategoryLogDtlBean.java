package com.pccw.backend.bean.stock_category;


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
 @ApiModel(value="StockCategory 模块 - CategoryLogDtlBean",description="")
public class CategoryLogDtlBean extends BaseBean {

    @ApiModelProperty(value="dtlSkuId",name="dtlSkuId",example="")
    private long dtlSkuId;

    @ApiModelProperty(value="dtlItemId",name="dtlItemId",example="")
    private long dtlItemId;

    @ApiModelProperty(value="仓库Id",name="dtlRepoId",example="")
    private long dtlRepoId;

    @ApiModelProperty(value="数量",name="dtlQty",example="")
    private long dtlQty;

    @ApiModelProperty(value="交易号",name="logTxtBum",example="")
    private String logTxtBum;

    @ApiModelProperty(value="dtlLogId",name="dtlLogId",example="")
    private long dtlLogId;

    @ApiModelProperty(value="业务动作",name="dtlAction",example="")
    private String dtlAction;

    @ApiModelProperty(value="dtlSubin",name="dtlSubin",example="")
    private String dtlSubin;

    @ApiModelProperty(value="lisStatus",name="lisStatus",example="")
    private String lisStatus;

    @ApiModelProperty(value="lisResult",name="lisResult",example="")
    private String lisResult;

    @ApiModelProperty(value="Status",name="Status",example="")
    private String status;

    //根据以下两个字段值确认dtlAction dtlSubin Status的字段值
    @ApiModelProperty(value="toStockTypeId",name="toStockTypeId",example="")
    private long toStockTypeId;

    @ApiModelProperty(value="fromStockTypeId",name="fromStockTypeId",example="")
    private long fromStockTypeId;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String itemCode;






}