package com.pccw.backend.bean.reservation_rule;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Reservation_Rule",description="")
public class EditBean extends CreateBean {


    @ApiModelProperty(value="id",name="id",example="")
    private Long id;


}
