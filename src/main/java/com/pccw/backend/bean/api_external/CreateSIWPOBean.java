package com.pccw.backend.bean.api_external;

import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResLogMgtDtl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-01 14:56
 **/
@Data
@NoArgsConstructor
public class CreateSIWPOBean extends BaseBean {
    @ApiModelProperty(value="门店接收",name="storeNameTo",example="")
    private String channelCodeTo;

    @ApiModelProperty(value="门店出货",name="storeNameFrom",example="")
    private String channelCodeFrom;

//    @ApiModelProperty(value="门店接收",name="logRepoIn",example="")
    private long logRepoIn;

//    @ApiModelProperty(value="门店出货",name="logRepoOut",example="")
    private long logRepoOut;

    @ApiModelProperty(value="deliveryNumber",name="deliveryNumber",example="")
    private String deliveryNumber;

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

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<DbResLogMgtDtl> line;
}
