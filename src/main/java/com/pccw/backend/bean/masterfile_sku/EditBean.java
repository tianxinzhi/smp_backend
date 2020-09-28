package com.pccw.backend.bean.masterfile_sku;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Sku模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
}
