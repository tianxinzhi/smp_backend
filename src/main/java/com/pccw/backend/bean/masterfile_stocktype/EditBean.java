package com.pccw.backend.bean.masterfile_stocktype;




import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="StockType 模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
    
}