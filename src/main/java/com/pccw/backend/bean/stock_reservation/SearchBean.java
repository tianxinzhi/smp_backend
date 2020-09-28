package com.pccw.backend.bean.stock_reservation;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseBean;
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

    @ApiModelProperty(value="customerType",name="customerType",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String customerType;

    @ApiModelProperty(value="repoId",name="repoId",example="")
    @PredicateAnnotation(type = PredicateType.EQUEL)
    private Long repoId;

    @ApiModelProperty(value="paymentStatus",name="paymentStatus",example="")
    @PredicateAnnotation(type = PredicateType.LIKE)
    private String paymentStatus;

    @ApiModelProperty(value="sku",name="sku",example="")
    @PredicateAnnotation(type = PredicateType.IN)
    private List<String> skuId;

    @ApiModelProperty(value="true",name="sortByRule",example="")
    private String sortByRule;
}
