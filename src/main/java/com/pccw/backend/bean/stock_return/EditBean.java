package com.pccw.backend.bean.stock_return;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Stock_return",description="")
public class EditBean extends CreateBean {

    @ApiModelProperty(value="id",name="id",example="")
    private long id;

}
