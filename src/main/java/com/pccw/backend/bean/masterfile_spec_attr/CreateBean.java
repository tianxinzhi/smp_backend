package com.pccw.backend.bean.masterfile_spec_attr;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Spec_Attr模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="specId",name="specId",example="")
    private Long specId;

    @ApiModelProperty(value="specId",name="specId",example="")
    private String verId;

    @ApiModelProperty(value="attrId",name="attrId",example="")
    private Long attrId;

    @ApiModelProperty(value="attrValueId",name="attrValueId",example="")
    private Long attrValueId;

}
