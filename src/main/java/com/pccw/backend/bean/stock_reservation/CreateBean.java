package com.pccw.backend.bean.stock_reservation;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_Reservation",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="customerName",name="",example="")
    private String customerName;

    @ApiModelProperty(value="logTxtBum",name="",example="")
    private String logTxtBum;

    @ApiModelProperty(value="orderNo",name="",example="")
    private String orderNo;

    @ApiModelProperty(value="customerType",name="",example="")
    private String customerType;

    @ApiModelProperty(value="skuId",name="",example="")
    private Long skuId;

    @ApiModelProperty(value="repoId",name="",example="")
    private Long repoId;

    @ApiModelProperty(value="qty",name="",example="")
    private Long qty;

    @ApiModelProperty(value="staffId",name="",example="")
    private String staffId;

    @ApiModelProperty(value="reservationDate",name="",example="")
    private Long reservationDate;

    @ApiModelProperty(value="orderDate",name="",example="")
    private Long orderDate;

    @ApiModelProperty(value="paymentStatus",name="",example="")
    private String paymentStatus;

    @ApiModelProperty(value="days",name="",example="")
    private Long days;

    @ApiModelProperty(value="selected",name="",example="")
    private String selected;

    @ApiModelProperty(value="remark",name="",example="")
    private String remark;
}
