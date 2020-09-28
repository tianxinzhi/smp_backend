package com.pccw.backend.bean.reservation_rule;

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
@ApiModel(value="Stock_ReservationCtrl",description="")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.IN)
    @ApiModelProperty(value="sku",name="sku",example="")
    private List<String> skuId;
}
