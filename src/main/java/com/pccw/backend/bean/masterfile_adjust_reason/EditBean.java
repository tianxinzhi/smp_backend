package com.pccw.backend.bean.masterfile_adjust_reason;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Adjust_Reason模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
}
