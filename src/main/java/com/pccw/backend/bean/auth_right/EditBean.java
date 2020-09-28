package com.pccw.backend.bean.auth_right;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
@ApiModel(value="Right模块 - EditeBean",description="")
public class EditBean extends CreateBean {

    private Long id;
    
}