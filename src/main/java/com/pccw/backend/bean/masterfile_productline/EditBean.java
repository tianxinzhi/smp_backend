package com.pccw.backend.bean.masterfile_productline;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "ProductLine 模块 - EditBean", description = "")
public class EditBean extends CreateBean {
    private Long id;
}
