package com.pccw.backend.bean.stock_threshold;




import com.pccw.backend.bean.masterfile_class.CreateBean;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Stock_threshold模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
    private Long qtyy;
    private Long repoId;
    private String logTxtBum;
    private String remark;
    
}