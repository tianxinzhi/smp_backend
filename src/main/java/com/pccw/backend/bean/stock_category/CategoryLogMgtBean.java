package com.pccw.backend.bean.stock_category;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_Category主模块 - CategoryLogMgtBean",description="")
public class CategoryLogMgtBean extends BaseBean {

    @ApiModelProperty(value="remark",name="备注",example="")
    private String remark;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logTxtBum;

    @ApiModelProperty(value="logRepoOut",name="From",example="")
    private long logRepoOut;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logType;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String logOrderNature;

    @ApiModelProperty(value="logTxtBum",name="logTxtBum",example="")
    private String status;

    @ApiModelProperty(value="dtls",name="交易明细",example="")
    private List<CategoryLogDtlBean> line;

}
