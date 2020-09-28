package com.pccw.backend.bean.masterfile_item;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Item 模块 - SearchBean", description = "")
public class CreateBean extends BaseBean {

    private Long id;

    @ApiModelProperty(value="物料编码",name="itemCode",example="")
    private String itemCode;

    @ApiModelProperty(value="物料名称",name="itemName",example="")
    private String itemName;
}
