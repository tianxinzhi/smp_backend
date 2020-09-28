package com.pccw.backend.bean.stock_return;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_return",description="")
public class SearchBean extends BaseSearchBean {

    @ApiModelProperty(value="sku",name="sku",example="")
    @PredicateAnnotation(type = PredicateType.IN)
    private List<String> skuId;

    @ApiModelProperty(value="fromChannel",name="",example="")
    @PredicateAnnotation(type = PredicateType.EQUEL)
    private Long fromChannel;

    @ApiModelProperty(value="toWareHouse",name="",example="")
    @PredicateAnnotation(type = PredicateType.EQUEL)
    private Long toWareHouse;

    @ApiModelProperty(value="returnDate",name="",example="")
    private Long returnDate;
}
