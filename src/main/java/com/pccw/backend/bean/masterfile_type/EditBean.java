package com.pccw.backend.bean.masterfile_type;




import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Type模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
    
}