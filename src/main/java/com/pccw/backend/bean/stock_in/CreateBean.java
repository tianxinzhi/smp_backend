package com.pccw.backend.bean.stock_in;

import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResLogMgtDtl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="StockIn - CreateBean",description="")
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

    @ApiModelProperty(value="deliveryDate",name="deliveryDate",example="")
    private long deliveryDate;

    @ApiModelProperty(value="deliveryStatus",name="deliveryStatus",example="")
    private String deliveryStatus;

    private Long originId;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResLogMgtDtl> line;
}
