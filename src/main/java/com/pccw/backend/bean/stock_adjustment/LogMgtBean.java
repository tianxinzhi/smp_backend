package com.pccw.backend.bean.stock_adjustment;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_Adjustment主单模块 - LogMgtBean",description="")
public class LogMgtBean extends BaseBean {

    @ApiModelProperty(value="remark",name="备注",example="")
    private String remark;

    @ApiModelProperty(value="reason",name="原因",example="")
    private long reason;

    @ApiModelProperty(value="transactionNumber",name="交易编码",example="")
    private String transactionNumber;

    @ApiModelProperty(value="logRepoOut",name="From",example="")
    private long logRepoOut;

    @ApiModelProperty(value="logRepoIn",name="To",example="")
    private long logRepoIn;

    @ApiModelProperty(value="createDate",name="创建时间",example="")
    private long createDate;

    @ApiModelProperty(value="dtls",name="交易明细",example="")
    private List<LogMgtDtlBean> line;

}
