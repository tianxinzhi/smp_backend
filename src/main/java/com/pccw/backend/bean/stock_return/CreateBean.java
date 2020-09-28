package com.pccw.backend.bean.stock_return;

import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResStockReturnSerial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_return",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="returnHeaderId",name="",example="")
    private String returnHeaderId;

    @ApiModelProperty(value="returnLineId",name="",example="")
    private String returnLineId;

    @ApiModelProperty(value="SkuId",name="",example="")
    private Long skuId;

    @ApiModelProperty(value="fromChannel",name="",example="")
    private Long fromChannel;

    @ApiModelProperty(value="toWareHouse",name="",example="")
    private Long toWareHouse;

    @ApiModelProperty(value="qty",name="",example="")
    private Long qty;

    @ApiModelProperty(value="returnDate",name="",example="")
    private Long returnDate;

    @ApiModelProperty(value="reason",name="",example="")
    private String reason;

    @ApiModelProperty(value="status",name="",example="")
    private String status;

    @ApiModelProperty(value="remarks",name="",example="")
    private String remarks;

    @ApiModelProperty(value="logTxtNum",name="",example="")
    private String logTxtNum;

    private List<DbResStockReturnSerial> line;
}
