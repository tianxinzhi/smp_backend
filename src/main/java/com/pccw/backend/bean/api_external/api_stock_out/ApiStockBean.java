package com.pccw.backend.bean.api_external.api_stock_out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-02 17:19
 **/
@Data
public class ApiStockBean{
    @ApiModelProperty(value="门店出货",name="channelOutCode",example="Code-01")
    private String channelOutCode;

    @ApiModelProperty(value="门店接收",name="channelInCode",example="Code-02")
    private String channelInCode;

    @ApiModelProperty(value="channelType",name="channelType",example="S")
    private String channelType;

    @ApiModelProperty(value="status",name="status",example="")
    private String remark;

    private List<ApiStockDtlBean> line;
}
