package com.pccw.backend.bean.stock_category;


import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Sku_Repo模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="商店ID",name="repoId",example="")
    private Long repoId;

    @ApiModelProperty(value="备注",name="remark",example="")
    private String remark;

    private List<CreateBeandtl> line;


    
}