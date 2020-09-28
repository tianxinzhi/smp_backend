package com.pccw.backend.bean.stock_category;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="TypeSkuSpec - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;

    private Long toStockTypeId;
}