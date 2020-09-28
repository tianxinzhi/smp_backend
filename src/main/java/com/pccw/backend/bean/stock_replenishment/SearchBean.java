package com.pccw.backend.bean.stock_replenishment;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchBean {
    private String logBatchId;
    private String logDnNum;

    @ApiModelProperty(value="sku",name="sku",example="")
    @PredicateAnnotation(type = PredicateType.IN)
    private Long skuId;

    private String[] createAt;
    private String logTxtNum;
    private Integer pageIndex;
    private Integer pageSize;
}
