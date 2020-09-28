package com.pccw.backend.bean.masterfile_ccc;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Ccc 模块 - EditBean", description = "")
public class EditBean extends CreateBean {
    private Long id;
}
