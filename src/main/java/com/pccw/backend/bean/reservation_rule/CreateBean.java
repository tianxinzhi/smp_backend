package com.pccw.backend.bean.reservation_rule;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_Reservation",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="customerType",name="",example="")
    private String customerType;

    @ApiModelProperty(value="skuId",name="",example="")
    private Long skuId;

    @ApiModelProperty(value="paymentStatus",name="",example="")
    private String paymentStatus;

    @ApiModelProperty(value="priority",name="",example="")
    private int priority;
}
